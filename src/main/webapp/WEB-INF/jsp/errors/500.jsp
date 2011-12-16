<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true"%>

<jsp:include page="../header/header-full.jsp" />
<jsp:include page="../header/nav-home.jsp" />

		<div class="clear"></div>

		<section id="content" class="grid_20 content">

			<div class="grid_20" style="margin-bottom: 18px;">

				<div class="panel light">

					<div class="grid_11">

						<h4>Error occurred.</h4>
						<p>
							Sorry, there has been an error displaying the page you requested.
							Please <a href="mailto:foundations@lib.cam.ac.uk"
								title="500 Page - Error">Contact Us</a> so we can
							resolve the issue or click on the link below to go to our home
							page. <br /> <br /> <a href="/">Cambridge Digital Library</a>.
						</p>
						
						<p style="color: #999999">
							Message:<a style="color: #999999" href="javascript:;"
								onmousedown="toggleDiv('errordiv');">
								<%out.print(exception.toString());%>
							</a>

						</p>

						<div id="errordiv" style="color: #999999;">
							<%
							StackTraceElement[] sts = exception.getStackTrace(); 
							for (int i=0; i<sts.length; i++) {
								out.print(sts[i].toString()+"<br/>");
							}%>
						</div>
					</div>
				</div>

			</div>



		</section>

<script language="javascript">
	// default to hide error if JS enabled. 
	toggleDiv('errordiv');
</script>

<jsp:include page="../footer/footer.jsp" />




