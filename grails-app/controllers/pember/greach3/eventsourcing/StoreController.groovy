package pember.greach3.eventsourcing

import grails.plugins.springsecurity.Secured
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.ShoppingCartService

@Secured(['IS_AUTHENTICATED_FULLY'])
class StoreController {

    def index() {
        List products = Product.list()
        render view: "/store/index", model: [products: products]
    }
}
