<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="-1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Admin" />
</jsp:include>
<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">

			<br />
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title"><%=pageContext.findAttribute("copysuccess")%></h3>
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




<jsp:include page="header/footer-full.jsp" />