<%@tag description="The base for the majority of CUDL pages" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="pagetype" required="false" type="java.lang.String" %>
<%@attribute name="bodyAttrs" required="false" fragment="true" %>

<%-- A string containing JSON encoded data. It should start with '{' or '['. --%>
<%@attribute name="pageData" required="false" type="java.lang.String" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:base-page title="${title}" pagetype="${pagetype}" bodyAttrs="${bodyAttrs}"
				pageData="${pageData}">
	<cudl:ie-div>
		<cudl:projectlight-header/>

		<%-- Pass through body --%>
		<jsp:doBody/>

		<cudl:cookie-notice/>
		<cudl:footer/>
		<cudl:projectlight-footer/>
	</cudl:ie-div>
</cudl:base-page>
