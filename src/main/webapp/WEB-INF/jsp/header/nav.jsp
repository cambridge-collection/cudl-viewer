<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>      
<%
	int activeIndex = Integer.valueOf(request.getParameter("activeMenuIndex"));
	boolean displaySearch = request.getParameter("displaySearch").toString().equals("true");
	String title = request.getParameter("title");
	String subtitle = request.getParameter("subtitle");
%>

<style>
 	.cudl-header-search {
 		position: absolute;
 		right: -20px;
 		bottom: 0;
 		width: 40%;
 	}
 	.campl-search-input {
 		height: 32px;
 		min-height: 32px;
 	}
 	.cudl-header-search input[type=text] {
 		height: 32px;
 		width: calc(100% - 31px);
 	}
 	form.campl-search-input input.campl-search-submit {
 		height: 32px;
 		width: 32px;
 	}
 	div.campl-site-search {
		display: none;
	}
	@media screen and (max-width: 767px) {
		div.campl-site-search {
			display: block;
		}
		.cudl-header-search {
			display: none;
		}
		#site-search-container input[type=text] {
			margin: 0;
			padding: 0;
			height: 32px;
			line-height: 32px;
		}
	}
</style>

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
				
				<div style="position:relative;">
					<h1 class="campl-page-title">Cambridge Digital Library</h1>
					
					<% if (activeIndex == 0) { %>
					
					<div class="campl-column6 cudl-header-search">
						<form action="/search" class="campl-search-input">
							<input name="keyword" type="text" class="text" placeholder="Search" />
							<input type="image" class="campl-search-submit " src="/images/interface/btn-search-inpage.png">
						</form>
					</div>
					
					<% } %>
				</div>

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

                <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li><a style="background:#ff4444" href="/admin/" title="Admin"> Admin </a></li>
                </sec:authorize>

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

