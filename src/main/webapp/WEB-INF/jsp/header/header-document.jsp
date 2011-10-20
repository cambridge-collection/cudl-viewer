<%@ page import="java.net.URLEncoder"%>
<html>
<head>
<%
	String requestURL = request.getAttribute("requestURL").toString();
	String encodedRequestURL = URLEncoder.encode(requestURL, "UTF-8");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<NOSCRIPT>
	<!--  no javascript redirect. -->
	<META HTTP-EQUIV="refresh"
		content="0;URL=/nojavascript?url=<%=encodedRequestURL%>">
</NOSCRIPT>

<title>Cambridge Digital Library - University of Cambridge</title>

<link rel="icon" type="image/png" href="/img/favicon.png">
<link rel="stylesheet" href="/styles/uoc.min.css">
<!--[if lt IE 9]>
   <link rel="stylesheet" href="/styles/ie.min.css"/>
   <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>   
<![endif]-->

<link rel="stylesheet" type="text/css"
	href="/scripts/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/styles/style.css" />
<link rel="stylesheet" type="text/css" href="/styles/style-document.css" />

<!--[if lt IE 8]>
   <link rel="stylesheet" href="/styles/ie.style.css"/>
<![endif]-->

<style>

h2 {
	font-size: 36px;
}

</style>

<script type="text/javascript">
	var JSONURL = '/json/${docId}.json';
	var pagenum = ${page};
	var docId = '${docId}';
	var docURL = '${docURL}';
</script>

<script type="text/javascript" src="/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="/scripts/cudl-util.js"></script>
<script type="text/javascript" src="/scripts/docData.js"></script>
<script type="text/javascript" src="/scripts/docViewport.js"></script>
<script type="text/javascript" src="/scripts/docView.js"></script>
<script type="text/javascript" src="/scripts/seadragon-min.js"></script>
<script type="text/javascript" src="/scripts/document.js"></script>

</head>
<body>
	<div class="container" id="north">

		<div class="clear"></div>

		<header id="localMasthead"> <hgroup> <a id="UoCLogo"
			href="http://www.cam.ac.uk"><img title="University of Cambridge" alt="University of Cambridge"
			align="left" src="/images/header/logo.gif" /></a>
		<h2>
			|&nbsp; <a href="/" title="Cambridge Digital Library ">Cambridge
				Digital Library</a>
		</h2>
		<a href="http://www.lib.cam.ac.uk"><img title="Cambridge University Library" alt="Cambridge University Library" align="right" src="/images/header/ul-small.gif" /></a> 
			
			</hgroup> </header>
		<!-- end #localMasthead -->