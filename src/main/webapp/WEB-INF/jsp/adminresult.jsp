<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Admin" />
</jsp:include>
<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">
			<div class="campl-content-container">

				<div class="alert alert-info" role="alert">
					<h3><%=pageContext.findAttribute("copysuccess")%></h3>
					<p>
						<a class="btn btn-primary" href="/admin/" role="button">Back</a>
					</p>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="header/footer-full.jsp" />