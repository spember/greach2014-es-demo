class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/shoppingCart"(resources:'shoppingCart')
        "/cartProduct/$action?/$id?(.$format)?"(controller: 'shoppingCartProductMapping')
        "/"(controller:'store', action: "index")
        "500"(view:'/error')
	}
}
