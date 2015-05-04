<%--
  Created by IntelliJ IDEA.
  User: jeslopalo
  Date: 24/04/15
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Live combos demo page</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

    <!-- JQuery -->
    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

    <script src="<c:url value="/resources/scripts/jquery.livecombos.js" />"></script>
</head>
<body>
<section class="container">
    <header>
        <spring:url value="/" var="homeUrl"/>
        <h1><a href="${homeUrl}">Hdiv Live combos</a></h1>
    </header>

    <spring:url var="actionUrl" value="/live-combos"/>
    <form:form action="${actionUrl}" modelAttribute="liveCombosForm">

        <spring:bind path="*">
            <c:choose>
                <c:when test="${fn:length(status.errorMessages) eq 1}">
                    <div class="alert alert-danger">
                        <c:forEach var="message" items="${status.errorMessages}">
                            <span>${message}</span>
                        </c:forEach>
                    </div>
                </c:when>
                <c:when test="${fn:length(status.errorMessages) gt 1}">
                    <div class="alert alert-danger">
                        <ul>
                            <c:forEach var="message" items="${status.errorMessages}">
                                <li>${message}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </spring:bind>

        <div class="row">
            <div class="col-md-4">
                <form:select path="firstValue" cssClass="form-control">
                    <c:forEach var="option" items="${firstValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>

            <div class="col-md-4">
                <form:select path="secondValue" cssClass="form-control">
                    <c:forEach var="option" items="${secondValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>

            <div class="col-md-4">
                <form:select path="thirdValue" cssClass="form-control">
                    <c:forEach var="option" items="${thirdValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <input type="submit" class="btn btn-default"/>
    </form:form>
</section>

<style>
    #map {
        padding: 3em;
        color: black;
    }

    .updating {
        border: 1px dotted skyblue;
    }

    .changed {
        color: red;
        font-weight: bold;

        -webkit-transition: all 2s ease-out;
        -moz-transition: all 2s ease-out;
        -o-transition: all 2s ease-out;
        transition: all 2s ease-out;
    }
</style>
<section id="map" class="container" style="width: 75%; white-space: nowrap; overflow:hidden; text-overflow: ellipsis;">
    <p>
        <span>modifyHdivStateParameter: ${modifyHDIVStateParameter}</span>
        <span>hdivStateParameter: <span id="hdiv-form-state-name"></span>, <span id="hdiv-form-state-value"></span>
            <script>
                $(function () {
                    setInterval(function () {
                        $("#map").addClass("updating");
                        $("#hdiv-form-state-value").removeClass("changed");

                        $("#hdiv-form-state-name").text($("form input[type=hidden][name!=_csrf]").last().attr("name"));
                        var oldValue = $("#hdiv-form-state-value").text();
                        if (oldValue != $("form input[type=hidden][name!=_csrf]").last().val()) {
                            $("#hdiv-form-state-value").text($("form input[type=hidden][name!=_csrf]").last().val());
                            $("#hdiv-form-state-value").addClass("changed");
                        }
                        $("#map").removeClass("updating");
                    }, 2000);
                });
            </script>
        </span>
    </p>
    <script>
        <%--
        var urls = {};

        <c:if test="${not empty firstValues.urls}">
        urls['${firstValues.path}'] = {
            <c:forEach items="${firstValues.urls}" var="url" varStatus="status">
            '${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'<c:if test="${!status.last}">, </c:if>
            </c:forEach>
        };

        </c:if>
        <c:if test="${not empty secondValues.urls}">
        urls['${secondValues.path}'] = {
            <c:forEach items="${secondValues.urls}" var="url" varStatus="status">
            '${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'<c:if test="${!status.last}">, </c:if>
            </c:forEach>
        };

        </c:if>
        <c:if test="${not empty thirdValues.urls}">
        urls['${thirdValues.path}'] = {
            <c:forEach items="${thirdValues.urls}" var="url" varStatus="status">
            '${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'<c:if test="${!status.last}">, </c:if>
            </c:forEach>
        };
        </c:if>
        --%>

        $(function () {
            $("select#firstValue").liveCombo({
                urls: {
                    <c:forEach items="${firstValues.urls}" var="url" varStatus="status">'${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'${!status.last?', ':''}</c:forEach>
                }
            });
            $("select#secondValue").liveCombo({
                urls: {
                    <c:forEach items="${secondValues.urls}" var="url" varStatus="status">'${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'${!status.last?', ':''}</c:forEach>
                }
            });
        })
    </script>
</section>

</body>
</html>
