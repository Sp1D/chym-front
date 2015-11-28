<%-- 
    Document   : registered
    Created on : Nov 23, 2015, 12:19:26 AM
    Author     : sp1d
--%>

<%-- 
    Document   : signup
    Created on : Nov 22, 2015, 8:37:08 PM
    Author     : sp1d
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>CHYM - user registered</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">        
        <link href="<c:url value="/static/css/bootstrap.css"></c:url>" rel="stylesheet">        
        <link href="<c:url value="/static/css/local.css"></c:url>" rel="stylesheet">                

            <style type="text/css">
                body {
                    padding-top: 5%;
                    padding-bottom: 5%;
                    padding-left: 10%;
                    padding-right: 5%;
                }
            </style>
        </head>
        <body>
            <div class="container">                
                <h3>You are successfully
                    signed up</h3>
                <a href="<c:url value="/"/>">back to main page</a>
            </div>

            <script src="http://code.jquery.com/jquery-latest.js"></script>
            <script src="<c:url value="/static/js/bootstrap.min.js"/>"></script>
    </body>
</html>

