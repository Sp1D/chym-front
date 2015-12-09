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
                    padding-top: 5%;
                    padding-bottom: 5%;
                    padding-left: 10%;
                    padding-right: 5%;
                }
            </style>
        </head>
        <body>
            <div class="navbar navbar-inverse navbar-fixed-top navbar-default">
                <div class="navbar-header"><a class="navbar-brand" href="#">Project name</a>

                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"> <span class="glyphicon glyphicon-bar"></span>
                        <span class="glyphicon glyphicon-bar"></span>

                        <span class="glyphicon glyphicon-bar"></span>
                    </button>
                </div>
                <div class="container">
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav">
                            <li class="active"><a href="#" class="">Home</a>

                            </li>
                            <li><a href="#about" class="">About</a>

                            </li>
                            <li><a href="#contact" class="">Contact</a>

                            </li>
                        </ul>
                    </div>
                    <!--/.navbar-collapse -->
                </div>
            </div>
            <div class="container target">
                <div class="row">
                    <div class="col-md-6 col-md-offset-3">
                        <div class="page-header text-center">
                            <h2>Movie Title<small> (2015)</small></h2>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4">
                        <p class="text-right">
                            <img src="http://placehold.it/350X350" class="img-responsive" align="right">
                        </p>
                    </div>
                    <div class="col-md-8">
                        <table class="table">
                            <tbody>
                                <tr>
                                    <td class="">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis pharetra
                                        varius quam sit amet vulputate. Quisque mauris augue, molestie tincidunt
                                        condimentum vitae, gravida a libero. Aenean sit amet felis dolor, in sagittis
                                        nisi. Sed ac orci quis tortor imperdiet venenatis. Duis elementum auctor
                                        accumsan. Aliquam in felis sit amet augue.</td>
                                </tr>
                                <tr>
                                    <td><b class="">Genre </b>Comedy, Porn, Detective, Fantasy</td>
                                </tr>
                                <tr>
                                    <td><b class="">IMDB </b>9.9</td>
                                </tr>
                                <tr>
                                    <td><b class="">Last release </b>31 Feb 2015</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-8 col-md-offset-4">
                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                            <div class="panel panel-default">
                                <div class="panel-heading" role="tab" id="headingOne">
                                    <h4 class="panel-title">
                                        <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne" class="">
                                            Season 3
                                        </a>
                                    </h4>

                                </div>
                                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                                    <div class="panel-body">
                                        <table class="table">
                                            <tbody>
                                                <tr>
                                                    <td>10</td>
                                                    <td>Episode title <span class="label label-info">new</span></td>
                                                </tr>
                                            </tbody></table>

                                    </div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading" role="tab" id="headingTwo">
                                    <h4 class="panel-title">
                                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                            Collapsible Group Item #2
                                        </a>
                                    </h4>

                                </div>
                                <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                                    <div class="panel-body">Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                        richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard
                                        dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf
                                        moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla
                                        assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore
                                        wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher
                                        vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic
                                        synth nesciunt you probably haven't heard of them accusamus labore sustainable
                                        VHS.</div>
                                </div>
                            </div>
                            <div class="panel panel-default">
                                <div class="panel-heading" role="tab" id="headingThree">
                                    <h4 class="panel-title">
                                        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                            Collapsible Group Item #3
                                        </a>
                                    </h4>

                                </div>
                                <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                                    <div class="panel-body">Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry
                                        richardson ad squid. 3 wolf moon officia aute, non cupidatat skateboard
                                        dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf
                                        moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla
                                        assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore
                                        wes anderson cred nesciunt sapiente ea proident. Ad vegan excepteur butcher
                                        vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic
                                        synth nesciunt you probably haven't heard of them accusamus labore sustainable
                                        VHS.</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /container -->
            </div>
            <script src="http://code.jquery.com/jquery-latest.js"></script>
            <script src="<c:url value="/static/js/bootstrap.min.js"></c:url>"></script>
    </body>
</html>
