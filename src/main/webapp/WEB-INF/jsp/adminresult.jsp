<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD">
	<cudl:nav activeMenuIndex="${-1}" displaySearch="true" title="Admin"/>

	<div class="campl-row campl-content campl-recessed-content">
		<div class="campl-wrap clearfix">
			<div class="campl-column9  campl-main-content" id="content">
				<br />
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title"><c:out value="${copysuccess}"/></h3>
					</div>
					<div class="panel-body">
						<div class="campl-column8">
							<a class="btn btn-default" href="/admin/" role="button">Back</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</cudl:generic-page>
