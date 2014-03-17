package pember.greach3.eventsourcing.ecomm

import grails.test.mixin.TestFor
import pember.greach3.eventsourcing.eventstore.EventStore
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ShoppingCartService)
class ShoppingCartServiceTests {
    EventStore eventStore

    def setup() {
    }

    def cleanup() {
    }

    void testSomething() {
        assert true
    }
}
