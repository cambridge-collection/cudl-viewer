<%@tag description="Generate JSON for CSRF token and header names required to send AJAX POST requests."
       pageEncoding="UTF-8"
       trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<c:if test="${not empty _csrf.token and not empty _csrf.headerName}">
    <json:object name="csrf">
        <json:property name="token" value="${_csrf.token}"/>
        <json:property name="header" value="${_csrf.headerName}"/>
    </json:object>
</c:if>
