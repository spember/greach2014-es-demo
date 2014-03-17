package pember.greach3.eventsourcing.ecomm

import pember.greach3.eventsourcing.Product
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.eventstore.H2EventStore

/**
 *
 * @author spember
 *
 * */

class ShoppingCartServiceIntegrationTests extends GroovyTestCase {
    ShoppingCartService shoppingCartService
    H2EventStore eventStore
    Long userId = 10

    ShoppingCart cart
    Product hat
    Product shoes

    void setUp() {
        println "check"
        hat = new Product(sku: "123", title: "Great hat", description: "Sample Hat", price: new BigDecimal(14.5))
        shoes = new Product(sku: "4561", title: "Fantastic show", description: "Sample shoe", price: new BigDecimal(25.5))
    }

    void tearDown() {
    }

    // I should really be using Spock

    void testCartCreateAssignsUser() {
        cart = shoppingCartService.create(userId)
        assert cart.ownerId == userId
    }

    void testAddItem() {
        cart = shoppingCartService.create(userId)
        assert cart.skus == [:]
        shoppingCartService.addItem(cart, hat, 1)
        assert cart.itemCount == 1
        assert cart.skus[hat.sku] == 1
    }

    void testAddMultipleQuantities() {
        cart = shoppingCartService.create(userId)
        assert cart.skus == [:]
        shoppingCartService.addItem(cart, hat, 3)
        assert cart.itemCount == 3
        assert cart.skus[hat.sku] == 3
    }

    void testAddMultipleProducts() {
        cart = shoppingCartService.create(userId)
        shoppingCartService.addItem(cart, hat, 2)
        shoppingCartService.addItem(cart, shoes, 5)
        assert cart.itemCount == 7
        assert cart.skus[hat.sku] == 2
        assert cart.skus[shoes.sku] == 5
    }

    void testRemoveItemsBasic() {
        cart = shoppingCartService.create(userId)
        shoppingCartService.addItem(cart, hat, 1)
        assert cart.itemCount == 1
        assert cart.skus[hat.sku] == 1
        shoppingCartService.removeItem(cart, hat)
        assert cart.itemCount == 0
        assert !cart.skus.containsKey(hat.sku)
    }

    void testRemoteMultipleItems() {
        cart = shoppingCartService.create(userId)
        shoppingCartService.addItem(cart, hat, 1)
        shoppingCartService.addItem(cart, shoes, 3)
        assert  cart.itemCount == 4
        assert cart.skus[shoes.sku] == 3
        shoppingCartService.removeItem(cart, shoes)
        assert cart.itemCount == 3
        assert cart.skus[shoes.sku] == 2

        shoppingCartService.removeItem(cart, shoes)
        shoppingCartService.removeItem(cart, shoes)
        assert cart.itemCount == 1
        assert !cart.skus.containsKey(shoes.sku)
    }

    void testRemoveHasZeroMin() {
        cart = shoppingCartService.create(userId)
        shoppingCartService.addItem(cart, hat, 1)
        assert cart.itemCount == 1
        shoppingCartService.removeItem(cart, hat)
        assert cart.itemCount == 0
        assert !cart.skus.containsKey(hat.sku)
        shoppingCartService.removeItem(cart, hat)
        shoppingCartService.removeItem(cart, hat)
        assert cart.itemCount == 0
        assert !cart.skus.containsKey(hat.sku)
    }

    void testCartPurchase() {
        cart = shoppingCartService.create(userId)
        shoppingCartService.addItem(cart, hat, 3)
        shoppingCartService.addItem(cart, shoes, 5)
        assert cart.itemCount == 8

        shoppingCartService.purchaseCart(cart)
        assert cart.itemCount == 0
        assert !cart.skus.containsKey(hat.sku)
        assert !cart.skus.containsKey(shoes.sku)
    }

    void testSnapshotCreation() {
        cart = shoppingCartService.create(userId)
        // force the interval, although this will normally be a configuration option
        eventStore.snapshotInterval = 3

        shoppingCartService.addItem(cart, hat, 2)
        shoppingCartService.addItem(cart, shoes, 3)
        shoppingCartService.removeItem(cart, shoes)
        shoppingCartService.removeItem(cart, shoes)
        assert cart.revision == 8

        assert eventStore.loadSnapshot(cart, 9).size() == 1
        assert eventStore.loadSnapshot(cart, 7).size() == 1
        assert eventStore.loadSnapshot(cart, 5).size() == 1
        assert eventStore.loadSnapshot(cart, 3).size() == 1
        assert eventStore.loadSnapshot(cart, 1).size() == 0

    }
}
