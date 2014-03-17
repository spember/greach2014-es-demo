package pember.greach3.eventsourcing.eventstore

/**
 *
 * Used to retrieve an {@link Aggregate}'s event stream, determine the proper {@link Event} to parse, and process the events on
 * to the {@link Aggregate}
 */
public interface EventLoader {
    // Rebuilds an Aggregate from the entire event stream
    abstract replay(Aggregate aggregate)

    abstract replay(Aggregate aggregate, int revision)

    abstract replayFromSnapshot(Aggregate aggregate)
}