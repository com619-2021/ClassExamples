<%-- 
    Document   : header
    Created on : Jan 4, 2020, 11:19:01 AM
    Author     : cgallen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<!-- start of header.jsp -->
<!-- userPrincipal.name ${pageContext.request.userPrincipal.name} -->
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="icon" href="../../favicon.ico">
        <!--<link rel="canonical" href="https://getbootstrap.com/docs/3.3/examples/navbar/">-->

        <title>Navbar Template for Bootstrap</title>

        <!-- Bootstrap core CSS -->
        <link href="./resources/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="./resources/css/navbar.css" rel="stylesheet">
        
         <!-- Custom styles for date time picker https://getdatepicker.com/4/Installing/#manual -->
        <link  href="./resources/css/bootstrap-datetimepicker.min.css" rel="stylesheet"/>
        <script type="text/javascript" src="./resources/js/jquery.min.js"></script>

    </head>

    <body>

        <header>

            <!-- Static navbar -->
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="#">Project name</a>
                    </div>
                    <div id="navbar" class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">

                            <!-- this jstl should work but problems with multiple if statements -->
                            <!-- selected page = ${selectedPage} home ${'home'.equals(selectedPage) } about ${'about'.equals(selectedPage) } contact ${'contact'.equals(selectedPage) }-->
                            <!--<li <c:if test="${'home'.equals(selectedPage) }"> class="active"  </c:if> ><a href="./home">Home</a></li>--> 
                                <!-- this raw java code works !! -->
                                <li <% if ("home".equals(request.getAttribute("selectedPage"))) {%> class="active"  <% } %> ><a href="./home">Home</a></li> 
                            <li <% if ("order".equals(request.getAttribute("selectedPage"))) {%> class="active"  <% } %> ><a href="./order">Orders</a></li> 
                            <li <% if ("orderchange".equals(request.getAttribute("selectedPage"))) {%>  class="active"  <% }%> ><a href="./orderchange">Order Changes</a></li> 
                            <li <% if ("resource".equals(request.getAttribute("selectedPage"))) {%> class="active"  <% } %> ><a href="./resource">Resource Inventory</a></li> 
                            <li <% if ("catalog".equals(request.getAttribute("selectedPage"))) {%> class="active"  <% }%> ><a href="./catalog">Resource Catalogue</a></li> 
                            <li <% if ("about".equals(request.getAttribute("selectedPage"))) {%>  class="active"  <% } %> ><a href="./about">About</a></li> 
                            <li <% if ("contact".equals(request.getAttribute("selectedPage"))) {%>  class="active"  <% }%> ><a href="./contact">Contact</a></li> 

                            <!--   <li><a href="./swagger-ui/index.html" target="_blank" >ReST API</a></li>  now set using javascript-->
                            <li><a id="swaggerLink" href="" target="_blank"  >ReST API</a></li>    
                                <sec:authorize access="hasRole('ROLE_GLOBAL_ADMIN')">
                                <li class="dropdown" >
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"> Admin <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="./users">Manage Users</a></li>
                                        <li><a href="./partys">Manage Partys</a></li>
                                    </ul>
                                </li>
                            </sec:authorize>

                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <c:if test="${pageContext.request.userPrincipal.name == null}">
                                <li><a href="./login">Login or create a new Account</a></li>
                                </c:if>
                                <c:if test="${pageContext.request.userPrincipal.name != null}">
                                <form id="logoutForm" method="POST" action="./logout">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                </form>
                                <form id="profile" method="GET" action="./viewModifyUser">
                                    <input type="hidden" name="username" value="${pageContext.request.userPrincipal.name}"/>
                                </form>
                                <p class="text-muted"> Welcome ${pageContext.request.userPrincipal.name}&nbsp;&nbsp;
                                    <a onclick="document.forms['logoutForm'].submit()">Logout</a><BR>
                                    <a onclick="document.forms['profile'].submit()">User Profile</a></p>
                                </c:if>
                        </ul>
                    </div><!--/.nav-collapse -->
                </div><!--/.container-fluid -->
            </nav>
        </header>
        <Script type="text/javascript">
<!-- this loads a script to dynamically set the url for swagger ui running in front of a reverse proxy in ReST API tag -->
            window.onload = function () {
                var swaggerJsonLink = window.location.href.substring(0, window.location.href.indexOf('/project-web')) + '/project-web/rest/openapi.json';
                var swaggerUILink = './swagger-ui/index.html?url=' + swaggerJsonLink;
                document.getElementById('swaggerLink').href = swaggerUILink;
                return false;
            };
        </script>
        <!-- end of header.jsp -->