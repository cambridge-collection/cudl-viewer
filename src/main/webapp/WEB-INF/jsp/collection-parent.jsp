<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.Iterator,ulcambridge.foundations.viewer.ItemFactory"%>
	

<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>
	
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="subtitle" value="<%=collection.getTitle()%>" />
</jsp:include>

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
					<ul
						class="campl-unstyled-list campl-vertical-breadcrumb-navigation">
						<li class="campl-selected"><a href="/collections/longitude">Board of Longitude</a>
							<ul
								class='campl-unstyled-list campl-vertical-breadcrumb-children'>
								<li><a href="/about/">Papers of the Board of Longitude</a></li>
								<li><a href="/news/">Papers of Nevil Maskelyne</a></li>
								<li><a href="/contributors/">Contributors</a></li>
								<li><a href="/terms/">Terms & Conditions</a></li>
							</ul></li>

					</ul>
				</div>
			</div>
		</div>


		<div class="campl-column6  campl-main-content" id="content">
			<div class="campl-content-container news_items">
				<h2>Latest Updates</h2>

	<jsp:include page="<%=collection.getSummary()%>" />

	<jsp:include page="<%=collection.getSponsors()%>" />


<jsp:include page="header/footer-full.jsp" />













<jsp:include page="header/header-full.jsp" >
	<jsp:param name="title" value="<%=collection.getTitle()%>" />
</jsp:include>	
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>
<jsp:include page="header/nav-browse-submenu.jsp" />

<div class="clear"></div>

<section id="content" class="grid_20 content">

	<jsp:include page="<%=collection.getSummary()%>" />

	<jsp:include page="<%=collection.getSponsors()%>" />
</section>

<jsp:include page="footer/footer.jsp" />



