package pember.greach3.eventsourcing.eventstore

/**
 * An interface for each Command Object which wants to use the Event Store
 *
 */
public interface Aggregate {
    String id
    String type

    abstract String snapshot()
    abstract Aggregate applySnapshot(data)
}