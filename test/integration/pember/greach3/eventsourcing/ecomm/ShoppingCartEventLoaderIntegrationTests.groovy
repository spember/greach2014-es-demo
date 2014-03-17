package pember.greach3.eventsourcing.ecomm

import pember.greach3.eventsourcing.Product
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.eventstore.H2EventStore

/**
 *
 * @author spember
 *
 * */
class ShoppingCartEventLoaderIntegrationTests extends GroovyTestCase {

    ShoppingCartService shoppingCartService
    ShoppingCartEventLoader cartEventLoader
    H2EventStore eventStore

    Product product
    List<Product> products
    ShoppingCart cart
    String cartId

    Long ownerId = 10l

    void setUp() {
        // create a test cart with some sample events
        products = Product.list()
    }

    public setupCart() {
        // force the snapshotInterval to create more of them
        eventStore.snapshotInterval = 3
        // create a few events
        cart = shoppingCartService.create(ownerId)
        cartId = cart.id
        shoppingCartService.addItem(cart, products[0], 2)
        shoppingCartService.addItem(cart, products[0], 2)
        shoppingCartService.removeItem(cart, products[0])
        println cart.revision
    }

    void testReplay() {
        setupCart()
        ShoppingCart testCart = new ShoppingCart()
        testCart.id = cartId
        cartEventLoader.replay(testCart)
        assert testCart.revision == 6
        assert testCart.skus[products[0].sku] == 3
        assert testCart.ownerId == ownerId
    }

    void testReplaySelectedShort() {
        setupCart()
        ShoppingCart testCart = new ShoppingCart()
        testCart.id = cartId
        cartEventLoader.replay(testCart, 1)
        assert testCart.revision == 1
        assert testCart.skus.keySet().size() == 0
        assert testCart.ownerId == ownerId
    }

    void testReplaySelectedLonger() {
        setupCart()
        ShoppingCart testCart = new ShoppingCart()
        testCart.id = cartId
        cartEventLoader.replay(testCart, 3)
        assert testCart.revision == 3
        assert testCart.skus.keySet().size() == 1
        assert testCart.ownerId == ownerId
        assert testCart.skus[products[0].sku] == 2
    }

    void testLoadFromCurrentSnapshot() {
        setupCart()
        ShoppingCart testCart = new ShoppingCart()
        testCart.id = cartId

        cartEventLoader.replayFromSnapshot(testCart)

        assert testCart.id == cartId
        assert testCart.ownerId == ownerId
        assert testCart.revision == 6
        assert testCart.skus[products[0].sku] == 3

    }


}
