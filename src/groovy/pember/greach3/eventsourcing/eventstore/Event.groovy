package pember.greach3.eventsourcing.eventstore

/**
 * Interface for all Domain Events to implement
 *
 */
interface Event {
    // need a type in order to hydrate the event
    String type

    abstract public process()
    // the 'expected' revision of the event. Should typically be the current revision of the aggregate
    //abstract public getRevision()
    abstract public getAggregateType()
    abstract public getAggregateId()

    abstract public serialize()
    abstract public createFromRowData(Aggregate aggregate, Date date, String data)
}
