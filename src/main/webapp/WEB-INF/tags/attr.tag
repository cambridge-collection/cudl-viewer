<%@tag description="HTML attribute" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="name" required="true" type="java.lang.String" %>
<%@attribute name="value" required="false" type="java.lang.String" %>
<%@attribute name="skipEmpty" required="false" type="java.lang.Boolean" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${empty value}">
	<c:set var="value"><jsp:doBody/></c:set>
</c:if>

<c:if test="${not (skipEmpty and empty value)}">
	<c:out value=" ${name}" escapeXml="false"/>
	<c:if test="${not empty value}">
		<c:out value="=\"${fn:escapeXml(value)}\"" escapeXml="false"/>
	</c:if>
</c:if>
