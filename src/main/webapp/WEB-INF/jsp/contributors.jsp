<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
	<jsp:attribute name="pageData">
		<json:object>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<json:property name="isAdmin" value="${true}"/>
				<json:array name="editableAreas">
					<json:object>
						<json:property name="id" value="contributorsDiv"/>
						<json:property name="filename" value="contributors.html"/>
					</json:object>
				</json:array>
			</sec:authorize>
		</json:object>
	</jsp:attribute>

	<jsp:body>
		<cudl:nav activeMenuIndex="${4}" displaySearch="true"
				  subtitle="Contributors to the Cambridge Digital Library"/>

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
								<li class="campl-selected"><a href="/about/">About</a>
									<ul class='campl-unstyled-list campl-vertical-breadcrumb-children'>
										<li><a href="/about/">Introducing the Cambridge Digital
												Library</a></li>
										<li><a href="/news/">News</a></li>
										<li><a href="/contributors/">Contributors</a></li>
										<li><a href="/terms/">Terms & Conditions</a></li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</div>

				<div class="campl-column8  campl-main-content" id="content">
					<div class="campl-content-container">
						<div id="contributorsDiv">
							<c:import charEncoding="UTF-8" url="/html/contributors.html" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</cudl:generic-page>
