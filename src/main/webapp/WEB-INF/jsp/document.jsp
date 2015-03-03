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

<!-- JQuery -->
<script type="text/javascript" src="/scripts/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/scripts/jquery.paging.min.js"></script>
<script type="text/javascript" src="/scripts/spin.min.js"></script>
<script type="text/javascript" src="/scripts/jquery.xdomainrequest.min.js"></script>

<!--  bootstrap -->
<link rel="stylesheet" href="/styles/bootstrap-default.min.css">
<script src="/scripts/bootstrap.min.js"></script>

<!--  project light font  -->
<script type="text/javascript" src="//use.typekit.com/hyb5bko.js"></script>
<script type="text/javascript">try{Typekit.load();}catch(e){}</script>

<!--  font awesome -->
<link rel="stylesheet" href="/styles/font-awesome/font-awesome.min.css">

<!--  addThis sharing tools -->
<script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-54886fc8007cb9c4" async="async"></script>

<script src="/scripts/cudl.js" type="text/javascript"></script>

<script type="text/javascript">

	cudl.JSONURL = '${jsonURL}';
	cudl.JSONTHUMBURL = '${jsonThumbnailsURL}';
	cudl.pagenum = ${page};
	cudl.docId = '${docId}';
	cudl.docURL = '${docURL}';
	cudl.imageServer = '${imageServer}';
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
<link rel="stylesheet" type="text/css" href="/styles/style-document.css" />

</head>
<body>


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
									
				<a href="/"><img id="cam-logo" class="pull-left" src="/images/documentView/cudl-logo-1x.png" 
				alt="Logo for Cambridge University" title="Cambridge University"></a>
				
		 <div class="cudl-viewer-buttons-pagination">
           <button id="prevPage" class="cudl-btn fa fa-arrow-left" title="Previous Page"></button>
           <input id="pageInput" type="text" value="1" size="4">
           of 
           <span id="maxPage"></span>
           <button id="nextPage" class="cudl-btn fa fa-arrow-right" title="Next Page"></button>
           <br/>           
         </div>
         
				<a href="http://www.lib.cam.ac.uk"><img id="ul-logo" class="pull-right" src="/images/header/ULLogowhite.gif" 
				alt="Logo for Cambridge University Library" title="Cambridge University Library"></a>
				
			</div>
		</div>
	</div>	
	<!-- /.container -->
	<div id="seadragonImage">	
		<div id="doc">

         <span id="pageLabel"></span>         
         
		 <div class="cudl-viewer-buttons-zoom">
           <button id="rotateLeft" class="cudl-btn fa fa-rotate-left" title="Rotate the image 90° left"></button>
           <button id="rotateRight" class="cudl-btn fa fa-rotate-right" title="Rotate the image 90° right"></button>
           <button id="zoomIn" class="cudl-btn fa fa-plus" title="Zoom in to the image"></button>
           <button id="zoomOut" class="cudl-btn fa fa-minus" title="Zoom out of the image"></button>
         </div>
         <div class="cudl-viewer-buttons-maximize">
           <button id="fullscreen" class="cudl-btn fa fa-expand" title="Toggle fullscreen view"></button>
         </div>
        </div>

	</div>
	<div id="right-panel">

		<div id="right-panel-toggle"><i class="fa fa-angle-right pull-left"></i></div>

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
						<li><a id="moreinfotab" aria-controls="moreinfo"
							data-toggle="tab" role="tab" tabindex="-1" href="#metadata">More
								Information</a></li>
						<li><a id="transcriptionnormtab" aria-controls="transcriptionnormtab"
							data-toggle="tab" role="tab" tabindex="-1"
							href="#transcriptionnorm">Transcription (normalised)</a></li>
						<li><a id="transcriptiondiplotab" aria-controls="transcriptiondiplotab"
							data-toggle="tab" role="tab" tabindex="-1" 
							href="#transcriptiondiplo">Transcription (diplomatic)</a></li>
						<li><a id="translationtab" aria-controls="translationtab"
							data-toggle="tab" role="tab" tabindex="-1" href="#translation">Translation</a>
						</li>
						<li><a id="downloadtab" aria-controls="downloadtab"
							data-toggle="tab" role="tab" tabindex="-1" href="#download">Download
								or share</a></li>
					</ul></li>

			</ul>
			<!-- End of nav tabs -->

			<!-- Tab panes -->
			<div id="tab-content" class="tab-content" style="overflow-x: hidden; overflow-y: auto">
			  <div role="tabpanel" class="tab-pane active" id="abouttab">	
			  <span id="doc-breadcrumb"></span>			
				<div id='about-content'><h3><span id="about-header"></span></h3>
				 <div>
				    <span id="about-completeness"></span>
				    <span id="about-abstract"></span>				    
				    <span id="about-metadata"></span>
				    <br><div class="well">
				    <h4>Want to know more?</h4>
				    <p>Under the 'More' menu you can find <a href="#" onclick="cudl.showPanel(&quot;#metadata&quot;)">metadata about the item</a>, 
				    any transcription and translation we have of the text and find out about 
				    <a href="#" onclick="cudl.showPanel(&quot;#download&quot;)">downloading or sharing this image</a>.  
				    </p></div>
				    <div id="zoomRights"><span id="about-imagerights"></span></div>
				 </div>
				</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="contentstab">No
					Contents List Available</div>
				<div role="tabpanel" class="tab-pane" id="thumbnailstab">
					<div id='thumbnails-content'>
						<div id="thumbnailpaginationtop" class="text-center"></div>
						<div id="thumbnailimages"></div>
						<div id="thumbnailpaginationbottom" class="text-center"></div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="metadata"><ol class="breadcrumb"><li class="active">More Information</li></ol><div id="metadatacontent">No Metadata Available</div></div>
				<div role="tabpanel" class="tab-pane" id="transcriptionnorm"><ol class="breadcrumb"><li class="active">Transcription (normalised)</li></ol><iframe id="transcriptionnormframe" src="" ></iframe></div>
				<div role="tabpanel" class="tab-pane" id="transcriptiondiplo"><ol class="breadcrumb"><li class="active">Transcription (diplomatic)</li></ol><iframe id="transcriptiondiploframe" src="" ></iframe></div>
				<div role="tabpanel" class="tab-pane" id="translation"><ol class="breadcrumb"><li class="active">Translation</li></ol><iframe id="translationframe" src="" ></iframe></div>
				<div role="tabpanel" class="tab-pane" id="download">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h3 class="panel-title">Download</h3>
						</div>
						<div class="panel-body">
							<a class="btn btn-info left" href="#" onclick="$('#downloadConfirmation').show();return false;"> <i
								class="fa fa-download fa-2x pull-left"></i> Download This<br>Image
							</a>
							<p class="copyright text col-md-9">This image may be used in
								accord with fair use and fair dealing provisions, including
								teaching and research. If you wish to reproduce it within
								publications or on the public web, please make a reproduction
								request.</p>
							<br /> <br /> <a class="btn btn-info left" href="http://www.lib.cam.ac.uk/deptserv/imagingservices/reproductionrights.html" target="_blank"> <i
								class="fa fa-gavel fa-2x pull-left"></i> Request Image<br>Rights
							</a>
							<p class="copyright text col-md-9">Request reproduction
								rights to this image</p>
							<br /> <br /> <a class="btn btn-info left" href="#" onclick="$('#bookmarkConfirmation').show();return false;"> <i
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
							<p class="col-md-12">If you want to share this page with others you can send them a link to this individual page: <input id="currentURL" type="text" name="link" value="" readonly="readonly" size="60" onclick="select()"><br/></p>							
							<p class="col-md-12">Alternatively please share this page on social media</p> <div class="addthis_sharing_toolbox col-md-12"></div>							
							<p class="col-md-12"><br/>You can also embed the viewer into your own website or blog using the code below:<br/>
							<textarea id="embedCode" name="embedCode" readonly="readonly" cols="60" rows="6" onclick="select()"></textarea><br/></p>
						</div>

						</div>
					</div>
				</div>
			</div>
		</div>

		<!--  Confirmation pop ups -->
        <div id="bookmarkConfirmation" class="alert alert-info" style="display: none">
              <a href="#" class="close" onclick="$('.alert').hide();">&times;</a>
               Do you want to create a bookmark for this page in 'My Library'?<br/><br/>
			  <button type="button" class="btn btn-default btn-success" onclick="cudl.addBookmark()">Yes</button>
  			  <button type="button" class="btn btn-default" onclick="$('#bookmarkConfirmation').hide();">Cancel</button>
        </div>
        <div id="downloadConfirmation" class="alert alert-info" style="display:none">
              <a href="#" class="close" onclick="$('.alert').hide();">&times;</a>
              <p>This image has the following copyright:</p><div class="well" id="downloadCopyright"></div><p>Do you want to download this image?</p>              
			  <button type="button" class="btn btn-default btn-success" onclick="cudl.downloadImage()">Yes</button>
  			  <button type="button" class="btn btn-default" onclick="$('#downloadConfirmation').hide();">No</button>
         </div>
     
     
 <jsp:include page="header/analytics.jsp" />     
</body>

</html>
