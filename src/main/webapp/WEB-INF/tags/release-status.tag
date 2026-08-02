<%@tag description="Unreleased-content badge or notice" pageEncoding="UTF-8"
       trimDirectiveWhitespaces="true" %>

<%@attribute name="type" required="true" type="java.lang.String" %>
<%@attribute name="unreleased" required="true" type="java.lang.Boolean" %>
<%@attribute name="status" required="true" type="java.lang.String" %>
<%@attribute name="subject" required="false" type="java.lang.String" %>
<%@attribute name="style" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<%-- showUnreleasedContent is a request attribute published by the controller of each
     page that renders one of these. It suppresses the indicator, not the content. --%>
<c:if test="${showUnreleasedContent and unreleased}">
    <c:choose>
        <c:when test="${type eq 'badge'}">
            <span class="badge bg-warning text-dark"><c:out value="${cudlfn:capitalise(status)}"/></span>
        </c:when>
        <c:otherwise>
            <div class="alert alert-warning" role="alert"<cudl:attr name="style" value="${style}" skipEmpty="${true}"/>>
                <strong><c:out value="${cudlfn:capitalise(status)}"/>:</strong> This ${subject} is not yet publicly available.
            </div>
        </c:otherwise>
    </c:choose>
</c:if>
