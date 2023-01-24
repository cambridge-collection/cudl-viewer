<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="title" value="${organisationalCollection.title} : ${item.title}"/>
<c:set var="authors" value="${cudlfn:join(item.authorNames, ', ')}"/>
<c:set var="iiifManifestURL" value="${rootURL}/iiif/${item.id}"/>
<c:set var="fullThumbnailURL" value="${imageServer}/${thumbnailURL}"/>

<cudl:base-page title="${title}">
    <jsp:attribute name="head">
        <cudl:head-content pagetype="DOCUMENT"
                           viewport="width=device-width, maximum-scale=1, initial-scale=1">
            <jsp:attribute name="metaTags">
                <link rel="canonical" href="${fn:escapeXml(canonicalURL)}" />
                <cudl:meta name="keywords" content="${item['abstract']}" />
                <cudl:meta name="description" content="${item['abstract']}" />

                <!-- Tags for search engines -->
                <cudl:meta property="schema:url" content="${canonicalURL}" />
                <cudl:meta property="schema:name rdfs:label dcterms:title" content="${title}"/>
                <cudl:meta property="schema:keywords" name="keywords"  content="${authors}" />
                <cudl:meta property="schema:keywords" content="${item['abstract']}" />
                <cudl:meta property="schema:description rdfs:comment dcterms:description"
                           content="${item['abstract']}"/>
                <cudl:meta property="schema:image" content="${fullThumbnailURL}" />
                <cudl:meta property="schema:thumbnailUrl" content="${fullThumbnailURL}" />

                <!-- Tags for general social media, including Facebook -->
                <cudl:meta property="og:url" content="${canonicalURL}" />
                <cudl:meta property="og:type" content="website"/>
                <cudl:meta property="og:site_name" content="Cambridge Digital Library" />
                <cudl:meta property="og:title" content="${title}"/>
                <cudl:meta property="og:description" content="${item.abstractShort}" />
                <cudl:meta property="og:locale" content="en_GB"/>
                <c:choose>
                    <c:when test="${socialIIIFUrl != null}">
                        <cudl:meta property="og:image" content="${socialIIIFUrl}" />
<%--                <cudl:meta property="og:image:width" content="1200"/>--%>
<%--                <cudl:meta property="og:image:height" content="630"/>--%>
                    </c:when>
                    <c:otherwise>
                        <cudl:meta property="og:image" content="${fullThumbnailURL}" />
                    </c:otherwise>
                </c:choose>

                <!-- Tags for Twitter -->
                <cudl:meta property="twitter:title" content="${title}"/>
                <cudl:meta property="twitter:description" content="${item.abstractShort}" />
                <cudl:meta property="twitter:creator" content="@camdiglib" />
                <cudl:meta property="twitter:site" content="@camdiglib" />
                <c:choose>
                    <c:when test="${socialIIIFUrl != null}">
                        <cudl:meta property="twitter:card" content="summary_large_image"/>
                        <cudl:meta property="twitter:image" content="${socialIIIFUrl}" />
                    </c:when>
                    <c:otherwise>
                        <cudl:meta property="twitter:card" content="summary"/>
                        <cudl:meta property="twitter:image" content="${fullThumbnailURL}" />
                    </c:otherwise>
                </c:choose>

            </jsp:attribute>

            <jsp:body>
                <script type="text/javascript">
                    var addthis_config = addthis_config||{};
                    addthis_config.data_track_addressbar = false;
                    addthis_config.data_track_clickback = false;
                </script>
            </jsp:body>
        </cudl:head-content>
    </jsp:attribute>

    <jsp:attribute name="pageData">
        <%-- FIXME: Some of these arent' used --%>
        <cudl:default-context>
            <json:property name="rootURL" value="${rootURL}"/>
            <json:property name="jsonURL" value="${jsonURL}"/>
            <json:property name="jsonThumbURL" value="${jsonThumbnailsURL}"/>
            <json:property name="pageNum" value="${page}"/>
            <json:property name="docId" value="${item.id}"/>
            <json:property name="docURL" value="${docURL}"/>
            <json:property name="imageServer" value="${imageServer}"/>
            <json:property name="iiifImageServer" value="${iiifImageServer}"/>
            <json:property name="services" value="${services}"/>
            <json:property name="collectionURL" value="${organisationalCollection.URL}"/>
            <json:property name="collectionTitle" value="${organisationalCollection.title}"/>
            <json:property name="parentCollectionURL" value="${parentCollection.URL}"/>
            <json:property name="parentCollectionTitle" value="${parentCollection.title}"/>
            <json:property name="itemTitle" value="${item.title}"/>
            <json:property name="iiifEnabled" value="${item.IIIFEnabled}"/>
            <json:property name="iiifManifestURL" value="${iiifManifestURL}"/>
            <json:property name="iiifMiradorURL" value="/mirador/${item.id}/${page}"/>
            <json:property name="imageReproPageURL" value="${imageReproPageURL}"/>
            <json:property name="viewportNavigatorEnabled" value="${!!viewportNavigator}"/>
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
                            <button id="${textDirectionRightToLeft ? 'nextPage' : 'prevPage'}" class="cudl-btn fa fa-arrow-left"
                                title="${textDirectionRightToLeft ? 'Next Page' : 'Previous Page'}"></button>
                            <input id="pageInput" type="text" value="1" size="4"> of <span
                                id="maxPage"></span>
                            <button id="${textDirectionRightToLeft ? 'prevPage' : 'nextPage'}" class="cudl-btn fa fa-arrow-right"
                                title="${textDirectionRightToLeft ? 'Previous Page' : 'Next Page'}"></button>
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

                    <div class="cudl-viewer-zoom">
                        <div class="cudl-viewer-buttons-zoom">
                            <button id="rotateLeft" class="cudl-btn fa fa-rotate-left"
                                title="Rotate the image 90° left"></button>
                            <%-- Optional rotation slider set in properties --%>
                            <c:if test="${rotationSlider eq true}">
                                <div id="rotationSlider"></div>
                            </c:if>
                            <button id="rotateRight" class="cudl-btn fa fa-rotate-right"
                                title="Rotate the image 90° right"></button>

                            <button id="zoomIn" class="cudl-btn fa fa-plus"
                                title="Zoom in to the image"></button>
                            <button id="zoomOut" class="cudl-btn fa fa-minus"
                                title="Zoom out of the image"></button>
                            <%-- Optional zoom reset button set in properties --%>
                            <c:if test="${zoomResetButton eq true}">
                                <button id="zoomHome" class="cudl-btn fa fa-home"
                                        title="Reset zoom to home view"></button>
                            </c:if>
                        </div>
                        <div class="cudl-viewer-zoom-info">
                            <%-- Optional zoom factor set in properties --%>
                            <c:if test="${zoomFactor eq true}">
                                <span id="zoomFactor"></span>
                            </c:if>
                        </div>
                    </div>
                    <div class="cudl-viewer-buttons-maximize">
                        <button id="fullscreen" class="cudl-btn fa fa-expand"
                            title="Toggle fullscreen view"></button>
                    </div>
                </div>

            </div>
            <div id="right-panel" class="right-panel">
                <div id="doc-breadcrumb"></div>
                <button id="right-panel-toggle" class="btn toggle-btn fa fa-bars"
                     aria-label="Toggle sidebar"></button>

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
                                <li><a id="transcriptiondiplotab"
                                    aria-controls="transcriptiondiplotab" data-toggle="tab"
                                    role="tab" tabindex="-1" href="#transcriptiondiplo">Transcription</a></li>
                                <li><a id="translationtab" aria-controls="translationtab"
                                    data-toggle="tab" role="tab" tabindex="-1" href="#translation">Translation</a>
                                    </li>
                                <li><a id="downloadtab" aria-controls="downloadtab"
                                    data-toggle="tab" role="tab" tabindex="-1" href="#download">Share
                                        </a></li>

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
                                            Under the 'View more options' menu you can find <a class="show-metadata" href="#">metadata
                                                about the item</a>, any transcription and translation we have of
                                            the text and find out about <a class="show-download" href="#">sharing this image</a>.
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
                                <div id="thumbnailimages" class="container-fluid"></div>
                                <div id="thumbnailpaginationbottom" class="text-center"></div>
                            </div>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="metadata">
                            <ol class="breadcrumb">
                                <li class="active">Item Metadata</li>
                            </ol>
                            <div id="metadatacontent">No Metadata Available</div>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="transcriptiondiplo">
                            <div class="breadcrumbdiv"><ol class="breadcrumb">
                                <li class="active">Transcription</li>
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
                        <div role="tabpanel" class="tab-pane" id="download">
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
                <!-- Usage buttons -->
                <div id="use" class="bottom-panel">
<%--                    <div class="panel panel-default">--%>
                        <div class="panel-body">
                            <div id="downloadOption">
                                <div class="button usebutton">
                                    <a class="btn btn-info left" href="#" title="Download Image">
                                        <i class="fa fa-download fa-2x"></i>
                                        <span>Download<br/>Image</span>
                                    </a>
                                </div>
                            </div>
                            <c:if test="${pdfSinglePage eq true}">
                                <div id="singlePagePdfDownloadOption">
                                    <div class="button usebutton">
                                        <a class="btn btn-info left" href="" download="${docId}-${page}.pdf">
                                            <i class="fa fa-file-pdf-o fa-2x pull-left"></i>Page<br/>PDF
                                        </a>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${pdfFullDocument eq true}">
                                <div id="fullDocumentPdfDownloadOption">
                                    <div class="button usebutton">
                                        <a class="btn btn-info left" href="" download="${docId}.pdf">
                                            <i class="fa fa-file-pdf-o fa-2x pull-left"></i>Document<br/>PDF
                                        </a>
                                    </div>
                                </div>
                            </c:if>
                            <div id="rightsOption">
                                <div class="button usebutton" >
                                    <a class="btn btn-info left"
                                       href="${imageReproPageURL}"
                                       target="_blank"
                                       title="Request Rights">
                                        <i class="fa fa-gavel fa-2x"></i>
                                        <span>Request<br/>Rights</span>
                                    </a>
                                </div>
                            </div>
                            <div id="downloadMetadataOption">
                                <div class="button usebutton">
                                    <a class="btn btn-info left" href="#" title="Download Metadata">
                                        <i class="fa fa-file-code-o fa-2x"></i>
                                        <span>Download<br>Metadata</span>
                                    </a>
                                </div>
                            </div>
                            <div id="bookmarkOption">
                                <div class="button usebutton">
                                    <a class="btn btn-info left" href="#" title="Bookmark Image">
                                        <i class="fa fa-bookmark fa-2x"></i>
                                        <span>Bookmark<br/>Image</span>
                                    </a>
                                </div>
                            </div>

                            <div id="iiifOption">
                                <div class="button usebutton">
                                    <a class="btn btn-info left" href="${iiifManifestURL}?manifest=${iiifManifestURL}"
                                       title="View IIIF Manifest">
                                                          <img src="/img/logo-iiif-34x30.png"
                                                               title="International Image Interoperability Framework">
                                        <span>IIIF<br>Manifest</span>
                                    </a>
                                </div>
                            </div>
                            <div id="miradorOption">
                                <div class="button usebutton">
                                    <a class="btn btn-info left" href="#" title="Open in Mirador">
                                                            <img src="/img/mirador.png" title="Open in Mirador">
                                        <span>Open in<br>Mirador</span>
                                    </a>
                                </div>
                            </div>
                        </div>
<%--                    </div>--%>
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
            <div id="singlePagePdfConfirmation" class="alert alert-info"
                 style="display: none">
                <a href="#" class="close">&times;</a>
                <p>The images contained in the pdf download have the following copyright:</p>
                <div class="well" id="pdfSinglePageDownloadCopyright"></div>
                <button type="button" class="btn btn-default btn-success">Download</button>
                <button type="button" class="btn btn-default">Cancel</button>
            </div>
            <div id="fullDocumentPdfConfirmation" class="alert alert-info"
                 style="display: none">
                <a href="#" class="close">&times;</a>
                <p>This will create a PDF with thumbnail images for all pages, and
                    may take some time for large documents.</p>
                <p>The images contained in this document have the following
                    copyright: </p>
                <div class="well" id="pdfFullDocumentDownloadCopyright"></div>
                <button type="button" class="btn btn-default btn-success">Download</button>
                <button type="button" class="btn btn-default">Cancel</button>
            </div>
            <div id="downloadConfirmation" class="alert alert-info"
                style="display: none">
                <a href="#" class="close">&times;</a>
                <p>This image has the following copyright:</p>
                <div class="well" id="downloadCopyright"></div>
                <c:if test="${!empty downloadSizes}">
                    <p>Choose one of the available sizes to download:
                    <select id="downloadSizes" name="downloadSizes">
                        <c:forEach var="downloadSize" items="${downloadSizes}">
                            <option value="${downloadSize.value}">
                                <c:out value="${downloadSize.key}"/>
                                (<c:out value="${downloadSize.value}"/>px)
                            </option>
                        </c:forEach>
                    </select>
                    <button type="button" class="btn btn-default btn-success">Download</button>
                    <button type="button" class="btn btn-default">Cancel</button>
                </c:if>


                <c:if test="${empty downloadSizes}">
                    <p>Sorry, there are no available sizes to download.</p>
                </c:if>

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
