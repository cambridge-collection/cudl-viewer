<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List"%>
<%@taglib prefix="c" 
       uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="0" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<%
	String downtimeWarning = (String) request
	.getAttribute("downtimeWarning");
	String itemCount = (String) request.getAttribute("itemCount");
	List<Collection> rootCollections = (List<Collection>) request.getAttribute("rootCollections");
%>


<%=downtimeWarning%>


<div class="campl-row campl-page-header" id="content">
	<div class="campl-wrap clearfix">
		<div class="campl-column12">
			<div class="campl-recessed-carousel">
				<div class="section-carousel campl-carousel clearfix campl-banner">
					<div class="campl-carousel-container">
						<ul class="campl-unstyled-list campl-slides">
							<li class="campl-slide campl-column12">
								<div class="image-container">
									<a href="/collections/treasures"> <img alt="Treasures"
										title="Treasures" src="/images/index/carousel-treasures.jpg"
										class="campl-scale-with-grid" />
									</a>
								</div>
								<div class="campl-slide-caption">
									<a href="/collections/treasures"><span
										class="campl-slide-caption-txt"> Treasures of the
											Library</span></a>
								</div>
								<div class="carousel-panel" style="overflow: auto;">
									<h4>
										<a href="/collections/treasures">Treasures of the Library</a>
									</h4>

									<p>Many items within the Library’s collections deserve to
										be highlighted. This may be because of their historical
										importance, uniqueness, beauty, fascinating content, or
										perhaps their personal associations. In this special
										collection within the Cambridge Digital Library we will draw
										together books, manuscripts and other items from across our
										collections that are especially significant. Many of them have
										been displayed in Library exhibitions in the past – now they
										can be accessed at any time, from anywhere in the world, and
										browsed cover to cover.</p>

								</div>
							</li>						
							<li class="campl-slide campl-column12">
								<div class="image-container">
									<a href="/collections/newton"> <img alt="Newton"
										title="Newton Papers" src="/images/index/carousel-newton.jpg"
										class="campl-scale-with-grid" />
									</a>
								</div>
								<div class="campl-slide-caption">
									<a href="/collections/newton"><span
										class="campl-slide-caption-txt">Papers of Isaac Newton</span></a>
								</div>
								<div class="carousel-panel" style="overflow: auto;">
									<h4>
										<a href="/collections/newton">Newton Papers</a>
									</h4>

									<p>Cambridge University Library is pleased to present the
										first items in its Foundations of Science collection: a
										selection from the Papers of Sir Isaac Newton. The Library
										holds the most important and substantial collection of
										Newton's scientific and mathematical manuscripts and over the
										next few months we intend to make most of our Newton papers
										available on this site.</p>

									<p>
										This collection features some of Newton's most important work
										from the 1660s, including his <a href="/collections/newton">college
											notebooks</a> and '<a href="/view/MS-ADD-04004/">Waste Book</a>'.
									</p>

								</div>
							</li>
							<li class="campl-slide campl-column12">
								<div class="image-container">
									<a href="/collections/sassoon"> <img alt="Sassoon Journals"
										title="Sassoon Journals"
										src="/images/index/carousel-sassoon.jpg"
										class="campl-scale-with-grid" />
									</a>
								</div>
								<div class="campl-slide-caption">
									<a href="/collections/sassoon"><span
										class="campl-slide-caption-txt">Sassoon Journals</span></a>
								</div>
								<div class="carousel-panel" style="overflow: auto;">
									<h4>
										<a href="/collections/sassoon">Sassoon Journals</a>
									</h4>

									<p>The notebooks kept by the soldier-poet Siegfried Sassoon
										(1886–1967) during his service in the British Army in the
										First World War are among the most remarkable documents of
										their kind, and provide an extraordinary insight into his
										participation in one of the defining conflicts of European
										history.</p>

								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="campl-column4">&nbsp;</div>
		</div>
	</div>
</div>
<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">

		<div class="campl-column12">
			<div class="campl-side-padding ">
				<h2>Collections</h2>
			</div>
		</div>
		<div class="campl-column8 campl-main-content">

			<%
				for (int i=0; i<rootCollections.size(); i++) { 
					       Collection c = rootCollections.get(i);
			%>
			<div class="campl-column6">
				<div class="campl-content-container campl-side-padding">
					<div
						class="campl-horizontal-teaser campl-teaser clearfix campl-focus-teaser">
						<div class="campl-focus-teaser-img">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="<%=c.getURL()%>" class="campl-teaser-img-link"><img
									alt=""
									src="/images/collectionsView/collection-<%=c.getId()%>.jpg"
									class="campl-scale-with-grid" /></a>
							</div>
						</div>
						<div class="campl-focus-teaser-txt">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class='campl-teaser-title'>
									<a href="<%=c.getURL()%>"><%=c.getTitle()%></a>
								</h3>
								<a href="<%=c.getURL()%>" class="ir campl-focus-link">Read
									more</a>
							</div>
						</div>
					</div>
				</div>
			</div>

			<%
				}
			%>

		</div>

		<div class="campl-column4">


			<!-- twitter feed -->
			<a class="twitter-timeline" href="https://twitter.com/CamDigLib"
				data-widget-id="309321526665154560">Tweets by @CamDigLib</a>
			<script>
				!function(d, s, id) {
					var js, fjs = d.getElementsByTagName(s)[0];
					if (!d.getElementById(id)) {
						js = d.createElement(s);
						js.id = id;
						js.src = "//platform.twitter.com/widgets.js";
						fjs.parentNode.insertBefore(js, fjs);
					}
				}(document, "script", "twitter-wjs");
			</script>

			<!-- end of twitter feed -->


		</div>

		<div class="campl-column4">
			<div id="latestNewsDiv" contenteditable="true">				
		        <c:import charEncoding="UTF-8" url="/html/index-latest-news.html" /> 
			</div>
			


		</div>
	</div>
</div>

<jsp:include page="editor.jsp" >
  <jsp:param name='dataElements' value='latestNewsDiv'/>
  <jsp:param name='filenames' value='index-latest-news.html'/>
</jsp:include>

<jsp:include page="header/footer-full.jsp" />

