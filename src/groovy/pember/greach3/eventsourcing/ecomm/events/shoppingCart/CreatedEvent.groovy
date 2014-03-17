package pember.greach3.eventsourcing.ecomm.events.shoppingCart

import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.eventstore.Aggregate
import pember.greach3.eventsourcing.eventstore.BaseEvent

/**
 *
 * @author spember
 *
 * */
class CreatedEvent extends BaseEvent {
    static String type = "cart_create"

    Long ownerId

    CreatedEvent(ShoppingCart cart, Date date, Long ownerId) {
        this.aggregate = cart
        this.date = date
        this.ownerId = ownerId
    }

    def process() {
        // a little redundant as we maintain this information in a User->Aggregate mapping table. Consider removing
        this.aggregate.ownerId = this.ownerId
        super.process()
    }

    def serialize() {
        mapToString([ownerId: ownerId])
    }

    static createFromRowData(Aggregate cart, Date date, String data) {
        new CreatedEvent(cart, date, stringToMap(data).ownerId)
    }
}
