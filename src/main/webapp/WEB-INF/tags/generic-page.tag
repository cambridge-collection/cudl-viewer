<%@tag description="CUDL base page" pageEncoding="UTF-8"%>
<%@attribute name="title" required="false" type="java.lang.String"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<c:set var="title" value="${(empty title) ? 'Cambridge Digital Library - University of Cambridge' : title}"/>

<cudl:html title="${title}">
	<jsp:attribute name="head">
		<cudl:head-content/>
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
