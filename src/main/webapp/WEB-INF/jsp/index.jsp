<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="pagetype" value="STANDARD"/>
<c:set var="metaDescription">
    A home for the discovery of digitised material and research
    outputs from the University of Cambridge and beyond
</c:set>

<cudl:generic-page pagetype="${pagetype}" title="${collection.title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="indexDiv" filename="index.html"/>
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

        <div id="indexDiv">
            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/index.html" />
        </div>
    </jsp:body>
</cudl:generic-page>
