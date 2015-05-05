<%@ tag body-content="empty"
        description="Draw a live combo"
        isELIgnored="false"
        pageEncoding="UTF-8" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="selector" required="true" type="es.sandbox.app.web.control.Selector" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>

<form:select path="${selector.path}" cssClass="${cssClass}">
    <c:forEach var="option" items="${selector.options}">
        <form:option value="${option.value}" label="${option.label}" data-value="${option.value}"/>
    </c:forEach>
</form:select>