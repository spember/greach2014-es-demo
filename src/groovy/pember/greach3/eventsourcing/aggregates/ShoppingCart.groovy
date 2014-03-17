package pember.greach3.eventsourcing.aggregates

import grails.validation.Validateable
import groovy.json.JsonBuilder
import pember.greach3.eventsourcing.eventstore.Aggregate

@Validateable
class ShoppingCart implements Aggregate{
    String id
    int revision = 0
    int itemCount = 0
    String type = "shopping_cart"
    Long ownerId
    Map skus = [:]

    String snapshot() {
        def builder = new JsonBuilder()
        builder([revision: revision, itemCount: itemCount, type: type, ownerId: ownerId], skus: skus)
        return builder.toString()
    }

    ShoppingCart applySnapshot(data) {
        //id = data.id

        def relations = data[0]
        def properties = data[1]

        revision = properties.revision
        itemCount = properties.itemCount
        ownerId = properties.ownerId
        println relations.skus
        //explicitly reset the skus
        skus = [:]
        relations.skus.each {k, v ->
            println "$k, $v"
            skus[k] = v
        }
        this
    }
}
