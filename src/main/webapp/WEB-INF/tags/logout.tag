<%@tag description="Logout button" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<%@attribute name="label" required="false" type="java.lang.String"  %>
<%@attribute name="nexturl" required="false" type="java.lang.String" %>

<form:form method="POST" action="/auth/logout${empty nexturl ? '' : '?next='.concat(cudlfn:uriEnc(nexturl))}">
    <button class="campl-primary-cta" action="submit"><c:out value="${empty label ? 'Logout' : label}"/></button>
</form:form>
