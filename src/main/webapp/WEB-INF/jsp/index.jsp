<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
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
								<div id="index-carousel-1" style="position:relative">
									<c:import charEncoding="UTF-8"
										url="/html/index-carousel-1.html" />
								</div>
							</li>
							<li class="campl-slide campl-column12">
								<div id="index-carousel-2" style="position:relative">
									<c:import charEncoding="UTF-8"
										url="/html/index-carousel-2.html" />
								</div>
							</li>
							<li class="campl-slide campl-column12">
								<div id="index-carousel-3" style="position:relative">
									<c:import charEncoding="UTF-8"
										url="/html/index-carousel-3.html" />
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
			<div id="latestNewsDiv">
				<c:import charEncoding="UTF-8" url="/html/index-latest-news.html" />
			</div>



		</div>
	</div>
</div>

<sec:authorize access="hasRole('ROLE_ADMIN')">

	<script type="text/javascript">
		$('#latestNewsDiv').attr('contenteditable', 'true');
		$('#index-carousel-1').attr('contenteditable', 'true');
		$('#index-carousel-2').attr('contenteditable', 'true');
		$('#index-carousel-3').attr('contenteditable', 'true');
	</script>
	<jsp:include page="editor.jsp">
		<jsp:param name='dataElements'
			value='latestNewsDiv,index-carousel-1,index-carousel-2,index-carousel-3' />
		<jsp:param name='filenames'
			value='index-latest-news.html,index-carousel-1.html,index-carousel-2.html,index-carousel-3.html' />
	</jsp:include>

</sec:authorize>

<jsp:include page="header/footer-full.jsp" />

