<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<jsp:include page="../header/header-full.jsp" />
<jsp:include page="../header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="0" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Error" />
</jsp:include>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">
			<div class="campl-content-container">

				<div class="panel light">

					<div class="grid_11">

						<h2>Error occurred.</h2>
						<p>
							Sorry, there has been an error displaying the page you requested.
							Please <a href="mailto:dl-support@lib.cam.ac.uk"
								title="500 Page - Error">Contact Us</a> so we can resolve the
							issue or click on the link below to go to our home page. <br />
							<br /> <a href="/">Cambridge Digital Library</a>.
						</p>

						<p style="color: #999999">
							Message:<a style="color: #999999" href="javascript:;"
								onmousedown="cudl.toggleDiv('errordiv');"> <%
 	if (exception!=null) { out.print(exception.toString()); }
 	else { 
 		String error = request.getAttribute("javax.servlet.error.message").toString();
 		if (error!=null) { out.println(error); }
 	}
	
 %>
							</a>

						</p>

						<div id="errordiv" style="color: #999999;">
							<% if (exception!=null) {
								StackTraceElement[] sts = exception.getStackTrace();
								for (int i = 0; i < sts.length; i++) {
									out.print(sts[i].toString() + "<br/>");
								}
							   }
							%> 
						</div>
					</div>
				</div>

			</div>


			<script language="javascript">
				// default to hide error if JS enabled. 
				cudl.toggleDiv('errordiv');
			</script>

		</div>
	</div>
</div>
<jsp:include page="../header/footer-full.jsp" />




