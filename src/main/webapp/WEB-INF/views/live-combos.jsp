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
    <spring:url value="/" var="homeUrl"/>
    <h1><a href="${homeUrl}">Home</a></h1>

    <spring:url var="actionUrl" value="/live-combos"/>
    <form:form action="${actionUrl}" modelAttribute="form">

        <div style="border: 1px solid red; display: block; padding: 1em; ">
            <form:errors path="*"/>
        </div>

        <div class="row">
            <div class="col-md-4">
                <form:select path="firstValue" cssClass="form-control" data-populate-with-selection="secondValue">
                    <form:options items="${firstValues.options}" itemLabel="label" itemValue="value"/>
                </form:select>
            </div>

            <div class="col-md-4">
                <form:select path="secondValue" cssClass="form-control" data-populate-with-selection="thirdValue">
                    <form:options items="${secondValues.options}" itemLabel="label" itemValue="value"/>
                </form:select>
            </div>

            <div class="col-md-4">
                <form:select path="thirdValue" cssClass="form-control">
                    <form:options items="${thirdValues.options}" itemLabel="label" itemValue="value"/>
                </form:select>
            </div>
        </div>

        <input type="submit" class="btn btn-default"/>
    </form:form>
</section>
<%--<section class="container" style="width: 75%; white-space: nowrap; overflow:hidden; text-overflow: ellipsis;">
    <div style="padding: 1em;">
        ModifyHdivStateParameter= ${modifyHDIVStateParameter} <br/>
        HdivFormStateId= ${hdivFormStateId}
    </div>

    <div style="padding: 1em;">
        <script>
            var hdivFormState = $("form input[type=hidden][name!=_csrf]").last();

            $(function () {
                hdivFormState.on("change", function () {
                    window.console.log("Modificado el hdivFormState...");
                    $("#hdivFormState").text($(this).attr("name"));
                    $("#hdivFormStateValue").text($(this).val());
                });
                $("#hdivFormState").text(hdivFormState.attr("name"));
                $("#hdivFormStateValue").text(hdivFormState.val());
            });
        </script>
        <div>HdivFormState= <span id="hdivFormState"></span></div>
        <div>HdivFormStateValue= <span id="hdivFormStateValue"></span></div>
    </div>
</section>
<section class="container" style="width: 75%; white-space: nowrap; overflow:hidden; text-overflow: ellipsis;">
    <c:forEach items="${firstValues.urls}" var="value">
        ${value.key} : ${value.value}<br/>
    </c:forEach>
</section>--%>
<section id="map" class="container" style="width: 75%; white-space: nowrap; overflow:hidden; text-overflow: ellipsis;">
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

        $(function () {
            $("#map span").text(JSON.stringify(urls));
        });

        function modifyUrl(url, hdivFormStateParameter, modifyHdivFormStateParameter) {
            window.console.log(url + " " + hdivFormStateParameter + " " + modifyHdivFormStateParameter);
//            return url + "&" + modifyHdivFormStateParameter + "=" + $(hdivFormStateParameter).val();
            return url;
        }

        $(function () {
            $("form select").change(function () {
                var selectName = $(this).attr("name");
                var selectedValue = $(this).find("option:selected").val();

                var onSelectPopulateThis = $(this).data("populate-with-selection");

                var url = urls[selectName][selectedValue];

                var hdivFormStateParameter= $("form input[type=hidden][name!=_csrf]").last();
                var modifyHdivFormStateParameter= "${modifyHDIVStateParameter}";


                $.getJSON(modifyUrl(url, hdivFormStateParameter, modifyHdivFormStateParameter), function (data) {

                    var targetSelector = $("#" + onSelectPopulateThis).empty();
                    var newHdivFormState = data.csrf;

                    $(hdivFormStateParameter).val(newHdivFormState);

                    $.each(data.options, function (index, option) {
                        targetSelector.append("<option value='" + option.value + "'>" + option.label + "</option>");
                    });

                    urls[onSelectPopulateThis]= data.urls;
                });
            });
        })
    </script>
    <span></span>
</section>

</body>
</html>
