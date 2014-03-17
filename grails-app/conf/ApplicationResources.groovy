modules = {
    core {
        resource url:'/js/libs/jquery.js'
        resource url:'/js/libs/lodash.js'
    }

    shopping {
        dependsOn 'core'
        resource url: '/js/store.js'
    }
}