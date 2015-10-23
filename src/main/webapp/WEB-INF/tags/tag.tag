<%@tag description="HTML tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="name" required="true" type="java.lang.String" %>
<%@attribute name="attributes" required="false" fragment="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="selfClose" required="true" type="java.lang.Boolean" %>

<c:out value="<${name}" escapeXml="false"/>
<jsp:invoke fragment="attributes"/>
<c:out value=">" escapeXml="false"/>

<c:if test="${not selfClose}">
	<jsp:doBody/>
	<c:out value="</${name}>" escapeXml="false"/>
</c:if>
