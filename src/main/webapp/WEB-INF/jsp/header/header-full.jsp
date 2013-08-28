<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder,java.util.Enumeration"%>
<!DOCTYPE html>
<html>

<head>

<%
  // title can be specified in a param when you include this jsp. 
  if (request.getParameter("title")!=null) {
%>
  <title><%=request.getParameter("title")%></title>
<% } else { %>
  <title>Cambridge Digital Library - University of Cambridge</title>
<% } %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<jsp:include page="includes.jsp" />

<script src="/scripts/glow/1.7.7/core/core.js" type="text/javascript"></script>
<script src="/scripts/glow/1.7.7/widgets/widgets.js"
	type="text/javascript"></script>
<link rel="stylesheet" href="/scripts/glow/1.7.7/widgets/widgets.css">
<link rel="stylesheet" href="/styles/hint.min.css">

<link href="/styles/treestyler.css" rel="stylesheet" type="text/css"
	media="screen" />

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
	<div class="container">

		<!-- cookie notice -->
		<div id="cookienotice" class="grid_20">
			<div class="grid_16">By continuing to use the site you agree to the use of cookies. You can find out more in our <a href="/help#cookies" onclick="cudl.acceptCookies();return true;">help section</a>.</div>
			<form class="grid_3 right">
				<input class="right" type="submit" value="Accept Cookies" onclick="return cudl.acceptCookies();"/>
			</form>
		</div>
		<div class="clear"></div>
        <!-- end of cookie notice -->

		<header id="globalMasthead" class="grid_20">
			<a id="identifier" class="grid_4 alpha" href="http://www.cam.ac.uk"
				title="University of Cambridge"> University of Cambridge </a>
				
			<a id="libraryLogo" class="grid_4 alpha"
				title="Cambridge University Library" href="http://www.lib.cam.ac.uk"></a>
		</header>

		<!-- end #globalMasthead -->
		<div class="clear"></div>

		<header id="localMasthead" class="grid_16">
			<hgroup>

				<h1>
					<a href="/" title="Cambridge Digital Library ">Cambridge
						Digital Library</a>
				</h1>

			</hgroup>
		</header>
		<div class="grid_4 helpBanner">
		  <%  if (request.getParameter("loggedin")!=null && 
				  request.getParameter("loggedin").equals("true")) { %>
			<a href="/auth/logout/" title="Logout"><img
				src="/images/header/feedback-arrow.png" alt="arrow" /><font color="#3333FF"> Logout </font></a> <br />
				<% } else { %>	  
			<a href="/auth/login/" title="Login"><img
				src="/images/header/feedback-arrow.png" alt="arrow" /><font color="#3333FF"> Login </font></a> <br />
				<% } %>
			<a class="iframe" href="/feedbackform.html" title="Feedback"><img
				src="/images/header/feedback-arrow.png" alt="arrow" /> Feedback</a> <br />
			<a class="iframe" href="/mailinglistform.html"
				title="Keep me informed"><img
				src="/images/header/feedback-arrow.png" alt="arrow" /> Keep me
				informed</a><br />
		</div>
		<!-- end #localMasthead -->