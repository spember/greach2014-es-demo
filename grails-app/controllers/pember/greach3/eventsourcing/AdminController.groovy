package pember.greach3.eventsourcing

import grails.converters.JSON
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.ShoppingCartEventLoader
import pember.greach3.eventsourcing.ecomm.ShoppingCartService

/**
 *
 * @author spember
 *
 * */
class AdminController {

    ShoppingCartEventLoader cartEventLoader
    ShoppingCartService shoppingCartService

    def index() {
        render view: "/store/admin", model: [events: cartEventLoader.loadRawEventStreamForAggregate(shoppingCartService.findCurrentCart())]
    }

    def data() {
        if (!params.id) {
            response.status = 404
            return
        }

        ShoppingCart cart = shoppingCartService.findCurrentCart()
        render cartEventLoader.loadRawEventStreamForAggregate(cart) as JSON
    }
}
