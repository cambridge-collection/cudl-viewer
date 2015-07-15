<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	int activeIndex = Integer.valueOf(request.getParameter("activeMenuIndex"));
	boolean displaySearch = request.getParameter("displaySearch").toString().equals("true");
	String title = request.getParameter("title");
	String subtitle = request.getParameter("subtitle");
%>

<div class="campl-row campl-page-header campl-section-page">
	<div class="campl-wrap clearfix">
		<div class="campl-column12">
			<div class="campl-content-container">
				<div class="campl-breadcrumb" id="breadcrumb">
					<ul class="campl-unstyled-list campl-horizontal-navigation clearfix">
						<li class='first-child'><a href="http://cam.ac.uk" class="campl-home ir">Home</a></li>
						<li><a href="http://www.lib.cam.ac.uk">University Library</a></li>

						<%
							if (title != null) {
						%>
						<li><a href="/">Cambridge Digital Library</a></li>
						<li>
							<p class="campl-current"><%=title%></p>
						</li>
						<%
							} else {
						%>
						<li>
							<p class="campl-current">Cambridge Digital Library</p>
						</li>
						<%
							}
						%>
					</ul>
				</div>
				<h1 class="campl-page-title">Cambridge Digital Library</h1>

				<p class="campl-mobile-parent">
					<a href=""><span class="campl-back-btn campl-menu-indicator"></span>Department A-Z</a>
				</p>
			</div>
		</div>
	</div>
</div>

<div class="campl-row campl-page-header">
	<div class="campl-wrap clearfix campl-local-navigation" id="local-nav">
		<div class="campl-local-navigation-container">
			<ul class="campl-unstyled-list">
				<li class="first"><a href="/" title="Home" <%if (activeIndex == 0) {%> class="campl-selected" <%}%>> Home </a></li>
				<li><a href="/collections/" title="Browse" <%if (activeIndex == 1) {%> class="campl-selected" <%}%>> Browse </a></li>
				<li><a href="/search" title="Search" <%if (activeIndex == 2) {%> class="campl-selected" <%}%>> Search </a>
					<ul class="campl-unstyled-list local-dropdown-menu">
						<li><a href="/search/advanced/query">Advanced Search</a></li>
					</ul>
				</li>
				<li><a href="/mylibrary/" title="My Library" <%if (activeIndex == 3) {%> class="campl-selected" <%}%>> My Library </a></li>
				<li><a href="/about/" title="About" <%if (activeIndex == 4) {%> class="campl-selected" <%}%>> About </a>
					<ul class="campl-unstyled-list local-dropdown-menu">
						<li><a href="/about/">Introducing the Cambridge Digital Library</a></li>
						<li><a href="/news/">News</a></li>
						<li><a href="/contributors/">Contributors</a>
						<li><a href="/terms/">Terms and Conditions</a></li>
					</ul>
				</li>
				<li><a href="/help/" title="Help" <%if (activeIndex == 5) {%> class="campl-selected" <%}%>> Help </a></li>
			</ul>
		</div>
	</div>

	<%
		if (title != null) {
	%>
	<div class="campl-wrap clearfix campl-page-sub-title campl-recessed-sub-title">
		<div class="campl-column12">
			<div class="campl-content-container">
				<p class="campl-sub-title"><%=title%></p>
			</div>
		</div>
	</div>
	<%
		}

		else if (subtitle != null) {
	%>

	<div class="campl-wrap clearfix campl-page-sub-title campl-recessed-sub-title">
		<div class="campl-column3 campl-spacing-column">&nbsp;</div>
		<div class="campl-column9">
			<div class="campl-content-container">
				<h1 class="campl-sub-title"><%=subtitle%></h1>
			</div>
		</div>

		<%
			}
		%>
	</div>

</div>
<!--  End of Local Header -->

