<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder, ulcambridge.foundations.viewer.model.*"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<%!public String prepareForMetaTag(String input) {

		// replacing double quotes with single quotes 
		String output = input.replaceAll("\"", "'");

		// remove [ or ]
		output = output.replaceAll("\\[|\\]", "");

		return output;
	}%>
<%
	String requestURL = request.getAttribute("requestURL").toString();
	String encodedRequestURL = URLEncoder.encode(requestURL, "UTF-8");
	String thumbnailURL = requestURL.replaceFirst("/view" + ".*$",
			request.getAttribute("thumbnailURL").toString());
	Collection collection = (Collection) request
			.getAttribute("organisationalCollection");
	Collection parentCollection = (Collection) request
			.getAttribute("parentCollection");
	String collectionURL = "";
	String collectionTitle = "";
	String parentCollectionURL = "";
	String parentCollectionTitle = "";

	if (collection != null) {
		collectionURL = collection.getURL();
		collectionTitle = collection.getTitle();
	}

	if (parentCollection != null) {
		parentCollectionURL = parentCollection.getURL();
		parentCollectionTitle = parentCollection.getTitle();
	}

	// For use in meta tags and to aid search.
	String itemTitle = request.getAttribute("itemTitle").toString();
	Object itemAbstract = request.getAttribute("itemAbstract");
	String metaItemAbstract = "";
	if (itemAbstract != null) {
		metaItemAbstract = prepareForMetaTag(itemAbstract.toString());
	}
	String metaItemAuthors = prepareForMetaTag(request.getAttribute(
			"itemAuthors").toString());
	String docId = request.getAttribute("docId").toString();
	String metaRequestURL = requestURL.replaceFirst(
			docId + "/" + ".*$", docId);
	String title = collectionTitle + " : " + itemTitle;
	String metaTitle = prepareForMetaTag(title);
%>

<!-- page metadata tags -->
<title><%=title%></title>
<meta property="og:type" content="website" />
<meta property="twitter:card" content="summary" />

<!-- Item URI -->
<meta property="schema:url" content="<%=metaRequestURL%>" />
<meta property="og:url" content="<%=metaRequestURL%>" />
<link rel="canonical" href="<%=metaRequestURL%>" />

<!-- Item Title -->
<meta property="schema:name rdfs:label dcterms:title"
	content="<%=metaTitle%>" />
<meta property="og:title" content="<%=metaTitle%>" />
<meta property="twitter:title" content="<%=metaTitle%>" />
<meta name="keywords" property="schema:keywords"
	content="<%=metaItemAuthors%>" />

<!-- Item Description -->
<%
	if (itemAbstract != null) {
%>
<meta property="schema:description rdfs:comment dcterms:description"
	content="<%=metaItemAbstract%>" />
<meta property="og:description" content="<%=metaItemAbstract%>" />
<meta property="twitter:description" content="<%=metaItemAbstract%>" />
<meta name="description" content="<%=metaItemAbstract%>" />
<meta property="schema:keywords" content="<%=metaItemAbstract%>" />
<meta name="keywords" content="<%=metaItemAbstract%>" />
<%
	}
%>

<!-- Image URI (Thumbnail) -->
<meta property="schema:image" content="<%=thumbnailURL%>" />
<meta property="og:image" content="<%=thumbnailURL%>" />
<meta property="twitter:image" content="<%=thumbnailURL%>" />
<meta property="schema:thumbnailUrl" content="<%=thumbnailURL%>" />

<meta property="og:site_name" content="Cambridge Digital Library" />
<meta property="twitter:creator" content="@camdiglib" />
<meta property="twitter:site" content="@camdiglib" />

<link rel="stylesheet" type="text/css" href="/styles/style-document.css" />

<!-- JQuery -->
<script type="text/javascript" src="/scripts/jquery-1.11.1.min.js"></script>
<script type="text/javascript"
	src="/scripts/fancybox/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="/scripts/jquery.paging.min.js"></script>
<script type="text/javascript" src="/scripts/spin.min.js"></script>

<!--  bootstrap -->
<link rel="stylesheet" href="/styles/bootstrap-default.min.css">
<script src="/scripts/bootstrap.min.js"></script>

<!--  font awesome -->
<link rel="stylesheet" href="/styles/font-awesome/font-awesome.min.css">

<script src="/scripts/cudl.js" type="text/javascript"></script>

<script type="text/javascript">

	cudl.JSONURL = '${jsonURL}';
	cudl.JSONTHUMBURL = '${jsonThumbnailsURL}';
	cudl.pagenum = ${page};
	cudl.docId = '${docId}';
	cudl.docURL = '${docURL}';
	cudl.imageServer = '${imageServer}';
	cudl.proxyURL = '${proxyURL}';
	cudl.services = '${services}';	
	// Read in Attributes
	cudl.collectionURL = "<%=collectionURL%>";
	cudl.collectionTitle = "<%=collectionTitle%>";	
	cudl.parentCollectionURL = "<%=parentCollectionURL%>";
    cudl.parentCollectionTitle = "<%=parentCollectionTitle%>";
	cudl.itemTitle = "${itemTitle}";
	cudl.itemAuthors = ${itemAuthors};
	cudl.itemAuthorsFullForm = ${itemAuthorsFullform};
</script>

<script type="text/javascript" src="/scripts/openseadragon.min.js"></script>
<script type="text/javascript" src="/scripts/document.js"></script>

</head>
<body>

	<!-- Go to www.addthis.com/dashboard to customize your tools -->
	<script type="text/javascript"
		src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-548865291dc37516"
		async="async"></script>


	<!--  hidden section for the search engines to index -->
	<div style="display: none">
		<h1><%=metaTitle%></h1>
		<h2><%=metaItemAuthors%></h2>
		<h2><%=collectionTitle%></h2>
		<p><%=metaItemAbstract%></p>
	</div>

	<!--  main site -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<img class="pull-left"
					src="/images/documentView/logo.png">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand pull-left text-left" href="/#">&nbsp;
					Cambridge Digital Library</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"></li>
					<li></li>
					<li></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<!-- /.container -->
	<div id="seadragonImage">	
		<div id="doc">
		 <div class="cudl-viewer-buttons-pagination">
           <button id="prevPage" class="cudl-btn fa fa-arrow-left" title="Previous Page"></button>
           <input id="pageInput" type="text" value="1" size="4">
           <button id="nextPage" class="cudl-btn fa fa-arrow-right" title="Next Page"></button>
         </div>
		 <div class="cudl-viewer-buttons-zoom">
           <button id="rotateLeft" class="cudl-btn fa fa-rotate-left" title="Rotate the image 90° left"></button>
           <button id="rotateRight" class="cudl-btn fa fa-rotate-right" title="Rotate the image 90° right"></button>
           <button id="zoomIn" class="cudl-btn fa fa-plus" title="Zoom in to the image"></button>
           <button id="zoomOut" class="cudl-btn fa fa-minus" title="Zoom out of the image"></button>
         </div>
         <div class="cudl-viewer-buttons-maximize">
           <button id="fullscreen" class="cudl-btn fa fa-expand" title="View image fullscreen"></button>
         </div>
        </div>

	</div>
	<div id="right-panel">

		<div id="right-panel-toggle"><i
								class="fa fa-angle-right pull-left"></i></div>
		<div role="tabpanel" id="rightTabs">

			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#abouttab"
					aria-controls="about" role="tab" data-toggle="tab">About</a></li>
				<li role="presentation"><a href="#contentstab"
					aria-controls="contents" role="tab" data-toggle="tab">Contents</a></li>
				<li role="presentation"><a href="#thumbnailstab"
					aria-controls="thumbnails" role="tab" data-toggle="tab">Thumbnails</a></li>
				<li class="dropdown" role="presentation"><a id="moreDropDown"
					class="dropdown-toggle" aria-controls="moreDropDown-contents"
					data-toggle="dropdown" href="#"> More <span class="caret"></span>
				</a>
					<ul id="moreDropDown-contents" class="dropdown-menu"
						aria-labelledby="moreDropDown" role="menu">
						<li><a id="dropdown0-tab" aria-controls="dropdown0"
							data-toggle="tab" role="tab" tabindex="-1" href="#metadata">More
								Information</a></li>
						<li><a id="dropdown1-tab" aria-controls="dropdown1"
							data-toggle="tab" role="tab" tabindex="-1"
							href="#transcriptionnorm">Transcription (normalised)</a></li>
						<li><a id="dropdown2-tab" aria-controls="dropdown2"
							data-toggle="tab" role="tab" tabindex="-1"
							href="#transcriptiondiplo">Transcription (diplomatic)</a></li>
						<li><a id="dropdown3-tab" aria-controls="dropdown3"
							data-toggle="tab" role="tab" tabindex="-1" href="#translation">Translation</a>
						</li>
						<li><a id="dropdown4-tab" aria-controls="dropdown4"
							data-toggle="tab" role="tab" tabindex="-1" href="#download">Download
								or share</a></li>
					</ul></li>

			</ul>
			<!-- End of nav tabs -->

			<!-- Tab panes -->
			<div id="tab-content" class="tab-content"
				style="overflow-x: hidden; overflow-y: auto">
				<div role="tabpanel" class="tab-pane active" id="abouttab"></div>
				<div role="tabpanel" class="tab-pane" id="contentstab">No
					Contents List Available</div>
				<div role="tabpanel" class="tab-pane" id="thumbnailstab">
					<div id='thumbnails-content'>
						<div id="thumbnailpaginationtop" class="text-center"></div>
						<div id="thumbnailimages"></div>
						<div id="thumbnailpaginationbottom" class="text-center"></div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="metadata">Metadata</div>
				<div role="tabpanel" class="tab-pane" id="transcriptionnorm">Normalised
					Transcription</div>
				<div role="tabpanel" class="tab-pane" id="transcriptiondiplo">Diplomatic
					Transcription</div>
				<div role="tabpanel" class="tab-pane" id="translation">Translation</div>
				<div role="tabpanel" class="tab-pane" id="download">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">Download</h3>
						</div>
						<div class="panel-body">
							<a class="btn btn-primary left" href="#"> <i
								class="fa fa-download fa-2x pull-left"></i> Download This<br>Image
							</a>
							<p class="copyright text col-md-9">This image may be used in
								accord with fair use and fair dealing provisions, including
								teaching and research. If you wish to reproduce it within
								publications or on the public web, please make a reproduction
								request.</p>
							<br /> <br /> <a class="btn btn-primary left" href="#"> <i
								class="fa fa-gavel fa-2x pull-left"></i> Request Image<br>Rights
							</a>
							<p class="copyright text col-md-9">Request reproduction
								rights to this image</p>
							<br /> <br /> <a class="btn btn-primary left" href="#"> <i
								class="fa fa-bookmark fa-2x pull-left"></i> Bookmark This<br>Image
							</a>
							<p class="copyright text col-md-9">Add this image / page to your personal bookmarks for quick access later.</p>
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">Share</h3>
						</div>
						<div class="panel-body">
							<p class="col-md-12">If you want to share this page with others you can send them a link to this individual page: <b><script>document.write(cudl.docURL+"/"+cudl.pagenum)</script></b></p>
							<p class="col-md-12">Alternatively please share this page on social media</p> <div class="addthis_sharing_toolbox col-md-12"></div>
						</div>

						</div>
					</div>
				</div>
			</div>
		</div>
</body>

</html>
