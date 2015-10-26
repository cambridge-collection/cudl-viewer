<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
	<jsp:attribute name="pageData">
		<cudl:default-context>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<json:property name="isAdmin" value="${true}"/>
				<json:array name="editableAreas">
					<json:object>
						<json:property name="id" value="summaryDiv"/>
						<json:property name="filename" value="${collection.summary}"/>
					</json:object>
					<json:object>
						<json:property name="id" value="sponsorDiv"/>
						<json:property name="filename" value="${collection.sponsors}"/>
					</json:object>
				</json:array>
			</sec:authorize>
		</cudl:default-context>
	</jsp:attribute>

	<jsp:body>
		<cudl:nav activeMenuIndex="${1}" displaySearch="true" subtitle="${collection.title}"/>

		<div class="campl-row campl-content campl-recessed-content">
			<div class="campl-wrap clearfix">

				<!-- side nav -->
				<div class="campl-column3">
					<div class="campl-tertiary-navigation">
						<div class="campl-tertiary-navigation-structure">
							<ul class="campl-unstyled-list campl-vertical-breadcrumb">
								<li><a href="/">Cambridge Digital Library<span
										class="campl-vertical-breadcrumb-indicator"></span></a></li>
							</ul>
							<ul class="campl-unstyled-list campl-vertical-breadcrumb-navigation">
								<li class="campl-selected"><a href="${fn:escapeXml(collection.URL)}"><c:out value="${collection.title}"/></a>
									<ul class='campl-unstyled-list campl-vertical-breadcrumb-children'>

										<c:forEach items="${subCollections}" var="c">
											<li><a href="${fn:escapeXml(c.URL)}"><c:out value="${c.title}"/></a></li>
										</c:forEach>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</div>

				<div id="summaryDiv">
					<c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.summary}"/>
				</div>

				<div id="sponsorDiv" class="campl-column12 campl-content-container">
					<c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.sponsors}"/>
				</div>
			</div>
		</div>
	</jsp:body>
</cudl:generic-page>
