<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="name" required="true" type="java.lang.String" %>
<%@attribute name="cssClass" required="false" type="java.lang.String" %>
<%@attribute name="id" required="false" type="java.lang.String" %>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:eval expression="@uiThemeBean.themeUI" var="themeUI" />

<c:if test="${fn:length(name) gt 0}">
    <c:set var = "img" value = "${themeUI.getImage(name)}"/>
    <c:choose>
        <c:when test="${fn:length(img.href) gt 0}">
            <a href="${img.href}">
                <img alt="${img.alt}" src="${img.src}" class="${cssClass}" id="${id}"/>
            </a>
        </c:when>
        <c:otherwise>
            <img alt="${img.alt}" src="${img.src}" class="${cssClass}" id="${id}"/>
        </c:otherwise>
    </c:choose>
</c:if>
