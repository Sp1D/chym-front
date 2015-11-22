<%-- 
    Document   : episodes
    Created on : Nov 18, 2015, 9:33:23 PM
    Author     : sp1d
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">        
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/bootstrap-theme.css" rel="stylesheet">        
        <link href="<c:url value="/static/css/local.css"/>" rel="stylesheet">
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>

        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
                padding-left: 40px;
                padding-right: 40px;
            }
        </style>
        <title>CHYM</title>
    </head>
    <body>
        <table>
            <c:forEach var="episode" items="${episodesList}">

                <tr> 
                    <td valign="top"><img class="epiposter" src="${episode.getPoster()}"></td>
                    <td valign="top"><table>
                            <tr><td><strong><c:out value="${episode.getTitle()}" /></strong>
                                    season <c:out value="${episode.getSeason()}"/>, episode <c:out value="${episode.getEpisode()}"/></td></tr>                             
                            <tr><td><c:out value="${episode.getPlot()}"/></td></tr>
                            <tr><td>IMDB:<strong> <c:out value="${episode.getImdbRating()}"/></strong> (<c:out value="${episode.getImdbVotes()}"/> votes)</td></tr>
                            <tr><td>released: <c:out value="${episode.getReleased()}"/></td></tr>
                            <c:forEach var="torrent" items="${episode.getTorrents()}">
                                <tr><td><a href="<c:url value="${torrent.getTorrent()}"/>">Download <c:out value="${torrent.getTracker().getName()}"/>&nbsp;<c:out value="${torrent.getQuality()}"/> </a></td></tr>
                            </c:forEach>

                        </table></td>
                </tr>

                
            </c:forEach>
        </table>
    </body>
</html>
