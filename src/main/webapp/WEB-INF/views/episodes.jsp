<%-- 
    Document   : episodes2
    Created on : Nov 22, 2015, 1:24:21 AM
    Author     : sp1d
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Episodes</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">        
        <link href="<c:url value="/static/css/bootstrap.css"></c:url>" rel="stylesheet">        
        <link href="<c:url value="/static/css/local.css"></c:url>" rel="stylesheet">                

            <style type="text/css">
                body {
                    padding-top: 60px;
                    padding-bottom: 40px;
                    padding-left: 100px;
                    padding-right: 50px;
                }
            </style>
        </head>
        <body>
            <div class="episeries"><c:out value="${seriesTitle}"/></div>
        <div style="clear:left; height:15px;"></div>
        <c:forEach var="seasonNumber" begin="1" end="${lastSeason}">
            <div class="episeason"><c:out value="${seasonNumber}"/>&nbsp;season</div>
            <div style="clear:left; height:20px;"></div>

            <c:forEach var="episode" items="${episodesList}">
                <c:if test="${episode.getSeason() == seasonNumber && episode.getEpisode() <= episodesInSeasonsMap.get(seasonNumber)}">
                    <div class="row">
                        <div class="col-lg-12">                
                            <div class="epiepisode"><c:out value="${episode.getEpisode()}"/></div>
                            <div class="epiposter">
                                <img class="epiposter-img" src="<c:url value="${episode.getPoster()}"/>">
                                <div style="float:left;">
                                    <button class="btn  btn-success dropdown-toggle btn-sm btn-episode" data-toggle="dropdown">Download <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="#">Lostfilm HD1080</a></li>
                                        <li><a href="#">Lostfilm HD720</a></li>                                                        
                                        <li><a href="#">Lostfilm SD</a></li>
                                    </ul>                    
                                </div>

                            </div>
                            <div style="">
                                <p><strong><c:out value="${episode.getTitle()}"/></strong><br><span class="glyphicon glyphicon-time"></span><em>&nbsp;<c:out value="${episode.getReleased()}"/></em><br>
                                    <c:out value="${episode.getPlot()}"/>
                                </p>
                            </div>


                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </c:forEach>

        <script src="http://code.jquery.com/jquery-latest.js"></script>
        <script src="<c:url value="/static/js/bootstrap.min.js"></c:url>"></script>
    </body>
</html>
