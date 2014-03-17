package pember.greach3.eventsourcing

import grails.plugins.springsecurity.Secured
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.ShoppingCartEventLoader
import pember.greach3.eventsourcing.ecomm.ShoppingCartService

/**
 *
 * @author spember
 *
 * */

@Secured(['IS_AUTHENTICATED_FULLY'])
class ShoppingCartController {
    static responseFormats = ['json', 'xml']

    ShoppingCartService shoppingCartService
    ShoppingCartEventLoader cartEventLoader

    def index() {
        respond shoppingCartService.findCurrentCart()
    }

    def show(ShoppingCart shoppingCart) {
        cartEventLoader.replayFromSnapshot(shoppingCart)
        respond shoppingCart
    }


}
