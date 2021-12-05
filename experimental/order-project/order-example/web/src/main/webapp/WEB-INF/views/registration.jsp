<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Create an account</title>

        <link href="./resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="./resources/css/common.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>

    <body>

        <div class="container">
            <div style="color:red;">${errorMessage}</div>
            <div style="color:green;">${message}</div>

            <form id="userForm" class="form-signin" action="./registration" method="POST">
                <h2 class="form-signin-heading">Create your account</h2>

                <div class="form-group ">
                    <input id="username" name="username" placeholder="Username" type="text" autofocus="true" class="form-control" value=""/>
                </div>
                <div class="form-group ">
                    <input id="password" name="password" placeholder="Password" type="password" class="form-control" value=""/>
                </div>
                <div class="form-group ">
                    <input id="passwordConfirm" name="passwordConfirm" placeholder="Confirm your password" type="password" class="form-control" value=""/>
                </div>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
                <div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </div>
            </form>

        </div>
        <!-- /container -->
        <script src="./resources/js/jquery.min.js"></script>
        <script src="./resources/js/bootstrap.min.js"></script>
        <!--<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="./resources/js/bootstrap.min.js"></script>-->
    </body>
</html>
