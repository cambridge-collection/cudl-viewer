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

<!-- Simple OpenID Selector -->
<link type="text/css" rel="stylesheet" href="/styles/openid.css" />
<script type="text/javascript" src="/scripts/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="/scripts/openid/openid-jquery.js"></script>
<script type="text/javascript" src="/scripts/openid/openid-en.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			openid.init('openid_identifier');
			//openid.setDemoMode(true); //Stops form submission for client javascript-only test purposes
			});
</script>

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
			<!-- 
                <nav>
                    <ul class="grid_10">
                        <li class="first">
                            <a href="http://www.cam.ac.uk/research" title="Research">
                             p   Research
                            </a>
                        </li>
                        <li>
                            <a href="http://www.cam.ac.uk/admissions" title="Study">
                                Study
                            </a>
                        </li>
                        <li>
                            <a href="http://www.cam.ac.uk/news" title="News">
                                News
                            </a>
                        </li>
                        <li>
                            <a href="http://www.cam.ac.uk/univ/" title="About">
                                About
                            </a>
                        </li>
                        <li>
                            <a href="http://www.cam.ac.uk/global/contact.html" title="Contact">
                                Contact
                            </a>
                        </li>
                        <li class="last">
                            <a href="http://www.cam.ac.uk/global/az.html" title="A&ndash;Z">
                                A&ndash;Z
                            </a>
                        </li>                                                                
                    </ul>
                </nav>-->
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

		<!-- end #localMasthead -->
