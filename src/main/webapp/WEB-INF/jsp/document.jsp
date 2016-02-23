<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="title" value="${organisationalCollection.title} : ${item.title}"/>
<c:set var="authors" value="${cudlfn:join(item.authorNames, ', ')}"/>

<cudl:base-page title="${title}">
	<jsp:attribute name="head">
		<cudl:head-content pagetype="DOCUMENT"
						   viewport="width=device-width, maximum-scale=1, initial-scale=1">
			<jsp:attribute name="metaTags">
				<!-- page metadata tags -->
				<cudl:meta property="og:type" content="website"/>
				<cudl:meta property="twitter:card" content="summary"/>

                <!-- Item URI -->
                <cudl:meta property="schema:url" content="${canonicalURL}" />
                <cudl:meta property="og:url" content="${canonicalURL}" />
                <link rel="canonical" href="${fn:escapeXml(canonicalURL)}" />

                <!-- Item Title -->
                <cudl:meta property="schema:name rdfs:label dcterms:title" content="${title}"/>
                <cudl:meta property="og:title" content="${title}"/>
                <cudl:meta property="twitter:title" content="${title}"/>
                <cudl:meta name="keywords" property="schema:keywords"
                		   content="${authors}" />

                <!-- Item Description -->
                <%-- Tomcat 7 seems to reserve the .abstract property... --%>
                <c:if test="${fn:length(fn:trim(item['abstract'])) > 0}">
                	<cudl:meta property="schema:description rdfs:comment dcterms:description"
							   content="${item['abstract']}"/>
					<cudl:meta property="og:description" content="${item['abstract']}" />
					<cudl:meta property="twitter:description" content="${item['abstract']}" />
					<cudl:meta name="description" content="${item['abstract']}" />
					<cudl:meta property="schema:keywords" content="${item['abstract']}" />
					<cudl:meta name="keywords" content="${item['abstract']}" />
                </c:if>

                <!-- Image URI (Thumbnail) -->
                <cudl:meta property="schema:image" content="${thumbnailURL}" />
                <cudl:meta property="og:image" content="http://cudl.lib.cam.ac.uk/images/index/carousel-treasures.jpg" />
                <cudl:meta property="twitter:image" content="${thumbnailURL}" />
                <cudl:meta property="schema:thumbnailUrl" content="${thumbnailURL}" />

                <cudl:meta property="og:site_name" content="Cambridge Digital Library" />
                <cudl:meta property="twitter:creator" content="@camdiglib" />
                <cudl:meta property="twitter:site" content="@camdiglib" />
			</jsp:attribute>
		</cudl:head-content>
	</jsp:attribute>

	<jsp:attribute name="pageData">
		<%-- FIXME: Some of these arent' used --%>
		<cudl:default-context>
			<json:property name="jsonURL" value="${jsonURL}"/>
			<json:property name="jsonThumbURL" value="${jsonThumbnailsURL}"/>
			<json:property name="pageNum" value="${page}"/>
			<json:property name="docId" value="${item.id}"/>
			<json:property name="docURL" value="${docURL}"/>
			<json:property name="imageServer" value="${imageServer}"/>
			<json:property name="services" value="${services}"/>
			<json:property name="collectionURL" value="${organisationalCollection.URL}"/>
			<json:property name="collectionTitle" value="${organisationalCollection.title}"/>
			<json:property name="parentCollectionURL" value="${parentCollection.URL}"/>
			<json:property name="parentCollectionTitle" value="${parentCollection.title}"/>
			<json:property name="itemTitle" value="${item.title}"/>
			<json:array name="itemAuthors" items="${item.authorNames}"/>
			<json:array name="itemAuthorsFullForm" items="${item.authorNamesFullForm}"/>

			<%-- Tagging related data --%>
			<sec:authorize access="hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')">
            	<json:property name="isUser" value="${true}"/>
            </sec:authorize>
            <json:property name="taggingEnabled" value="${!!taggable}"/>
		</cudl:default-context>
	</jsp:attribute>

	<jsp:body>
		<div id="container">

        	<!--  hidden section for the search engines to index -->
        	<div style="display: none">
        		<h1><c:out value="${title}"/></h1>
        		<h2><c:out value="${authors}"/></h2>
        		<h2><c:out value="${organisationalCollection.title}"/></h2>
        		<p><c:out value="${item['abstract']}"/></p>
        	</div>

        	<!--  main site -->
        	<div class="navbar navbar-inverse navbar-fixed-top">
        		<div class="container">
        			<div class="navbar-header">

        				<a href="/"><img id="cam-logo" class="pull-left"
        					src="/img/documentView/cudl-logo-1x.png"
        					alt="Logo for Cambridge University" title="Cambridge University"></a>

        				<div class="cudl-viewer-buttons-pagination">
        					<button id="prevPage" class="cudl-btn fa fa-arrow-left"
        						title="Previous Page"></button>
        					<input id="pageInput" type="text" value="1" size="4"> of <span
        						id="maxPage"></span>
        					<button id="nextPage" class="cudl-btn fa fa-arrow-right"
        						title="Next Page"></button>
        					<br />
        				</div>

        				<a href="http://www.lib.cam.ac.uk"><img id="ul-logo"
        					class="pull-right" src="/img/header/ULLogowhite.gif"
        					alt="Logo for Cambridge University Library"
        					title="Cambridge University Library"></a>

        			</div>
        		</div>
        	</div>
        	<!-- /.container -->
        	<div id="seadragonImage">
        		<div id="doc">

        			<span id="pageLabel"></span>

        			<div class="cudl-viewer-buttons-zoom">
        				<button id="rotateLeft" class="cudl-btn fa fa-rotate-left"
        					title="Rotate the image 90° left"></button>
        				<button id="rotateRight" class="cudl-btn fa fa-rotate-right"
        					title="Rotate the image 90° right"></button>
        				<button id="zoomIn" class="cudl-btn fa fa-plus"
        					title="Zoom in to the image"></button>
        				<button id="zoomOut" class="cudl-btn fa fa-minus"
        					title="Zoom out of the image"></button>
        			</div>
        			<div class="cudl-viewer-buttons-maximize">
        				<button id="fullscreen" class="cudl-btn fa fa-expand"
        					title="Toggle fullscreen view"></button>
        			</div>
        		</div>

        	</div>
        	<div id="right-panel">
                <div id="doc-breadcrumb"></div>
        		<div id="right-panel-toggle">
        			<i class="fa fa-angle-right pull-left"></i>
        		</div>

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
        					data-toggle="dropdown" href="#"> View more options <span class="caret"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        				</a>
        					<ul id="moreDropDown-contents" class="dropdown-menu"
        						aria-labelledby="moreDropDown" role="menu">
        						<li><a id="moreinfotab" aria-controls="moreinfo"
        							data-toggle="tab" role="tab" tabindex="-1" href="#metadata">Item metadata</a></li>
        						<li><a id="transcriptionnormtab"
        							aria-controls="transcriptionnormtab" data-toggle="tab" role="tab"
        							tabindex="-1" href="#transcriptionnorm">Transcription
        								(normalised)</a></li>
        						<li><a id="transcriptiondiplotab"
        							aria-controls="transcriptiondiplotab" data-toggle="tab"
        							role="tab" tabindex="-1" href="#transcriptiondiplo">Transcription
        								(diplomatic)</a></li>
        						<li><a id="translationtab" aria-controls="translationtab"
        							data-toggle="tab" role="tab" tabindex="-1" href="#translation">Translation</a>
        							</li>
        						<li><a id="similaritemstab" aria-controls="similaritemstab"
        							data-toggle="tab" role="tab" tabindex="-1" href="#similaritems">Similar items</a>
        						</li>
        						<li><a id="downloadtab" aria-controls="downloadtab"
        							data-toggle="tab" role="tab" tabindex="-1" href="#download">Download
        								or share</a></li>

								<%-- genizah tagging --%>
								<li><a id="taggingtab" aria-controls="taggingtab"
									data-toggle="tab" role="tab" tabindex="-1" href="#tagging">Tagging</a></li>
        					</ul></li>

        			</ul>
        			<!-- End of nav tabs -->

        			<!-- Tab panes -->
        			<div id="tab-content" class="tab-content"
        				style="overflow-x: auto; overflow-y: auto">
        				<div role="tabpanel" class="tab-pane active" id="abouttab">
        					<div id='about-content'>
        						<h3>
        							<span id="about-header"></span>
        						</h3>
        						<div>
        							<span id="about-completeness"></span> <span id="about-abstract"></span>
        							<span id="about-metadata"></span><span id="about-docAuthority"></span> <br>
        							<div id="know-more" class="well">
        								<h4>Want to know more?</h4>
        								<p>
        									Under the 'More' menu you can find <a class="show-metadata" href="#">metadata
        										about the item</a>, any transcription and translation we have of
        									the text and find out about <a class="show-download" href="#">downloading
        										or sharing this image</a>.
        								</p>
        							</div>
        							<div id="zoomRights">
        								<span id="about-imagerights"></span>
        							</div>
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
        				<div role="tabpanel" class="tab-pane" id="metadata">
        					<ol class="breadcrumb">
        						<li class="active">Item Metadata</li>
        					</ol>
        					<div id="metadatacontent">No Metadata Available</div>
        				</div>
        				<div role="tabpanel" class="tab-pane" id="transcriptionnorm">
        					<div class="breadcrumbdiv"><ol class="breadcrumb">
        						<li class="active">Transcription (normalised)</li>
        					</ol></div>
        					<div class="framediv">
        					<iframe id="transcriptionnormframe" src=""></iframe>
        					</div>
        				</div>
        				<div role="tabpanel" class="tab-pane" id="transcriptiondiplo">
        					<div class="breadcrumbdiv"><ol class="breadcrumb">
        						<li class="active">Transcription (diplomatic)</li>
        					</ol></div>
        					<div class="framediv">
        					<iframe id="transcriptiondiploframe" src=""></iframe>
        					</div>

        				</div>
        				<div role="tabpanel" class="tab-pane" id="translation">
        					<div class="breadcrumbdiv"><ol class="breadcrumb">
        						<li class="active">Translation</li>
        					</ol></div>
        					<div class="framediv">
        					<iframe id="translationframe" src=""></iframe>
        					</div>
        				</div>
        				<div role="tabpanel" class="tab-pane" id="similaritems">
        					<div class="breadcrumbdiv"><ol class="breadcrumb">
        						<li class="active">Similar items</li>
        					</ol></div>
        					<div class="similarity-container">
        					</div>
        				</div>
        				<div role="tabpanel" class="tab-pane" id="download">
        					<div class="panel panel-default">
        						<div class="panel-heading">
        							<h3 class="panel-title">Download</h3>
        						</div>
        						<div class="panel-body downloadpanel">
        						    <div id="downloadOption">
										<div class="button">
											<a class="btn btn-info left" href="#">
												<i class="fa fa-download fa-2x pull-left"></i>
        							  			Download This<br>Image
											</a>
										</div>
										<div>
											<p class="copyright text" id="downloadCopyright2"></p>
										</div>
									</div>
									<div id="rightsOption">
										<div class="button">
											<a class="btn btn-info left"
											href="http://www.lib.cam.ac.uk/deptserv/imagingservices/reproductionrights.html"
											target="_blank"> <i class="fa fa-gavel fa-2x pull-left"></i>
											Request Image<br>Rights
											</a>
										</div>
										<div>
											<p class="copyright text">Request reproduction rights to this image</p>
										</div>
									</div>
									<div id="bookmarkOption">
										<div class="button">
											<a class="btn btn-info left" href="#">
												<i class="fa fa-bookmark fa-2x pull-left"></i>
												Bookmark This<br>Image
											</a>
										</div>
										<div>
											<p class="copyright text">
												Add this image / page to your personal bookmarks for quick access later.
											</p>
										</div>
									</div>
									<div id="downloadMetadataOption">
										<div class="button">
											<a class="btn btn-info left" href="#">
												<i class="fa fa-file-code-o fa-2x pull-left"></i> Download<br>Metadata
										 	</a>
										</div>
										<div>
											<p class="copyright text">Download original metadata for this document.</p>
										</div>
									</div>
								</div>
        					</div>
        					<div class="panel panel-default">
        						<div class="panel-heading">
        							<h3 class="panel-title">Share</h3>
        						</div>
        						<div class="panel-body">
        							<div>
        								If you want to share this page with others you can send them a
        								link to this individual page: <div class="displaybox" id="currentURL"></div>
        							</div>
        							<div>Alternatively please share this page on
        								social media</div>
        							<div class="addthis_sharing_toolbox col-md-12">
        							    <script type="text/javascript"
                                                src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-54886fc8007cb9c4"
                                                async="async"></script>

        							</div>
        							<div id="embedOption">
        								<br />You can also embed the viewer into your own website or
        								blog using the code below:<br />
        								<div id="embedCode" class="displaybox"></div>
        								<br />
        							</div>
        						</div>

        					</div>
        				</div>
						<%-- genizah tagging --%>
						<div role="tabpanel" class="tab-pane" id="tagging"></div>
        			</div>
        		</div>
        	</div>

        	<!--  Confirmation pop ups -->
        	<div id="bookmarkConfirmation" class="alert alert-info"
        		style="display: none">
        		<a href="#" class="close">&times;</a> Do
        		you want to create a bookmark for this page in 'My Library'?<br /> <br />
        		<button type="button" class="btn btn-default btn-success">Yes</button>
        		<button type="button" class="btn btn-default">Cancel</button>
        	</div>
        	<div id="downloadConfirmation" class="alert alert-info"
        		style="display: none">
        		<a href="#" class="close">&times;</a>
        		<p>This image has the following copyright:</p>
        		<div class="well" id="downloadCopyright"></div>
        		<p>Do you want to download this image?</p>
        		<button type="button" class="btn btn-default btn-success">Yes</button>
        		<button type="button" class="btn btn-default">No</button>
        	</div>
			<div id="downloadMetadataConfirmation" class="alert alert-info"
				style="display: none">
				<a href="#" class="close">&times;</a>
				<p>This metadata has the following copyright:</p>
				<div class="well" id="downloadMetadataCopyright"></div>
				<p>Do you want to download metadata for this document?</p>
				<button type="button" class="btn btn-default btn-success">Yes</button>
				<button type="button" class="btn btn-default">No</button>
			</div>
        </div>
	</jsp:body>
</cudl:base-page>
