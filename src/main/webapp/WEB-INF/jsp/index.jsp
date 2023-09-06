<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<!--Links to a temporary directory created just for testing -->

<link type="text/css" href="${pageContext.request.contextPath}/bootstrap5-test-css/bootstrap.min.css" rel="stylesheet">
<link  type="text/css" href="${pageContext.request.contextPath}/bootstrap5-test-css/all%20collections-test.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap5-test-css/bootstrap.bundle.min.js"></script>

<c:set var="pagetype" value="STANDARD"/>
<c:set var="metaDescription">
    A home for the discovery of digitised material and research
    outputs from the University of Cambridge and beyond
</c:set>

<cudl:generic-page pagetype="${pagetype}" title="${collection.title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="main_content" filename="index.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

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
