<%@tag description="The CUDL crumbtrail" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="subtitle" required="false" type="java.lang.String" %>
<%@attribute name="collection" required="false" type="java.lang.Object" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="defaultTitle" value="Cambridge Digital Library"/>

<c:set var = "uriArray" value = "${fn:split(requestScope['javax.servlet.forward.request_uri'],'/,')}"/>
<c:if test="${fn:length(uriArray) gt 0}">
    <ul class="container crumbtrail">
        <li><a href="/"><i class="fa fa-home" title="Home" aria-hidden="true"></i><span class="sr-only">Home</span></a></li>
        <c:choose>
            <c:when test="${fn:length(uriArray) == 1 or fn:length(uriArray) > 1 && uriArray[0] != 'collections'}">
                <li>${(not empty title) ? title : subtitle}</li>
            </c:when>
            <c:when test="${fn:length(uriArray) > 1 && uriArray[0] == 'collections'}">
                <li><a href="/${uriArray[0]}/">${(not empty title) ? title : subtitle}</a></li>
                <c:if test="${fn:length(collection.parentCollectionId) > 0}">
                    <c:set var="parentCollection" value="${cudlfn:getCollection(collectionFactory, collection.parentCollectionId)}"/>
                    <li><a href="${fn:escapeXml(parentCollection.URL)}">
                        <c:out value="${parentCollection.title}"/></a></li>
                </c:if>
                <c:if test="${not empty collection}">
                    <li>${collection.title}</li>
                </c:if>
            </c:when>
            <c:otherwise/>
        </c:choose>
    </ul>
</c:if>
