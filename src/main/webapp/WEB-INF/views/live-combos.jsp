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
                    <c:if test="${not empty firstValues.unselectedOptionLabel}">
                        <form:option value="">${firstValues.unselectedOptionLabel}</form:option>
                    </c:if>
                    <c:forEach var="option" items="${firstValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>

            <div class="col-md-4">
                <form:select path="secondValue" cssClass="form-control">
                    <c:if test="${not empty secondValues.unselectedOptionLabel}">
                        <form:option value="">${secondValues.unselectedOptionLabel}</form:option>
                    </c:if>
                    <c:forEach var="option" items="${secondValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>

            <div class="col-md-4">
                <form:select path="thirdValue" cssClass="form-control">
                    <c:if test="${not empty thirdValues.unselectedOptionLabel}">
                        <form:option value="">${thirdValues.unselectedOptionLabel}</form:option>
                    </c:if>
                    <c:forEach var="option" items="${thirdValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <input type="submit" class="btn btn-default"/>
    </form:form>
</section>

<section id="map" class="container" style="width: 75%; white-space: nowrap; overflow:hidden; text-overflow: ellipsis;">
    <p>
        <span>modifyHdivStateParameter: ${modifyHDIVStateParameter}</span>
        <span>hdivStateParameter: <script>document.write($("form input[type=hidden][name!=_csrf]").last().attr("name"));</script></span>
    </p>
    <script>
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

        function modifyUrl(url, modifyHdivFormStateParameter, hdivFormState) {
            if (hdivFormState && url.indexOf(modifyHdivFormStateParameter) == -1) {
                var position= url.indexOf(modifyHdivFormStateParameter);
                var suffix= "&" + modifyHdivFormStateParameter + "=" + hdivFormState.val();
                window.console.log("Adding modify hdiv state parameter:\n" + [url.slice(0, position), suffix, url.slice(position)].join(''));
                return [url.slice(0, position), suffix, url.slice(position)].join('');
            }
            window.console.log("Modify hdiv state paramter is already in url (or its not needed): " + url);
            return url;
        }

        $(function () {
            var modifyHdivFormStateParameter = "${modifyHDIVStateParameter}";

            $("form select").change(function () {
                var changedSelector = $(this);
                var selectName = changedSelector.attr("name");
                var selectedValue = changedSelector.find("option:selected").data("value");

                if (urls[selectName]) {
                    var url = urls[selectName][selectedValue];
                    if (url) {
                        var hdivFormStateHiddenInput = $("form input[type=hidden][name!=_csrf]").last();

                        $.getJSON(modifyUrl(url, modifyHdivFormStateParameter, hdivFormStateHiddenInput), function (data) {

                            if (data.csrf != null) {
                                hdivFormStateHiddenInput.val(data.csrf);
                            }
                            var targetControl = $("#" + data.path).empty();

                            if (data.unselectedOptionLabel) {
                                targetControl.append("<option value='0' selected='selected'>" + data.unselectedOptionLabel + "</option>");
                            }

                            $.each(data.options, function (index, option) {
                                targetControl.append("<option value='" + option.value + "' data-value=" + option.dataValue + ">" + option.label + "</option>");
                            });

                            urls[data.path] = data.urls;
                        });
                    }
                }
            });
        })
    </script>
</section>

</body>
</html>
