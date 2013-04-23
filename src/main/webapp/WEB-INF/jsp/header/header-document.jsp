<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
import="java.net.URLEncoder, ulcambridge.foundations.viewer.model.*"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
<%
	String requestURL = request.getAttribute("requestURL").toString();
	String encodedRequestURL = URLEncoder.encode(requestURL, "UTF-8");
	Collection collection = (Collection) request.getAttribute("organisationalCollection");
	String collectionURL = "";
	String collectionTitle = "";
	
	if (collection!=null) {
		collectionURL = collection.getURL();
		collectionTitle = collection.getTitle();
	}
%>
<NOSCRIPT>
	<!--  no javascript redirect. -->
	<META HTTP-EQUIV="refresh"
		content="0; URL=/nojavascript?url=<%=encodedRequestURL%>" />
</NOSCRIPT>

<jsp:include page="includes.jsp" />

<link rel="stylesheet" type="text/css"
	href="/scripts/extjs/resources/css/ext-partial-gray.css" />
<link rel="stylesheet" type="text/css" href="/styles/style-document.css" />

<link href="/styles/treestyler.css" rel="stylesheet" type="text/css" media="screen" />

<script type="text/javascript">

	cudl.JSONURL = '${jsonURL}';
	cudl.pagenum = ${page};
	cudl.docId = '${docId}';
	cudl.docURL = '${docURL}';
	cudl.proxyURL = '${proxyURL}';
	
	// Read in Attributes
	cudl.collectionURL = "<%=collectionURL%>";
	cudl.collectionTitle = "<%=collectionTitle%>";	
	cudl.itemTitle = "${itemTitle}";
	cudl.itemAuthors = ${itemAuthors};
	cudl.itemAuthorsFullForm = ${itemAuthorsFullform}

</script>

<script type="text/javascript" src="/scripts/extjs/ext-all.js"></script>
<script type="text/javascript" src="/scripts/cudl-docData.js"></script>
<script type="text/javascript" src="/scripts/cudl-docViewport.js"></script>
<script type="text/javascript" src="/scripts/cudl-docView.js"></script>
<script type="text/javascript" src="/scripts/seadragon-min.js"></script>
<script type="text/javascript" src="/scripts/cudl-document.js"></script>

<title>${itemTitle}</title>

</head>
<body>
	<div id="north">
 
		<header id="globalMasthead" style="width:100%; height:45px">        
                <a id="identifier" class="grid_4 alpha" href="http://www.cam.ac.uk" title="University of Cambridge">
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
                            <a href="/collections/newton" title="Browse">
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
		