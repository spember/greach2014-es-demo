<%--
  Created by IntelliJ IDEA.
  User: spember
  Date: 3/16/14
  Time: 8:51 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Admin View</title>
</head>
<body>
    <h2>Admin View</h2>
    <h3>Cart History: </h3>
    <div id="chartContainer">
        <script src="${resource(dir: 'js', file: 'libs/Chart.js')}"></script>

        <ul>
        <g:each in="${events}" var="event">
            <li>${event.date} - ${event.type}</li>
        </g:each>
        </ul>
    </div>
</body>
</html>