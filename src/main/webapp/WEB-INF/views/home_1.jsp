<%-- 
    Document   : home
    Created on : Nov 22, 2015, 6:11:04 PM
    Author     : sp1d
--%>
<%@page import="net.sp1d.chym.entities.MovieFull"%>
<%@page import="net.sp1d.chym.entities.User"%>
<%@page import="java.util.Enumeration"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="f" uri="functions.tld"  %>




<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Shows</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">        
        <link href="<c:url value="/static/css/bootstrap.css"></c:url>" rel="stylesheet">        
        <link href="<c:url value="/static/css/local.css"></c:url>" rel="stylesheet"> 
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
                            <li>                            
                                <a href="#" data-toggle="modal" data-target="#displayModal">Display settings</a>                                
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

        <!--Display settings-->

        <div class="modal fade" id="displayModal" tabindex="-1" role="dialog" aria-labelledby="displayModal" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="displayModal">Display Settings</h4>
                    </div>
                    <div class="modal-body">
                        <form id="settings"> 
                            <div class="form-group">
                                <label for="sort">Sort order</label>
                                <select class="form-control" name="sort" id="sort">     
                                    <c:forTokens var="option" delims="," items="${sortValues}">
                                        <c:if test="${sortOrder eq option}">
                                            <option selected="selected"><c:out value="${option}"/></option>
                                        </c:if>
                                        <c:if test="${sortOrder ne option}">
                                            <option><c:out value="${option}"/></option>
                                        </c:if>
                                    </c:forTokens>
                                </select>
                            </div>
                            <!--                            <div class="checkbox">
                                                            <label>
                                                                <input type="checkbox" name="direction"  value="ascending">
                                                                Sort in ascending order
                                                            </label>
                                                        </div>-->
                            <div class="form-group">
                                <label for="size">Movies on page</label>
                                <select class="form-control" name="s" id="size" title="Movies count on page">                                        
                                    <c:forEach begin="24" end="48" step="6" var="option">
                                        <c:if test="${pageSize == option}">
                                            <option selected="selected"><c:out value="${option}"/></option>
                                        </c:if>
                                        <c:if test="${pageSize != option}">
                                            <option><c:out value="${option}"/></option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <label>Want to see your favorites always first ?</label>
                            <c:if test="${user != null}">
                                <c:if test="${favoritesFirst == true}">
                                    <div class="radio">                                        
                                        <label>                                            
                                            <input type="radio" name="favorites" id="optionsRadios1" value="yes" checked>
                                            Yes, please
                                        </label>
                                    </div>
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="favorites" id="optionsRadios2" value="no">
                                            No, thanks
                                        </label>
                                    </div>
                                </c:if>
                                <c:if test="${favoritesFirst == false}">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="favorites" id="optionsRadios1" value="yes">
                                            Yes, please
                                        </label>
                                    </div>
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="favorites" id="optionsRadios2" value="no" checked>
                                            No, thanks
                                        </label>
                                    </div>
                                </c:if>
                            </c:if>
                            <c:if test="${user == null}">
                                <div class="radio disabled">
                                    <label>                                        
                                        <input type="radio" name="favorites" id="optionsRadios1" value="yes" checked disabled>
                                        Yes, please
                                    </label>
                                </div>
                                <div class="radio disabled">
                                    <label>
                                        <input type="radio" name="favorites" id="optionsRadios2" value="no" disabled>
                                        No, thanks
                                    </label>
                                </div>
                            </c:if>
                        </form>                            
                    </div>
                    <div class="modal-footer" style="clear:both;">
                        <button type="submit" class="btn btn-success" form="settings">Submit</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Pages bar -->



        <nav>
            <div id="pagination">
<!--                <div class="row">
                    <div class="col-xs-6 col-md-6 col-md-offset-3">-->
                        <ul class="pagination">
                            <c:if test="${pagesFirst}">
                                <li class="disabled">
                                </c:if>
                                <c:if test="${!pagesFirst}">
                                <li>
                                </c:if>
                                <a aria-label="Previous" href="<c:url value="" >
                                       <c:param name="sort" value="${sortOrder}"/>    
                                       <c:param name="p" value="1"/>
                                       <c:param name="s" value="${pageSize}"/>
                                   </c:url>"><c:out value="${currentPage}"/><span aria-hidden="true">&laquo;</span></a>                        
                            </li>

                            <c:forEach var="currentPage" begin="1" end="${pagesTotal}" step="1">
                                <c:if test="${currentPage == pagesCurrent+1}">
                                    <li class="active">
                                    </c:if>
                                    <c:if test="${currentPage != pagesCurrent+1}">
                                    <li>
                                    </c:if>
                                    <a href="<c:url value="" >
                                           <c:param name="sort" value="${sortOrder}"/>    
                                           <c:param name="p" value="${currentPage}"/>
                                           <c:param name="s" value="${pageSize}"/>
                                       </c:url>"><c:out value="${currentPage}"/></a>
                                </li>

                            </c:forEach>
                            <c:if test="${pagesLast}">
                                <li class="disabled">
                                </c:if>
                                <c:if test="${!pagesLast}">
                                <li>
                                </c:if>
                                <a aria-label="Next" href="<c:url value="" >
                                       <c:param name="sort" value="${sortOrder}"/>    
                                       <c:param name="p" value="${pagesTotal}"/>
                                       <c:param name="s" value="${pageSize}"/>
                                   </c:url>"><c:out value="${currentPage}"/><span aria-hidden="true">&raquo;</span></a>   

                            </li>
                        </ul>
<!--                    </div>
                </div>-->
            </div>
        </nav>

        <!-- Content -->
        <div class="container-fluid">
        <c:forEach var="currentRow" begin="1" end="${rows}">
            <div class="row">
                <c:forEach var="movie" items="${MovieFullList}" begin="${(currentRow-1)*cols}" end="${currentRow*cols-1}">
                    <div class="col-lg-2 col-md-4 col-sm-6">                                        
                        <div class="rating"><span class="label label-warning "><span class="glyphicon glyphicon-stats"></span>&nbsp;${movie.getImdbRating()}</span></div>
                        <c:if test="${user != null}">
                            <c:choose>
                                <c:when test="${user != null and f:colcontains(user.favorites, movie)}">
                                    <div class="favorite"><a href="<c:url value="/favorites/remove/${movie.imdbID}" />"><span class="label label-danger"><span class="glyphicon glyphicon-heart icon-favorite"></span></span></a></div>
                                            </c:when>
                                            <c:otherwise>
                                    <div class="favorite"><a href="<c:url value="/favorites/add/${movie.imdbID}" />"><span class="label label-white"><span class="glyphicon glyphicon-heart-empty icon-love"></span></span></a></div>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                        <div class="poster" ><a href="<c:url value="/episodes/${movie.getImdbID()}"/>"><img src="<c:url value="${movie.getPoster()}"/>" alt="<c:out value="${movie.getTitle()}" />"></a></div>
                        <div class="btn-group">
                            <button class="btn btn-default btn-poster" data-toggle="modal" data-target="#descModal<c:out value="${movie.getImdbID()}"/>"><c:out value="${movie.getTitle()}"/></button>
<!--                            <div class="btn-group">
                                <button class="btn btn-success btn-poster">Download</button>
                                <button class="btn  btn-success dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Season 1</a></li>
                                    <li><a href="#">Season 2</a></li>                                                        
                                    <li><a href="#">Season 3...</a></li>
                                </ul>
                            </div>-->
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
        </div>
        
        <!-- Pages bar -->



        <nav>
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-6 col-md-6 col-md-offset-3">
                        <ul class="pagination">
                            <c:if test="${pagesFirst}">
                                <li class="disabled">
                                </c:if>
                                <c:if test="${!pagesFirst}">
                                <li>
                                </c:if>
                                <a aria-label="Previous" href="<c:url value="" >
                                       <c:param name="sort" value="${sortOrder}"/>    
                                       <c:param name="p" value="1"/>
                                       <c:param name="s" value="${pageSize}"/>
                                   </c:url>"><c:out value="${currentPage}"/><span aria-hidden="true">&laquo;</span></a>                        
                            </li>

                            <c:forEach var="currentPage" begin="1" end="${pagesTotal}" step="1">
                                <c:if test="${currentPage == pagesCurrent+1}">
                                    <li class="active">
                                    </c:if>
                                    <c:if test="${currentPage != pagesCurrent+1}">
                                    <li>
                                    </c:if>
                                    <a href="<c:url value="" >
                                           <c:param name="sort" value="${sortOrder}"/>    
                                           <c:param name="p" value="${currentPage}"/>
                                           <c:param name="s" value="${pageSize}"/>
                                       </c:url>"><c:out value="${currentPage}"/></a>
                                </li>

                            </c:forEach>
                            <c:if test="${pagesLast}">
                                <li class="disabled">
                                </c:if>
                                <c:if test="${!pagesLast}">
                                <li>
                                </c:if>
                                <a aria-label="Next" href="<c:url value="" >
                                       <c:param name="sort" value="${sortOrder}"/>    
                                       <c:param name="p" value="${pagesTotal}"/>
                                       <c:param name="s" value="${pageSize}"/>
                                   </c:url>"><c:out value="${currentPage}"/><span aria-hidden="true">&raquo;</span></a>   

                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script src="<c:url value="/static/js/bootstrap.min.js"></c:url>"></script>
    </body>
</html>