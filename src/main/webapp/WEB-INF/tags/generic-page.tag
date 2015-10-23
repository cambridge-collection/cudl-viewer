<%@tag description="CUDL base page" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="pagetype" required="true" type="java.lang.String" %>

<%-- A string containing JSON encoded data. It should start with '{' or '['. --%>
<%@attribute name="pageData" type="java.lang.String"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<c:set var="title" value="${(empty title) ? 'Cambridge Digital Library - University of Cambridge' : title}"/>

<c:set var="bodyAttrs" value=""/>

<c:if test="${not empty pageData}">
	<c:set var="bodyAttrs" value="${bodyAttrs} ${\"data-context=\\\"\"}${fn:escapeXml(pageData)}${\"\\\"\"}"/>
</c:if>

<cudl:html title="${title}" bodyAttrs="${bodyAttrs}">
	<jsp:attribute name="head">
		<cudl:head-content pagetype="${pagetype}"/>
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
