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
						<json:property name="id" value="collectionsDiv"/>
						<json:property name="filename" value="collections/collections.html"/>
					</json:object>
				</json:array>
			</sec:authorize>
		</json:object>
	</jsp:attribute>

	<jsp:body>
		<cudl:nav activeMenuIndex="${1}" displaySearch="true" title="Browse our collections"/>

		<div class="campl-row campl-content campl-recessed-content">
        	<div class="campl-wrap clearfix">
        		<div class="campl-column9  campl-main-content" id="content">
        			<div id="collectionsDiv">
        		        <c:import charEncoding="UTF-8" url="/html/collections/collections.html" />
        			</div>
        		</div>
        	</div>
        </div>
	</jsp:body>
</cudl:generic-page>
