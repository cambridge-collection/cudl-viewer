<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List"%>
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
										<a href="/collections/newton"> <img alt="Newton" title="Newton Papers"
											src="/images/index/carousel-newton.jpg"
											class="campl-scale-with-grid" />
										</a>
									</div>
									<div class="campl-slide-caption">
										<a href="/collections/newton"><span class="campl-slide-caption-txt">Papers
												of Isaac Newton</span></a>
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
										<a href="/collections/music"> <img alt="Music" title="Music"
											src="/images/index/carousel-music.jpg"
											class="campl-scale-with-grid" />
										</a>
									</div>
									<div class="campl-slide-caption">
										<a href="/collections/music"><span class="campl-slide-caption-txt">Music
												Collection</span></a>
									</div>
									<div class="carousel-panel" style="overflow: auto;">
										<h4>
											<a href="/collections/music">Music</a>
										</h4>

										<p>
											The music collections at Cambridge University Library play an
											important role in the preservation of musical heritage, both
											on a national and international level. From printed and
											manuscript music scores and texts on music to ephemera and
											concert programmes, archival materials documenting the life
											and work of composers, music scholars and performers, a huge
											variety and breadth of material has been collected over
											several centuries. We launch with a selection of important
											early lute music, digitised and described in collaboration
											with the <a href="http://www.lutesociety.org" target="_blank">Lute
												Society</a>
										</p>

									</div>
								</li>
								<li class="campl-slide campl-column12">
									<div class="image-container">
										<a href="/collections/sassoon"> <img alt="Sassoon Journals" title="Sassoon Journals"
											src="/images/index/carousel-sassoon.jpg"
											class="campl-scale-with-grid" />
										</a>
									</div>
									<div class="campl-slide-caption">
										<a href="/collections/sassoon"><span class="campl-slide-caption-txt">Sassoon
												Journals</span></a>
									</div>
									<div class="carousel-panel" style="overflow: auto;">
										<h4>
											<a href="/collections/sassoon">Sassoon Journals</a>
										</h4>

										<p>The notebooks kept by the soldier-poet Siegfried
											Sassoon (1886–1967) during his service in the British Army in
											the First World War are among the most remarkable documents
											of their kind, and provide an extraordinary insight into his
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
									<a href="<%=c.getURL()%>" class="campl-teaser-img-link"><img alt=""
										src="/images/collectionsView/collection-<%=c.getId()%>.jpg"
										class="campl-scale-with-grid" /></a>
								</div>
							</div>
							<div class="campl-focus-teaser-txt">
								<div class="campl-content-container campl-horizontal-teaser-txt">
									<h3 class='campl-teaser-title'>
										<a href="<%=c.getURL()%>"><%=c.getTitle()%></a>
									</h3>
									<a href="<%=c.getURL()%>" class="ir campl-focus-link">Read more</a>
								</div>
							</div>
						</div>
					</div>
				</div>		
		
		<% } %>				

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
				<div
					class="campl-content-container campl-sub-column-left-border campl-main-content-sub-column">
					<div class="campl-heading-container">
						<h2>Latest news</h2>
					</div>
				</div>
				<div
					class="campl-content-container campl-no-top-padding campl-sub-column-left-border campl-main-content-sub-column">
					<div class="campl-listing-item campl-news-listing clearfix">
						<p class="campl-listing-title">
							<a href="#4">February Release</a>
						</p>
						<span class="campl-datestamp">9 February 2015</span>
						<p>Our first release of 2015 sees the addition of 16 volumes to the Darwin Manuscripts collection. Amongst these, DAR 185 is especially interesting, containing marvellous hand-coloured drawings by Darwin’s children on the backs of heavily revised pages of the Origin of Species manuscript. Darwin himself showed little interest in preserving this draft, and these pages were kept for the children’s drawings rather than for their content. They give a fascinating glimpse into life inside the Darwin household, as well as further insights into the development of the Origin.</p>
					</div>
					<p>
						<a href="" class="campl-secondary-cta campl-float-right ">View
							all news</a>
					</p>
					<br />
				</div>

			</div>
		</div>
	</div>
	
<jsp:include page="header/footer-full.jsp" />	
	
	