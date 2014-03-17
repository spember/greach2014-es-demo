package pember.greach3.eventsourcing.ecomm.events.shoppingCart

import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.eventstore.Aggregate
import pember.greach3.eventsourcing.eventstore.BaseEvent

/**
 *
 * @author spember
 *
 * */
class AddItemEvent extends BaseEvent {
    static String type = "cart_add_item"

    String sku

    AddItemEvent(ShoppingCart cart, Date date, String sku) {
        this.aggregate = cart
        this.date = date
        this.sku = sku
    }

    def process() {
        this.aggregate.itemCount++
        if (!((ShoppingCart)this.aggregate).skus.containsKey(this.sku)) {
            this.aggregate.skus[sku] = 0
        }
        this.aggregate.skus[sku]++
        super.process()
    }

    def serialize() {
        return mapToString([sku: sku])
    }

    static createFromRowData(Aggregate cart, Date date, String data) {
        new AddItemEvent(cart, date, stringToMap(data).sku)
    }

}
