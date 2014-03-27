package pember.greach3.eventsourcing.eventstore

import groovy.json.JsonSlurper

/**
 *
 * @author spember
 *
 * */
class BaseEventLoader implements EventLoader {

    EventStore eventStore

    List possibleEvents = []

    /**
     * Replay the events from an aggregate to bring it up to current state
     *
     * @param aggregate
     * @return
     */
    def replay(Aggregate aggregate) {
        applyEvents aggregate, eventStore.replay(aggregate)

    }

    def replay(Aggregate aggregate, int revision) {
        applyEvents aggregate, eventStore.replay(aggregate, revision)
    }

    def replayFromSnapshot(Aggregate aggregate) {
        println "Looking for snapshot for aggregate $aggregate.id"
        def snapshot = eventStore.loadSnapshot(aggregate)
        if (snapshot) {
            def data = new JsonSlurper().parseText(snapshot.CURRENT_SERIALIZED_STATE?.asciiStream?.text)
            aggregate.applySnapshot(data)
        }
        def rows = eventStore.replayFromRevision aggregate, aggregate.revision
        applyEvents(aggregate, rows)
    }

    private applyEvents(Aggregate aggregate, rows) {
        parseEvents(aggregate, rows).each {event ->
            event.process()
        }
        aggregate
    }

    private parseEvents(Aggregate aggregate, rows) {
        List events = []
        rows.each { row ->
            possibleEvents.each{event->
                if (row.type == event.type) {
                    events.add event.createFromRowData(aggregate, row.DATE_CREATED, row.DATA?.asciiStream?.text)
                }
            }
        }
        events
    }

    def loadRawEventStreamForAggregate(Aggregate aggregate) {
        parseEvents aggregate, eventStore.replay(aggregate)
    }
}
