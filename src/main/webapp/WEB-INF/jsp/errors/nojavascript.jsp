<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="../header/header-full.jsp" />
<jsp:include page="../header/nav.jsp" >    
   <jsp:param name="activeMenuIndex" value="0" />
   <jsp:param name="displaySearch" value="true" />
</jsp:include>	

<div class="clear"></div>

<section id="content" class="grid_20 content">

	<div class="grid_20" style="margin-bottom:18px;">

		<div class="panel light">

			<div class="grid_11">
				<h4>
					Javascript required.
				</h4>
				<p>Javascript is required to view this page, please enable javascript and <a href="${requestURL}">try again</a>. </p>
			</div>
		</div>

	</div>
	
	
</section>

<jsp:include page="../footer/footer.jsp" />



