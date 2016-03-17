<%@tag description="Advanced search parameter value" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="form" required="true" type="ulcambridge.foundations.viewer.forms.SearchForm" %>
<%@attribute name="label" required="true" type="java.lang.String" %>
<%@attribute name="attr" required="true" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${not empty form[attr]}">
    <li>
        <span><c:out value="${label}"/>: <b><c:out value="${form[attr]}"/></b></span>
    </li>
</c:if>
