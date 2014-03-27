package pember.greach3.eventsourcing.ecomm

import grails.plugins.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import pember.greach3.eventsourcing.Product
import pember.greach3.eventsourcing.UserShoppingCart
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.AddItemEvent
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.CreatedEvent
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.PurchaseEvent
import pember.greach3.eventsourcing.ecomm.events.shoppingCart.RemoveItemEvent
import pember.greach3.eventsourcing.eventstore.EventLoader
import pember.greach3.eventsourcing.eventstore.EventStore
import static java.util.UUID.randomUUID

@Transactional
class ShoppingCartService {

    EventStore eventStore
    EventLoader cartEventLoader
    SpringSecurityService springSecurityService

    /**
     * Gets the current state of the cart for the current user.
     *
     * This is an example of a 'projection' or one way to view our Event Stream data. It's certainly the most common one
     *
      * @return
     */
    ShoppingCart findCurrentCart() {
        // note: should cache the current state of the aggregate for the user as a potential performance optimization
        // find the current user's cart's aggregateId
        def mapping = UserShoppingCart.findByUser(springSecurityService.getCurrentUser())
        // load that aggregate
        def rows = eventStore.loadAggregate mapping.shoppingCartId
        def cart = new ShoppingCart()
        cart.id = rows.id
        // and replay to the current state
        cartEventLoader.replay(cart)
        cart
    }

    def create(Long userId) {
        ShoppingCart cart = new ShoppingCart()
        cart.id = randomUUID().toString()
        log.info "Created cart with id = " + cart.id
        eventStore.insert cart, 0, [new CreatedEvent(cart, new Date(), userId)]
        cart
    }


    def addItem(ShoppingCart cart, Product product, int quantity=1) {
        def events = []
        quantity.times {
            events.push new AddItemEvent(cart, new Date(), product.sku)
        }
        eventStore.insert cart, cart.revision, events
        cart
    }

    def addItemToCurrentCart(String sku, int quantity = 1) {
        addItem findCurrentCart(), Product.findBySku(sku), quantity
    }

    def removeItemFromCurrentCart(String sku, int quantity = 1) {
        removeItem findCurrentCart(), Product.findBySku(sku), quantity
    }

    def removeItem(ShoppingCart cart, Product product, int quantity=1) {
        def events = []
        quantity.times {
            events.push new RemoveItemEvent(cart, new Date(), product.sku)
        }
        eventStore.insert cart, cart.revision, events
        cart
    }


    /**
     *
     * Should be called after payment is authorized, inventory is detected? Which should, by the way, happen in
     * a separate system from the product browsing. SOA!
     */
    def purchaseCart(ShoppingCart cart) {
        eventStore.insert cart, cart.revision, [new PurchaseEvent(cart, new Date())]
    }
}
