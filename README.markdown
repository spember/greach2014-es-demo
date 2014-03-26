#Greach 2014 - Event Sourcing Sample App

This repo contains a sample ecommerce application whose purpose is to demonstrate how easy it is to use event sourcing and integrate it in a Grails application.

### Getting Started

*	run the app via grails-app (note: app was written using grails 2.3.6)
*	navigate to localhost:8080/
*	login with username '__shopper__' and password '__password__'

The following actions are Event Sourced:

*	Click on the 'add to cart' button on the main page to add the items to your cart (You should see the cart item count increase)
*	Click on the cart count to show the cart... from here, you can remove items from your cart
*	From within the cart, one can also 'purchase' the items (without entering a credit card!)

### Important Concepts

This application uses an H2 Event Store, located externally from the Grails domain objects location. Package is ```pember.greach3.eventsourcing```, located in the ```src/groovy``` folder. The key files to look at are the:

*	```H2EventStore```: responsible for persisting and retrieving events. Built against H2 as opposed to something Redis or Mysql for quick development time. It is meant purely as a guide
*	```aggregates.ShoppingCart```: the main object representing a cart. int Event Sourcing terms, an Aggregate can be thought of as a particular instance, but acts as a receiver for a stream of events, and the object they act upon
*	```ecomm.events.shoppingCart.*```: The various event classes that can act on a Shopping Cart aggregate. Note the individual ```process()``` functions.
*	```ecomm.ShoppingCartEventLoader```: The Grails services use this file in order to proces events from the store. The event loader is responsible for reading the type of the event as it comes out of the store and hydrating it into the appropriate event type.

