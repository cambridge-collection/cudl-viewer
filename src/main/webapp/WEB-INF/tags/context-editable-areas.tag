<%@tag description="Generate context data defining editable areas if the user is an admin"
       pageEncoding="UTF-8"
       trimDirectiveWhitespaces="true" %>

<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>


<sec:authorize access="hasRole('ROLE_ADMIN')">
    <json:property name="isAdmin" value="${true}"/>
    <json:array name="editableAreas">
        <jsp:doBody/>
    </json:array>
</sec:authorize>
