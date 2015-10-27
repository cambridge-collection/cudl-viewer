<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="MY_LIBRARY">
	<cudl:nav activeMenuIndex="${3}" displaySearch="true" title="My Library"/>

	<form>
		<input type="hidden" name="username" value="${fn:escapeXml(username)}"/>
	</form>

	<div class="clear"></div>

	<div class="campl-row campl-content campl-recessed-content">
		<div class="campl-wrap clearfix">

			<div class="campl-column7  campl-main-content" id="content">
				<div class="campl-content-container">

					Create your own collection by bookmarking any page within Cambridge
					Digital Library, and it will appear here. <br /> <br />
					<jsp:include page="mylibrary-help.jsp" />

				</div>
			</div>
			<div class="campl-column5 campl-secondary-content ">

				<div class="campl-heading-container">
					<h2>My Bookmarks</h2>
				</div>

				<ol id="collections_carousel">
					<c:forEach items="${bookmarks}" var="bookmark" varStatus="loop">
						<c:set var="item" value="${cudlfn:getItem(itemFactory, bookmark.itemId)}"/>

						<c:choose>
							<c:when test="${item.thumbnailOrientation == 'portrait'}">
								<c:set var="imageDimensions" value="height: 100%"/>
							</c:when>
							<c:when test="${item.thumbnailOrientation == 'landscape'}">
								<c:set var="imageDimensions" value="width: 100%"/>
							</c:when>
						</c:choose>

						<li>
							<div class="collections_carousel_item">
								<div class="collections_carousel_image_box">
									<div class="collections_carousel_image" id="collections_carousel_item${loop.index}">
										<a href="/view/${cudlfn:uriEnc(item.id)}/${cudlfn:uriEnc(bookmark.page)}">
											<img src="${fn:escapeXml(bookmark.thumbnailURL)}"
												 alt="${fn:escapeXml(item.id)}"
												 style="${fn:escapeXml(imageDimensions)}">
										</a>
									</div>
								</div>
								<div class='collections_carousel_text word-wrap-200'>
									<h5><c:out value="${item.title} (${item.shelfLocator})"/></h5>
									Image: <c:out value="${bookmark.page}"/>
									<br>
									<c:out value="${item.abstractShort}"/> &hellip;
									<a href="/view/${cudlfn:uriEnc(item.id)}/1">more</a>
								</div>
								<a class="bookmark-removelink"
								   href="/mylibrary/deletebookmark/?itemId=${cudlfn:uriEnc(item.id)}&page=${cudlfn:uriEnc(bookmark.page)}&redirect=true"
								   >remove</a>
								<div class='clear'></div>
							</div>
						</li>

					</c:forEach>

					<c:if test="${fn:length(bookmarks) == 0}">
						<div>
							You don't have any bookmarks. Try browsing <a href="/collections/">our collections</a>.
						</div>
					</c:if>
				</ol>
			</div>
		</div>
	</div>
</cudl:generic-page>
