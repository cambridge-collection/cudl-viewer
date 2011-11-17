<%@ page import="java.net.URLEncoder"%>
<!DOCTYPE html>

<html>
<head>
<%
	String requestURL = request.getAttribute("requestURL").toString();
	String encodedRequestURL = URLEncoder.encode(requestURL, "UTF-8");
%>
<NOSCRIPT>
	<!--  no javascript redirect. -->
	<META HTTP-EQUIV="refresh"
		content="0; URL=/nojavascript?url=<%=encodedRequestURL%>" />
</NOSCRIPT>

<title>Cambridge Digital Library - University of Cambridge</title>

<jsp:include page="includes.jsp" />

<link rel="stylesheet" type="text/css"
	href="/scripts/extjs/resources/css/ext-partial-gray.css" />
<link rel="stylesheet" type="text/css" href="/styles/style-document.css" />


<script type="text/javascript">
	var JSONURL = '/json/${docId}.json';
	var pagenum = ${page};
	var docId = '${docId}';
	var docURL = '${docURL}';
</script>

<script type="text/javascript" src="/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="/scripts/docData.js"></script>
<script type="text/javascript" src="/scripts/docViewport.js"></script>
<script type="text/javascript" src="/scripts/docView.js"></script>
<script type="text/javascript" src="/scripts/seadragon-min.js"></script>
<script type="text/javascript" src="/scripts/document.js"></script>

</head>
<body>
	<div id="north">
 
		<header id="globalMasthead" style="width:100%; height:45px">        
                <a id="identifier" class="grid_4 alpha" href="#" title="University of Cambridge">
                    University of Cambridge
                </a>
                
                <span class="header-title"><a href="/" title="Cambridge Digital Library">Cambridge Digital Library</a></span>
                <!-- 
                <nav>
                    <ul class="grid_10">
                        <li class="first">
                            <a href="/" title="Home">
                             Home
                            </a>
                        </li>
                        <li>
                            <a href="/view/" title="Browse">
                                Browse
                            </a>
                        </li>
                        <li>
                            <a href="/about/" title="About">
                                About
                            </a>
                        </li>
                        <li>
                            <a href="/feedback/" title="Feedback">
                                Feedback
                            </a>
                        </li>                                                              
                    </ul>
                </nav> -->
                <a id="libraryLogo" class="grid_4 alpha" title="Cambridge University Library" href="http://www.lib.cam.ac.uk"></a>
            </header>

		<!-- end #globalMasthead -->		
		<jsp:include page="nav-browse-document.jsp" />
		</div>
		