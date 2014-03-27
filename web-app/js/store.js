(function (window) {
    "use strict";

    var shoppingCartURL = "/shoppingCart/";


    function updateCart(cart) {
        store.currentCart = cart;
        store.$cartPreview.fadeOut(function () {
            store.$cartPreview.html(cart.itemCount);
            store.$cartPreview.fadeIn();
        });
    }

    function renderCartItem(product) {
        var item = '<div class="cart-item clearfix">' +product.title;
        item += '/ quantity: <span class="quantity-label">' + store.currentCart.skus[product.sku] +'</span>';
        item += '/ Cost: $' + store.currentCart.skus[product.sku] * product.price;
        item += '<button class="pure-button delete" data-sku="' + product.sku +'">Delete Item</button>';
        item += '</div>';
        return item;
    }

    function renderCart() {
        store.$cartProducts.html('');
        store.$cart.fadeIn('fast', function () {
            $.ajax({url: '/cartProduct/listCurrent'})
                .done(function (data) {
                    _.each(data, function (product) {
                        store.$cartProducts.append(renderCartItem(product));
                    });
                    store.$cartProducts.append('<button class="pure-button pure-button-primary checkout">Checkout / Purchase</button>')
                })
                .fail(function () {
                    store.$cartProducts.html('<p class="error">Your cart is empty. ADD SOME THINGS TO IT, PLEASE.</p>');
                })

        });
    }


    var store = {
        currentCart: null,

        initialize: function () {
            // cache the cart preview
            store.$cartPreview = $('.header .cart-preview');
            store.$cart = $('.cart');
            store.$cartProducts = store.$cart.find('.products');

            //load the cart information
            $.ajax({url: shoppingCartURL}).done(function (data) {
                    updateCart(data);
                })
                .fail(function () {
                    alert("Failed to Load Cart!");
                });

            $('.cart-add').on('click', function (e) {
                e.stopPropagation();
                var $button = $(e.currentTarget),
                    quantity = $button.siblings('.quantity').val();
                $.ajax({method: 'POST', url: '/cartProduct/add/' + $button.data('sku'), data: {quantity: quantity}}).done(function (data) {
                        updateCart(data);
                    })
                    .fail(function () {
                        console.log("Failed to save!");
                    })
            });

            $('.cart-preview').on('click', function (e) {
                e.stopPropagation();
                renderCart();

            });

            store.$cart.find('.close').on('click', function (e) {
                store.$cart.fadeOut('fast');
            });

            store.$cartProducts.on('click', '.checkout', function (e) {
                store.$cartProducts.html('<i class="fa fa-spinner fa-spin fa-4x"></i>');
                // fake a timeout to simulate purchasing time
                setTimeout(function () {
                    $.ajax({url: '/cartProduct/checkout', method: 'POST'})
                        .done(function (data) {
                            updateCart(data);
                            store.$cartProducts.html('<p>Thank you for your Purchase. Now Buy more things!</p>')
                        });
                }, 1500);

            });

            store.$cartProducts.on('click', '.delete', function (e) {
                $.ajax({url: '/cartProduct/remove/' + $(e.currentTarget).data('sku'), method: 'DELETE', data: {quantity: 1}})
                    .done(function (data) {
                        updateCart(data);
                        renderCart();
                    })
                    .fail(function () {
                        console.log("Could not delete");
                    })
            })
        }
    }
    window.store = store;

})(window);
window.store.initialize();
