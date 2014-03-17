package pember.greach3.eventsourcing.eventstore

/**
 *
 *
 */
public interface EventStore {

    abstract insert(Aggregate aggregate, int expectedRevision, List<Event> events)
    abstract replay(Aggregate aggregate)
    abstract replayFromRevision(Aggregate aggregate, int revision)

}