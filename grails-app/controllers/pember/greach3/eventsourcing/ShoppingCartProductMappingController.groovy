package pember.greach3.eventsourcing

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.ShoppingCartService

/**
 *
 * @author spember
 *
 * */
@Secured(['IS_AUTHENTICATED_FULLY'])
class ShoppingCartProductMappingController {
    static responseFormats = ['json', 'xml']
    static allowedMethods = [add: "POST", listCurrent: "GET", remove: "DELETE", checkout: "POST"]

    ShoppingCartService shoppingCartService


    def add()  {
        log.info "Attempting to add product with sku ${params.id}"
        render shoppingCartService.addItemToCurrentCart(params.id, Integer.parseInt(params.quantity)) as JSON
    }

    def remove() {
        log.info "Attempting to remove product with sku ${params.id}"
        render shoppingCartService.removeItemFromCurrentCart(params.id, 1) as JSON

    }

    def listCurrent() {
        ShoppingCart cart = shoppingCartService.findCurrentCart()
        List products = Product.findAllBySkuInList(cart.skus.keySet())
        if (products) {
            render products as JSON
        } else {
            response.status = 404
        }
    }

    def checkout() {
        ShoppingCart cart = shoppingCartService.findCurrentCart()
        shoppingCartService.purchaseCart cart
        render cart as JSON
    }
}
