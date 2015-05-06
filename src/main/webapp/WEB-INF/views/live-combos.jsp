<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Live combos demo page</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <c:url var="liveCombosJs" value="/resources/scripts/jquery.livecombos.js"/>
    <script src="${liveCombosJs}"></script>
    <style>
        body {
            padding: 3em;
        }

        body form {
            padding: 1em 0;
        }

        .botonera {
            text-align: right;
            padding-top: 3em;
        }

        .debug {
            margin-top: 2em;
            padding: 1em 2em;
            color: grey;
            width: 80%;
            background-color: gainsboro;
            word-wrap: break-word;
        }

        .hdiv-form-state-value {
            color: dimgrey;

            -webkit-transition: color 2s ease-in-out;
            -moz-transition: color 2s ease-in-out;
            -o-transition: color 2s ease-in-out;
            transition: color 2s ease-in-out;
        }

        .changed {
            color: red;

            -webkit-transition: color 2s ease-in-out;
            -moz-transition: color 2s ease-in-out;
            -o-transition: color 2s ease-in-out;
            transition: color 2s ease-in-out;
        }
    </style>
</head>
<body>
<section class="container">
    <header>
        <spring:url value="/" var="homeUrl"/>
        <h1><a href="${homeUrl}">Hdiv Live Combos</a></h1>
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
                    <%--<live:combo selector="${firstValues}" cssClass="form-control"/>--%>
                <form:select path="${firstValues.path}" cssClass="form-control">
                    <c:forEach var="option" items="${firstValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>
            </div>
            <div class="col-md-4">
                    <%--<live:combo selector="${secondValues}" cssClass="form-control"/>--%>
                <form:select path="${secondValues.path}" cssClass="form-control">
                    <c:forEach var="option" items="${secondValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>

            </div>
            <div class="col-md-4">
                    <%--<live:combo selector="${thirdValues}" cssClass="form-control"/>--%>
                <form:select path="${thirdValues.path}" cssClass="form-control">
                    <c:forEach var="option" items="${thirdValues.options}">
                        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
                    </c:forEach>
                </form:select>

            </div>
        </div>
        <div class="botonera">
            <input type="submit" class="btn btn-primary"/>
        </div>
    </form:form>

    <script>
        $(function () {
            $("select#firstValue").liveCombo({
                urls: {
                    <c:forEach items="${firstValues.urls}" var="url" varStatus="status">
                    '${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'${!status.last?', ':''}
                    </c:forEach>
                }
            });
            $("select#secondValue").liveCombo({
                urls: {
                    <c:forEach items="${secondValues.urls}" var="url" varStatus="status">
                    '${url.key}': '<spring:url value="${url.value}"><spring:param name="${modifyHDIVStateParameter}" value="${hdivFormStateId}" /></spring:url>'${!status.last?', ':''}
                    </c:forEach>
                }
            });
        })
    </script>
</section>
<section class="container debug">
    <header><h2>Debug Info</h2></header>

    <p><b>modifyHdivStateParameter:</b> ${modifyHDIVStateParameter}</p>

    <p><b>hdivStateParameterName:</b> <span id="hdiv-form-state-name"></span></p>

    <p><b>hdivStateParameterValue:</b> <span id="hdiv-form-state-value" class="hdiv-form-state-value"></span></p>
    <script>
        $(function () {
            setInterval(function () {
                var $hdivFormStateValue = $("#hdiv-form-state-value");
                var $hdivFormStateInputControl = $("form input[type=hidden][name!=_csrf]:last");

                $hdivFormStateValue.removeClass("changed");
                $("#hdiv-form-state-name").text($hdivFormStateInputControl.attr("name"));
                var oldValue = $hdivFormStateValue.text();
                if (oldValue != $hdivFormStateInputControl.val()) {
                    $hdivFormStateValue.text($hdivFormStateInputControl.val());
                    $hdivFormStateValue.addClass("changed");
                }
            }, 2000);
        });
    </script>
</section>
</body>
</html>
