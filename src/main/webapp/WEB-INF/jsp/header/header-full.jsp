<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder,java.util.Enumeration"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en"
	class="no-js">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<!--  webmaster tools -->
<meta name="google-site-verification"
	content="asuuZDOIgJTwOZ0EfnrFe1hQ2A7q6uukh5ebEsciiNg" />
<meta name="msvalidate.01" content="EF9291052C3CCD9E1AC12147B69C52C2" />

<%
	// title can be specified in a param when you include this jsp. 
	if (request.getParameter("title") != null) {
%>
<title><%=request.getParameter("title")%></title>
<%
	} else {
%>
<title>Cambridge Digital Library - University of Cambridge</title>
<%
	}
%>

<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<!-- load Project Light Font -->
<script type="text/javascript" src="//use.typekit.com/hyb5bko.js"></script>
<script type="text/javascript">
	try {
		Typekit.load();
	} catch (e) {
	}
</script>

<!-- JQuery -->
<script type="text/javascript" src="/scripts/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/scripts/spin.min.js"></script>

<!--  bootstrap -->
<link rel="stylesheet" href="/styles/bootstrap-default.min.css">
<script src="/scripts/bootstrap.min.js"></script>

<!--  Project Light  -->
<link rel="stylesheet" href="/styles/projectlight-full-stylesheet.css" />
<script type="text/javascript"	src="/scripts/projectlight-libs/ios-orientationchange-fix.js"></script>
<script type="text/javascript"	src="/scripts/projectlight-libs/modernizr.js"></script>
<script type="text/javascript"	src="/scripts/projectlight-libs/jquery-min.js"></script>
<script type="text/javascript" src="/scripts/projectlight-custom.js"></script>

<!-- Fancybox -->
<link rel="stylesheet" href="/scripts/fancybox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript" src="/scripts/fancybox/jquery.fancybox.pack.js"></script>

<!-- Pagination -->
<script type="text/javascript" src="/scripts/jquery.paging.min.js"></script>

<!-- CUDL -->
<link rel="stylesheet" href="/styles/style.css" type="text/css" media="screen" />
<script type="text/javascript" src="/scripts/cudl.js"></script>

<script type="text/javascript">
	// initalise page if function is available. 
	function init() {
		
		// Show or hide cookie notice and call page specific init statement.
		cudl.displayCookieNotice();
		if (typeof pageinit == 'function') {
			pageinit();
		}
	}
</script>

</head>
<body onload="init()">


<!--[if lt IE 7]>
<div class="lt-ie9 lt-ie8 lt-ie7">
<![endif]-->
	<!--[if IE 7]>
<div class="lt-ie9 lt-ie8">
<![endif]-->
	<!--[if IE 8]>
<div class="lt-ie9">
<![endif]-->

	<a href="#primary-nav" class="campl-skipTo">skip to primary
		navigation</a>
	<a href="#content" class="campl-skipTo">skip to content</a>

	<div class="campl-row campl-global-header">
		<div class="campl-wrap clearfix">
			<div class="campl-header-container campl-column8"
				id="global-header-controls">
				<a href="http://www.cam.ac.uk" class="campl-main-logo"> <img
					alt="University of Cambridge"
					src="/img/interface/main-logo-small.png" />
				</a>

				<ul
					class="campl-unstyled-list campl-horizontal-navigation campl-global-navigation clearfix">
					<li><a href="#study-with-us">Study at Cambridge</a></li>
					<li><a href="#about-the-university">About the University</a></li>
					<li><a href="http://www.cam.ac.uk/research"
						class="campl-no-drawer">Research at Cambridge</a></li>
				</ul>
			</div>

			<div class="campl-column2" style="float:right;">
				<div class="campl-quicklinks"></div>
			</div>

			<div class="campl-column2">

				<div class="campl-site-search" id="site-search-btn">

					<label for="header-search" class="hidden">Search site</label>
					<div class="campl-search-input">
						<form action="/search" method="get">
							<input id="header-search" type="text" name="keyword" value=""
								placeholder="Search" /> <input type="image"
								class="campl-search-submit "
								src="/img/interface/btn-search-header.png" />
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="campl-row campl-global-header campl-search-drawer">
		<div class="campl-wrap clearfix">
			<form class="campl-site-search-form" id="site-search-container"
				action="/search">
				<div class="campl-search-form-wrapper clearfix">
					<input name="keyword" type="text" class="text" placeholder="Search" /> <input
						type="image" class="campl-search-submit"
						src="/img/interface/btn-search.png" />
				</div>
			</form>
		</div>
	</div>

	<div class="campl-row campl-global-navigation-drawer">

		<div class="campl-wrap clearfix">
			<div class="campl-column12 campl-home-link-container">
				<a href="">Home</a>
			</div>
		</div>
		<div class="campl-wrap clearfix">
			<div
				class="campl-column12 campl-global-navigation-mobile-list campl-global-navigation-list">
				<div class="campl-global-navigation-outer clearfix"
					id="study-with-us">
					<ul
						class="campl-unstyled-list campl-global-navigation-header-container ">
						<li><a href="http://www.cam.ac.uk/study-at-cambridge">Study
								at Cambridge</a></li>
					</ul>
					<div class="campl-column4">
						<ul
							class="campl-global-navigation-container campl-unstyled-list campl-global-navigation-secondary-with-children">
							<li><a href="http://www.study.cam.ac.uk/undergraduate/">Undergraduate</a>
								<ul class="campl-global-navigation-tertiary campl-unstyled-list">
									<li><a
										href="http://www.study.cam.ac.uk/undergraduate/courses/">Courses</a>
									</li>
									<li><a
										href="http://www.study.cam.ac.uk/undergraduate/apply/">Applying</a>
									</li>
									<li><a
										href="http://www.study.cam.ac.uk/undergraduate/events/">Events
											and open days</a></li>
									<li><a
										href="http://www.study.cam.ac.uk/undergraduate/finance/">Fees
											and finance</a></li>
									<li><a href="http://www.becambridge.com/">Student
											blogs and videos</a></li>
								</ul></li>
						</ul>
					</div>
					<div class="campl-column4">
						<ul
							class="campl-global-navigation-container campl-unstyled-list campl-global-navigation-secondary-with-children">
							<li><a href="http://www.graduate.study.cam.ac.uk/">Graduate</a>
								<ul class="campl-global-navigation-tertiary campl-unstyled-list">
									<li><a
										href="http://www.admin.cam.ac.uk/students/gradadmissions/prospec/whycam/">Why
											Cambridge</a></li>
									<li><a href="http://www.graduate.study.cam.ac.uk/courses
">Course
											directory</a></li>
									<li><a
										href="http://www.graduate.study.cam.ac.uk/how-do-i-apply">How
											to apply</a></li>
									<li><a
										href="http://www.admin.cam.ac.uk/students/studentregistry/fees/">Fees
											and funding</a></li>
									<li><a
										href="http://www.graduate.study.cam.ac.uk/faqs/applicant">Frequently
											asked questions</a></li>
								</ul></li>
						</ul>
					</div>
					<div class="campl-column4">
						<ul
							class="campl-global-navigation-container campl-unstyled-list last">
							<li><a href="http://www.internationalstudents.cam.ac.uk">International
									students</a></li>
							<li><a href="http://www.ice.cam.ac.uk">Continuing
									education</a></li>
							<li><a
								href="http://www.admin.cam.ac.uk/offices/education/epe/">Executive
									and professional education</a></li>
							<li><a href="http://www.educ.cam.ac.uk">Courses in
									education</a></li>
						</ul>
					</div>
				</div>

				<div class="campl-global-navigation-outer clearfix"
					id="about-the-university">
					<ul
						class="campl-global-navigation-header-container campl-unstyled-list">
						<li><a href="http://www.cam.ac.uk/about-the-university">About
								the University</a></li>
					</ul>
					<div class="campl-column4">
						<ul class="campl-global-navigation-container campl-unstyled-list">
							<li><a
								href="http://www.cam.ac.uk/about-the-university/how-the-university-and-colleges-work">How
									the University and Colleges work</a></li>
							<li><a
								href="http://www.cam.ac.uk/about-the-university/history">History</a>
							</li>
							<li><a
								href="http://www.cam.ac.uk/about-the-university/visiting-the-university">Visiting
									the University</a></li>
							<li><a
								href="http://www.cam.ac.uk/about-the-university/term-dates-and-calendars">Term
									dates and calendars</a></li>
							<li class="last"><a href="http://map.cam.ac.uk">Map</a></li>
						</ul>
					</div>
					<div class="campl-column4">
						<ul class="campl-global-navigation-container campl-unstyled-list">
							<li><a href="http://www.cam.ac.uk/for-media">For media</a></li>
							<li><a href="http://www.cam.ac.uk/video-and-audio">Video
									and audio</a></li>
							<li><a
								href="http://webservices.admin.cam.ac.uk/faesearch/map.cgi">Find
									an expert</a></li>
							<li><a
								href="http://www.cam.ac.uk/about-the-university/publications">Publications</a>
							</li>
							<li class="last"><a
								href="http://www.cam.ac.uk/global-cambridge">Global
									Cambridge</a></li>
						</ul>
					</div>
					<div class="campl-column4">
						<ul class="campl-global-navigation-container campl-unstyled-list">
							<li><a href="http://www.cam.ac.uk/news">News</a></li>
							<li><a href="http://www.admin.cam.ac.uk/whatson">Events</a>
							</li>
							<li><a href="http://www.cam.ac.uk/public-engagement">Public
									engagement</a></li>
							<li><a href="http://www.jobs.cam.ac.uk">Jobs</a></li>
							<li class="last"><a href="http://www.philanthropy.cam.ac.uk">Giving
									to Cambridge</a></li>
						</ul>
					</div>
				</div>

				<div class="campl-global-navigation-outer clearfix"
					id="our-research">
					<ul
						class="campl-global-navigation-header-container campl-unstyled-list">
						<li><a href="">Research at Cambridge</a></li>
					</ul>
				</div>
			</div>

			<ul
				class="campl-unstyled-list campl-quicklinks-list campl-global-navigation-container ">
				<li><a href="http://www.cam.ac.uk/for-staff">For staff</a></li>
				<li><a href="http://www.admin.cam.ac.uk/students/gateway">For
						current students</a></li>
				<li><a href="http://www.alumni.cam.ac.uk">For alumni</a></li>
				<li><a href="http://www.cam.ac.uk/for-business">For
						business</a></li>
				<li><a href="http://www.cam.ac.uk/colleges-and-departments">Colleges
						&amp; departments</a></li>
				<li><a href="http://www.cam.ac.uk/libraries-and-facilities">Libraries
						&amp; facilities</a></li>
				<li><a href="http://www.cam.ac.uk/museums-and-collections">Museums
						&amp; collections</a></li>
				<li class="last"><a
					href="http://www.cam.ac.uk/email-and-phone-search">Email &amp;
						phone search</a></li>
			</ul>
		</div>
	</div>
	<!-- .campl-global-header ends -->

