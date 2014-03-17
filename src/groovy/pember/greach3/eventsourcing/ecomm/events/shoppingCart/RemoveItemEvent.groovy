package pember.greach3.eventsourcing.ecomm.events.shoppingCart

import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.eventstore.BaseEvent

/**
 *
 * @author spember
 *
 * */
class RemoveItemEvent extends BaseEvent{

    static String type = "cart_remove_item"
    String sku

    RemoveItemEvent(ShoppingCart cart, Date date, String sku) {
        this.aggregate = cart
        this.sku = sku
    }

    def process() {
        super.process()
        if (this.aggregate.itemCount > 0) {
            this.aggregate.itemCount--
        }

        if (this.aggregate.skus.containsKey(sku) && this.aggregate.skus[sku] > 0) {
            this.aggregate.skus[sku]--
            // a little cleanup
            if (this.aggregate.skus[sku] <= 0) {
                this.aggregate.skus.remove(sku)
            }
        }
    }

    def serialize() {
        return mapToString([sku: sku])
    }

    static createFromRowData(ShoppingCart cart, Date date, String data) {
        new RemoveItemEvent(cart, date, stringToMap(data).sku)
    }
}
