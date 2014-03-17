package pember.greach3.eventsourcing.eventstore

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import javax.sql.DataSource

/**
 *
 * The H2EventStore acts as a wrapper for an Event Store
 *
 * It accepts Objects which implement the Event Interface. Each Event should be domain specific actions, e.g.
 *
 * A blog Post domain object could have 'PostCreatedEvent' or 'PostDeletedEvent'
 *
 */
class H2EventStore implements EventStore{

    DataSource dataSource
    // there are several ways we could generate the snapshots. Some folks suggest having a background tasks
    // responsible for going through and generating a snapshot per aggregate with a regular frequency.
    // Here, we simply use a configurable value that tells us the interval for recording snapshots every
    // <snapshotInterval> number of revisions
    int snapshotInterval

    private Sql sql

    public afterPropertiesSet() {
        // Create a SQL instance from the extracted dataSource
        sql = new Sql(dataSource.getConnection())
        // setup the EventStore tables
        initializeTables()
    }

    /**
     * Creates the EventStore tables
     */
    private initializeTables() {
        println "Creating Event Store Tables"
        sql.execute('''
            create table if not exists aggregates (
                id varchar(100) primary key not null,
                type varchar(256) not null,
                display_name varchar(256),
                revision integer not null
            )
        ''')

        sql.execute('''
            create table if not exists events (
                aggregate_id varchar(100) not null,
                type varchar(25) not null,
                data text not null,
                revision integer auto_increment not null,
                date_created timestamp,

                foreign key (aggregate_id)
                    references aggregates(id)
                    on delete cascade
            )
        ''')

        sql.execute('''
            create table if not exists snapshots (
                aggregate_id varchar(100) not null,
                revision integer not null,
                current_serialized_state text not null,

                foreign key (aggregate_id)
                    references aggregates(id)
                    on delete cascade
            )
        ''')
    }


    def replay(Aggregate aggregate) {
        //def query = "select * from events where aggregate_id = ? order by revision"
        loadEvents aggregate.id
    }

    def replay(Aggregate aggregate, int revision) {
        loadEvents aggregate.id, "and revision <= ?", [revision]
    }

    /**
     * Used mostly for replaying from a specific revision, e.g. a snapshot
     *
     * @param Aggregate
     * @param revision
     */
    def replayFromRevision(Aggregate aggregate, int revision) {
        loadEvents aggregate.id, "and revision > ?", [revision]
    }

    def loadEvents(String aggregateId, String constraints = "", List params = []) {
        def query = "select * from events where aggregate_id = ? $constraints order by revision"
        sql.rows query, [aggregateId] + params
    }

    def loadSnapshot(Aggregate aggregate, int revisionMax) {
        sql.rows "select * from snapshots where aggregate_id = ? and revision <= ? order by revision limit 1", [aggregate.id, revisionMax]
    }

    def loadSnapshot(Aggregate aggregate) {
        sql.rows("select * from snapshots where aggregate_id = ? order by revision limit 1", [aggregate.id])[0]
    }

    def loadAggregate(String aggregateId) {
        sql.rows("select * from aggregates where id = ?", [aggregateId])[0]
    }

    /**
     * Convenience method for handling one-off events.
     * @param aggregateId
     * @param expectedRevision
     * @param event
     * @return
     */
    def insert(aggregateId, expectedRevision, Event event) {
        insert aggregateId, expectedRevision, [event]
    }

    /**
     * As one might imagine, inserts a list of events for a specific Aggregate id
     *
     * @param aggregateId
     * @param expectedRevision the lowest version in the events list.
     * @param events list of events for the aggregate to insert in the order received
     */
    def insert(Aggregate aggregate, int expectedRevision, List<Event> events) {
        // Here we do most of the calculations in memory. We can (somewhat) get away with this because we're using an
        // in-memory database.
        // If using a normal Relational DB, one should make this insert a stored procedure.
        // Or, use Redis ;)
        if (events.size() > 0) {
            // THIS IS A RACE CONDITION, BTW. Do not use this in actual production code!
            // Purely for informational / demonstration purposes
            def revision = selectCurrentAggregateVersion(aggregate.id)
            // if no revision, assume the aggregate is new and create it
            if (!revision) {
                revision = 0
                insertNewAggregateFromEvent(events[0])
            }
            if (expectedRevision != revision) {
                //todo: change with with a custom exception
                throw new RuntimeException("Revisions not equal! Expecting $expectedRevision, received $revision")
            }
            events.each { event->
                revision++
                event.process()
                insertEvent event, revision
                if (revision % snapshotInterval == 0) {
                    insertSnapshot aggregate, revision
                }
            }
            // update the revision for an Aggregate once we've processed the revision
            updateAggregateRevision(aggregate.id, revision)
            //
        }
    }

    private selectCurrentAggregateVersion(aggregateId) {
        def rows = sql.rows "select revision from aggregates where id=:id", [id:aggregateId]
        assert rows.size() <= 1
        rows.revision[0]
    }

    private insertNewAggregateFromEvent(Event event) {
        sql.execute "insert into aggregates (id, type, revision) values (?, ?, ?)", [event.getAggregateId(), event.getAggregateType(), 0]
    }

    private insertEvent(Event event, revision) {
        sql.execute "Insert into events(aggregate_id, type, data, revision, date_created) values (?, ?, ?, ?, ?)", [event.getAggregateId(), event.type, event.serialize(), revision, new Date()]
    }

    private updateAggregateRevision(aggregateId, revision) {
        sql.execute "Update aggregates set revision = ? where aggregates.id = ?", [revision, aggregateId]
    }

    /**
     * Main method for creating a snapshot from an aggregate
     *
     *
     * @param aggregate
     */
    private insertSnapshot(Aggregate aggregate, revision) {
        sql.execute "insert into snapshots(aggregate_id, revision, current_serialized_state) values (?, ?, ?)", [aggregate.id, revision, aggregate.snapshot().toString()]
    }
}
