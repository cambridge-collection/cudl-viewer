<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('metaDescription')" var="metaDescription" />
<c:set var="pagetype" value="STANDARD"/>
<c:set var="metaDescription" value="${metaDescription}"/>

<cudl:generic-page pagetype="${pagetype}" title="${collection.title}">

    <jsp:attribute name="head">
        <cudl:head-content pagetype="${pagetype}" metaDescription="${metaDescription}"/>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${0}" displaySearch="true"/>

        <c:if test="${not empty downtimeWarning}">
            <c:out value="${downtimeWarning}" escapeXml="false"/>
        </c:if>

        <div id="main_content">
            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/index.html" />
        </div>
    </jsp:body>
</cudl:generic-page>
