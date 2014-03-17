package pember.greach3.eventsourcing.ecomm.events.shoppingCart

import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.eventstore.BaseEvent

/**
 *
 * @author spember
 *
 * */
class PurchaseEvent extends BaseEvent {
    static String type = "cart_purchase"

    PurchaseEvent(ShoppingCart cart, Date date) {
        this.aggregate = cart
        this.date = date
    }

    /**
     * Only clears the cart, but conceivably do other activities in an actual application
     */
    def process() {
        ((ShoppingCart)this.aggregate).skus = [:]
        ((ShoppingCart)this.aggregate).itemCount = 0
        super.process()
    }

    static createFromRowData(ShoppingCart cart, Date date, String data) {
        new PurchaseEvent(cart, date)
    }

}
