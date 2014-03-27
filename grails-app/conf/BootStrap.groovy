import pember.greach3.eventsourcing.Product
import pember.greach3.eventsourcing.Role
import pember.greach3.eventsourcing.User
import pember.greach3.eventsourcing.UserRole
import pember.greach3.eventsourcing.UserShoppingCart
import pember.greach3.eventsourcing.aggregates.ShoppingCart
import pember.greach3.eventsourcing.ecomm.ShoppingCartService

class BootStrap {

    ShoppingCartService shoppingCartService

    def init = { servletContext ->
        // seed database, eventually
        if (User.count() == 0) {
            // setup two roles and admin user
            Role member = new Role(authority:"member").save()
            Role admin = new Role(authority:"administrator").save()

            User shopper = new User(username: "shopper", password: "password").save()
            new UserRole(user: shopper, role:  member).save()

            User administrator = new User(username: "admin", password: "password").save()
            new UserRole(user: administrator, role: admin).save()
            print "Initial shopper created, instantiating cart"
            UserShoppingCart mapping = new UserShoppingCart(user: shopper)
            ShoppingCart cart = shoppingCartService.create(shopper.id)
            log.info "Created cart with id ${cart.id}"
            mapping.shoppingCartId = cart.id
            mapping.save()
        }

        if (Product.count() == 0) {
            new Product(sku: '12345-5678', imageUrl:'brown_shoes.jpg', title: 'Some Amazing Brown Shoes', description: 'It\'s like a hat for your feet!', size: 'L', price: new BigDecimal(325.0)).save()
            new Product(sku: '86753-0912', imageUrl:'coffee_shirt.jpg', title: 'Coffee T-Shirt', description: 'It\'s like pants for your chest!', size: 'L', price: new BigDecimal(55.0)).save()
            new Product(sku: '11111-2222', imageUrl:'jeans.jpg', title: 'Jeans', description: 'It\'s like a shirt for your legs!', size: 'M', price: new BigDecimal(90.0)).save()

        }


    }
    def destroy = {
    }
}
