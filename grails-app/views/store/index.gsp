<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <r:require module="shopping"/>
</head>
<body>
<section class="parallax-container">
    <div class="title">
        <h1>Welcome to the Greach Store</h1>
    </div>
</section>
<section class="products-container">
    <h2>Check out our great products!</h2>
    <g:each in="${products}" var="product">
        <div class="product">
            <img class="product-image" src="${resource(dir: 'images', file: product.imageUrl)}" alt="product"/>
            <h3>${product.title}: (${product.sku})</h3>
            <p>${product.description}</p>
            <input class="quantity" type="text" value='1'/>
            <button class="pure-button pure-button-primary cart-add" data-sku="${product.sku}">Add to Cart!</button>
        </div>
    </g:each>
</section>

<section class="cart">
    <div class="close">X</div>
    <h2>Your current Cart:</h2>
    <div class="products">
        <i class="fa fa-spin fa-spinner fa-4x"></i></div>
    </div>
</section>

</body>
</html>