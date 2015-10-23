<%@tag description="CUDL base page" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="pagetype" required="true" type="java.lang.String" %>
<%@attribute name="bodyAttrs" required="false" fragment="true" %>

<%-- A string containing JSON encoded data. It should start with '{' or '['. --%>
<%@attribute name="pageData" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<c:set var="title" value="${(empty title) ? 'Cambridge Digital Library - University of Cambridge' : title}"/>

<cudl:html title="${title}">
	<jsp:attribute name="head">
		<cudl:head-content pagetype="${pagetype}"/>
	</jsp:attribute>

	<jsp:attribute name="bodyAttrs">
		<cudl:attr name="data-context" value="${pageData}" skipEmpty="${true}"/>
		<jsp:invoke fragment="bodyAttrs"/>
	</jsp:attribute>

	<jsp:body>
		<cudl:ie-div>
			<cudl:projectlight-header/>

			<%-- Pass through body --%>
			<jsp:doBody/>

			<cudl:cookie-notice/>
			<cudl:footer/>
			<cudl:projectlight-footer/>
		</cudl:ie-div>
	</jsp:body>
</cudl:html>
