<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Greach Store"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: '/libs/pure-min.css')}" type="text/css">
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
        <r:require module="core"/>
		<g:layoutHead/>
		<r:layoutResources />

	</head>
	<body>
        <section class="header">
            <div class="header-menu pure-menu pure-menu-open pure-menu-horizontal pure-menu-fixed">
                <a class="pure-menu-heading" href="#">Greach 2014</a>
                <ul>
                    <li><div class="cart-preview"><i class="fa fa-spin fa-spinner"></i></div></li>
                    <li><a href="logout">Logout</a></li>
                </ul>
            </div>
        </section>
        <section class="content">
            <g:layoutBody/>
        </section>

		<section class="footer">

        </section>
		<r:layoutResources />
	</body>
</html>
