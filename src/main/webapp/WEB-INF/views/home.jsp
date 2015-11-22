<%-- 
    Document   : home
    Created on : Nov 16, 2015, 1:41:23 AM
    Author     : sp1d
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CHYM</title>
    </head>
    <body>
        <table>
            <c:forEach var="currentRow" begin="1" end="${rows}">
                <tr>
                    <c:forEach var="movie" items="${MovieFullList}" begin="${(currentRow-1)*cols}" end="${currentRow*cols-1}">
                        <td>
                            <table>
                                <tr> 
                                    <td><a href="<c:url value="/episodes/${movie.getImdbID()}"/>"><img src="${movie.getPoster()}" height="400"></a></td>
                                    <td valign="top"><table>
                                            <tr><td><strong><c:out value="${movie.getTitle()}" /></strong></td></tr> 
                                            <tr><td>genres: <c:forEach var="genre" items="${movie.getGenres()}"><c:out value="${genre.getContent()}"/>&nbsp;</c:forEach></td></tr>   
                                            <tr><td><c:out value="${movie.getPlot()}"/></td></tr>
                                            <tr><td>IMDB:<strong> <c:out value="${movie.getImdbRating()}"/></strong> (<c:out value="${movie.getImdbVotes()}"/> votes)</td></tr>
                                            <tr><td>released: <c:out value="${movie.getReleased()}"/></td></tr>
                                            <tr><td><a href="<c:url value="/episodes/${movie.getImdbID()}"/>">episodes</a> </td></tr>

                                        </table></td>
                                </tr>
                            </table>
                        </td>

                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
