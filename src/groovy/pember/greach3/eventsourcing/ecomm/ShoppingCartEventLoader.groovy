package pember.greach3.eventsourcing.ecomm

import groovy.json.JsonSlurper
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.AddItemEvent
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.CreatedEvent
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.PurchaseEvent
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.RemoveItemEvent
import pember.greach3.eventsourcing.eventstore.Aggregate
import pember.greach3.eventsourcing.eventstore.BaseEventLoader

/**
 *
 * @author spember
 *
 * */
class ShoppingCartEventLoader extends BaseEventLoader {
    List possibleEvents = [AddItemEvent, CreatedEvent, RemoveItemEvent, PurchaseEvent]
}
