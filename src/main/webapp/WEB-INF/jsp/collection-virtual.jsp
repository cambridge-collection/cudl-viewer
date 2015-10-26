<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
	<jsp:attribute name="pageData">
			<cudl:default-context>
    			<sec:authorize access="hasRole('ROLE_ADMIN')">
    				<json:property name="isAdmin" value="${true}"/>
    				<json:array name="editableAreas">
    					<json:object>
    						<json:property name="id" value="summaryDiv"/>
    						<json:property name="filename" value="${collection.summary}"/>
    					</json:object>
    					<json:object>
    						<json:property name="id" value="sponsorDiv"/>
    						<json:property name="filename" value="${collection.sponsors}"/>
    					</json:object>
    				</json:array>
    			</sec:authorize>
    		</cudl:default-context>
    	</jsp:attribute>
    	<jsp:body>
			<cudl:nav activeMenuIndex="${1}" displaySearch="true" title="Browse our collections"/>

			<div class="clear"></div>

			<div class="campl-row campl-content campl-recessed-content">
				<div class="campl-wrap clearfix">

					<div class="campl-column12  campl-main-content" id="content">
						<div id="summaryDiv" class="campl-content-container">
							<c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.summary}"/>
						</div>
						<div class="campl-content-container campl-column12">

							<ol id="collections_carousel">
								<c:forEach items="${collection.itemIds}" var="id" varStatus="loop">
									<c:set var="item" value="${cudlfn:getItem(itemFactory, id)}"/>

									<%-- FIXME: move this inline style into CSS and apply a class here --%>
									<c:choose>
										<c:when test="${item.thumbnailOrientation == 'portrait'}">
											<c:set var="imageDimensions" value="height: 100%"/>
										</c:when>
										<c:when test="${item.thumbnailOrientation == 'landscape'}">
											<c:set var="imageDimensions" value="width: 100%"/>
										</c:when>
									</c:choose>

									<li class="campl-column5">
										<div class="collections_carousel_item">
											<div class="virtual_collections_carousel_image_box campl-column6">
												<div class="collections_carousel_image" id="collections_carousel_item${loop.index + 1}">
													<a href="/view/${fn:escapeXml(item.id)}/1">
														<img src="${fn:escapeXml(item.thumbnailURL)}"
															 alt="${fn:escapeXml(item.id)}"
															 style="${fn:escapeXml(imageDimensions)}">
													</a>
												</div>
											</div>
											<div class='collections_carousel_text campl-column6'>
												<h5><c:out value="${item.title} (${item.shelfLocator})"/></h5>
												<c:out value="${item.abstractShort}"/> &hellip;
												<a href="/view/${fn:escapeXml(item.id)}/1">more</a>
											</div>
											<div class='clear'></div>
										</div>
									</li>
								</c:forEach>
							</ol>
						</div>

						<div id="sponsorDiv" class="campl-column12 campl-content-container">
							<c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.sponsors}" />
						</div>
					</div>
				</div>
			</div>
	</jsp:body>
</cudl:generic-page>
