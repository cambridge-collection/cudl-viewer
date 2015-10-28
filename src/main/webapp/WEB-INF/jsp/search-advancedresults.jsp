<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_GB"/>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:search-results-page title="Advanced Search">
	<jsp:attribute name="queryInfo">
		<ul>
			<cudl:search-result-param form="${form}" label="Keyword" attr="keyword"/>
			<cudl:search-result-param form="${form}" label="Full Text" attr="fullText"/>
			<cudl:search-result-param form="${form}" label="Exclude Text" attr="excludeText"/>
			<cudl:search-result-param form="${form}" label="Classmark" attr="shelfLocator"/>
			<cudl:search-result-param form="${form}" label="CUDL ID" attr="fileID"/>
			<cudl:search-result-param form="${form}" label="Title" attr="title"/>
			<cudl:search-result-param form="${form}" label="Subject" attr="subject"/>
			<cudl:search-result-param form="${form}" label="Location" attr="location"/>
			<c:if test="${not (empty form.yearStart or empty form.yearEnd)}">
				<li>
					<span>Year: <b><c:out value="${form.yearStart}"/></b> to <b><c:out value="${form.yearEnd}"/></b></span>
				</li>
			</c:if>
		</ul>

		<form class="grid_5" action="/search">
			<a href="/search/advanced/query?${fn:escapeXml(queryString)}">
				Change Query
			</a>
			<input type="hidden" name="fileID" value="${fn:escapeXml(form.fileID)}>">
		</form>
	</jsp:attribute>
	<jsp:body/>
</cudl:search-results-page>
