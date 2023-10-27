<%@tag description="The simplest possible CUDL page. No body markup is provided, but UI assets are loaded."
       pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="head" required="false" fragment="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="pagetype" required="false" type="java.lang.String" %>
<%@attribute name="bodyAttrs" required="false" fragment="true" %>

<%-- A string containing JSON encoded data. It should start with '{' or '['. --%>
<%@attribute name="pageData" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('default.title')" var="defaultTitle" />
<c:set var="defaultTitle" value="${defaultTitle}"/>


<c:set var="title" value="${(empty title) ? defaultTitle : title}"/>

<c:if test="${empty pageData}">
    <c:set var="pageData">
        <cudl:default-context/>
    </c:set>
</c:if>

<c:if test="${empty pagetype}">
    <c:set var="pagetype" value="STANDARD"/>
</c:if>

<cudl:html title="${title}">
    <jsp:attribute name="head">
        <c:choose>
            <c:when test="${empty head}">
                <cudl:head-content pagetype="${pagetype}"/>
            </c:when>
            <c:otherwise>
                <jsp:invoke fragment="head"/>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="bodyAttrs">
        <cudl:attr name="data-context" value="${pageData}" skipEmpty="${true}"/>
        <jsp:invoke fragment="bodyAttrs"/>
    </jsp:attribute>

    <jsp:body>
        <%-- Pass through body --%>
        <jsp:doBody/>
    </jsp:body>
</cudl:html>
