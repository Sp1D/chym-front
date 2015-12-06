<%-- 
    Document   : signup
    Created on : Nov 22, 2015, 8:37:08 PM
    Author     : sp1d
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>CHYM - signup</title>
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
                <div class="row">
                    <div class="col-md-5 well">
                    <form:form action="signup" method="post" modelAttribute="user">
                        <fieldset>
                            <legend>Sign up</legend>
                            <div class="form-group">
                                <form:errors path="*" cssClass="form-error"
                                             />
                            </div>
                            <div class="form-group">                                
                                <form:label path="email" cssClass="control-label" cssErrorClass="control-label form-error">Email</form:label>
                                <form:input type="email" path="email" cssClass="form-control" cssErrorClass="form-control form-error" required="required"/>
                                <%--<form:errors path="email"/>--%>
                            </div>
                            <div class="form-group">                                
                                <form:label path="username" cssClass="control-label">Username</form:label>
                                <form:input  type="text" path="username" cssClass="form-control" maxlength="${settings['user.username.maxsize']}" required="required"/>
                                <%--<form:errors path="username"/>--%>
                            </div>
                            <div class="form-group">
                                <form:label path="password" cssClass="control-label">Password</form:label>
                                <form:password path="password" cssClass="form-control" required="required" />
                                <%--<form:errors path="password"/>--%>
                            </div>
                            <div class="form-group">
                                <form:label path="passwordCheck" cssClass="control-label">Re-enter password</form:label>
                                <form:password path="passwordCheck" cssClass="form-control" required="required"/>
                                <%--<form:errors path="passwordCheck"/>--%>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </fieldset>
                    </form:form>
                </div>
            </div>            
        </div>

        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script src="<c:url value="/static/js/bootstrap.min.js"></c:url>"></script>
    </body>
</html>
