package pember.greach3.eventsourcing.eventstore

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import pember.greach3.eventsourcing.eventstore.Aggregate
import pember.greach3.eventsourcing.eventstore.Event

/**
 *
 * @author spember
 *
 * */
class BaseEvent implements Event {

    Aggregate aggregate
    Date date

    def process() {
        aggregate.revision++
    }

    def getAggregateType() {
        return aggregate.type
    }

    def getAggregateId() {
        return aggregate.id
    }

    def serialize() {
        return ""
    }

    static createFromRowData(Aggregate aggregate, String data) {
        return null
    }


    static mapToString(Map data) {
        def builder = new JsonBuilder()
        builder(data)
        return builder.toString()
    }

    static stringToMap(String json) {
        return new JsonSlurper().parseText(json)

    }
}
