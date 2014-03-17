import pember.greach3.eventsourcing.ecomm.ShoppingCartEventLoader
import pember.greach3.eventsourcing.eventstore.H2EventStore

// Place your Spring DSL code here
beans = {
    eventStore(H2EventStore) { bean ->
        bean.initMethod = 'afterPropertiesSet'
        dataSource = ref('dataSource')
        snapshotInterval = 5
    }

    cartEventLoader(ShoppingCartEventLoader) { bean ->
        eventStore = ref('eventStore')
    }
}