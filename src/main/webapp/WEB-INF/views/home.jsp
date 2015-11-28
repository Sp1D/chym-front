<%-- 
    Document   : home
    Created on : Nov 22, 2015, 6:11:04 PM
    Author     : sp1d
--%>
<%@page import="java.util.Enumeration"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Shows</title>
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
            <nav class="navbar navbar-fixed-top navbar-default">
                <div class="container-fluid">
                    <!-- Brand and toggle get grouped for better mobile display -->
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="#">CHYM</a>
                    </div>

                    <!-- Collect the nav links, forms, and other content for toggling -->
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav">
                            <!--<li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>-->
                            <!--                            <li><a href="#">Sort By</a></li>-->
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Sort by <span class="caret"></span></a>
                                <ul class="dropdown-menu">                                    
                                    <li><a href="<c:url value="/?sortby=rating"/>">Rating</a></li>
                                    <li><a href="<c:url value="/?sortby=released"/>">Release date</a></li>
<!--                                    <li><a href="#">Genre</a></li>-->
                                    <li role="separator" class="divider"></li>
                                    <li><a href="#">Favorites</a></li>                                    
                                </ul>
                            </li>
                        </ul>
                        <!--                        <form class="navbar-form navbar-left" role="search">
                                                    <div class="form-group">
                                                        <input type="text" class="form-control" placeholder="Search">
                                                    </div>
                                                    <button type="submit" class="btn btn-default">Submit</button>
                                                </form>-->

                        <ul class="nav navbar-nav navbar-right">
                        <c:if test="${user == null}">
                            <li><a href="<c:url value="/signup"/>">Sign up</a></li>
                            <li><a href="<c:url value="/login"/>">Login</a></li>                            
                            </c:if>
                            <c:if test="${user != null}">                            
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-user"></span><c:out value="${' '.concat(user.username.concat(' '))}"/><span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Edit profile</a></li>                                    
                                    <li role="separator" class="divider"></li>
                                    <li><a href="<c:url value="/logout"/>">Logout</a></li>
                                </ul>
                            </li>
                        </c:if>
                    </ul>
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>
        <c:forEach var="currentRow" begin="1" end="${rows}">
            <div class="row">
                <c:forEach var="movie" items="${MovieFullList}" begin="${(currentRow-1)*cols}" end="${currentRow*cols-1}">
                    <div class="col-md-2">                                        

                        <span class="label label-warning rating"><span class="glyphicon glyphicon-stats"></span>&nbsp;${movie.getImdbRating()}</span>
                        <div class="poster"><a href="<c:url value="/episodes/${movie.getImdbID()}"/>"><img class="poster" src="<c:url value="${movie.getPoster()}"/>" ></a></div>


                        <div class="btn-group">
                            <button class="btn btn-default btn-poster" data-toggle="modal" data-target="#descModal<c:out value="${movie.getImdbID()}"/>">Description</button>


                            <div class="btn-group">
                                <button class="btn btn-success btn-poster">Download</button>
                                <button class="btn  btn-success dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Season 1</a></li>
                                    <li><a href="#">Season 2</a></li>                                                        
                                    <li><a href="#">Season 3...</a></li>
                                </ul>
                            </div>
                        </div> 
                        <!--<div class="movtitle"><--c:out value="${movie.getTitle()}" /></div>-->

                        <c:set var="id" value="${movie.getImdbID()}"/>
                        <div class="modal fade" id="descModal<c:out value="${movie.getImdbID()}"/>" tabindex="-1" role="dialog" aria-labelledby="descModal" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                        <h4 class="modal-title" id="descModal"><c:out value="${movie.getTitle()}" /></h4>
                                    </div>
                                    <div class="modal-body">

                                        <div class="descposter">
                                            <img class="descposter" src="<c:url value="${movie.getPoster()}"/>" >
                                        </div>

                                        <div class="descposter descdesc">                                        
                                            <p>genres: <c:forEach var="genre" items="${movie.getGenres()}"><c:out value="${genre.getContent()}"/>&nbsp;</c:forEach></p>
                                            <p>released: <c:out value="${movie.getReleased()}"/></p>
                                        </div>
                                        <div class="desctext">
                                            <p><c:out value="${movie.getPlot()}"/></p>
                                        </div>

                                    </div>
                                    <div class="modal-footer" style="clear:both;">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>                                

                                    </div>
                                </div>
                            </div>
                        </div>


                    </div>
                </c:forEach>  
            </div>

        </c:forEach>        

        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script src="<c:url value="/static/js/bootstrap.min.js"></c:url>"></script>
    </body>
</html>