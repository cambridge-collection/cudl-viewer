<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_GB"/>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="ADVANCED_SEARCH_RESULTS" title="Advanced Search">
	<jsp:attribute name="pageData">
		<cudl:default-context>
			<json:property name="resultCount" value="${results.numberOfResults}"/>
			<json:property name="queryString" value="${queryString}"/>
		</cudl:default-context>
	</jsp:attribute>

	<jsp:body>
		<cudl:nav activeMenuIndex="${2}" displaySearch="true" title="Advanced Search"/>

		<div class="campl-row campl-content campl-recessed-content">
			<div class="campl-wrap clearfix">
				<div class="campl-main-content" id="content">
					<div class="campl-column4 campl-secondary-content">

						<div class="searchform box">

							<div class="campl-content-container">
								<ul>
									<cudl:search-result-param form="${form}" label="Keyword" attr="keyword"/>
									<cudl:search-result-param form="${form}" label="Full Text" attr="fullText"/>
									<cudl:search-result-param form="${form}" label="Exclude Text" attr="excludeText"/>
									<cudl:search-result-param form="${form}" label="Classmark" attr="shelfLocator"/>
									<cudl:search-result-param form="${form}" label="CUDL ID" attr="fileID"/>
									<cudl:search-result-param form="${form}" label="Title" attr="title"/>
									<cudl:search-result-param form="${form}" label="Subject" attr="subject"/>
									<cudl:search-result-param form="${form}" label="Location" attr="location"/>
									<c:if test="${not (empty form.yearStart or empty form.yearEnd)}">
										<li>
											<span>Year: <b><c:out value="${form.yearStart}"/></b> to <b><c:out value="${form.yearEnd}"/></b></span>
										</li>
									</c:if>
								</ul>

								<form class="grid_5" action="/search">
									<a href="/search/advanced/query?${fn:escapeXml(queryString)}">
										Change Query
									</a>
									<input type="hidden" name="fileID" value="${fn:escapeXml(form.fileID)}>">
								</form>

								<c:forEach items="${form.facets}" var="facet">
									<div class="search-facet-selected">
										<a class="search-close" href="?${fn:escapeXml(cudlfn:urlParamsWithoutFacet(form, facetName))}&amp;" title="Remove">
											in <b><c:out value="${facet.value}"/></b> ( <c:out value="${facetName}"/>) &cross;
										</a>
									</div>
								</c:forEach>

								<c:if test="${not empty results.spellingSuggestedTerm}">
									Did you mean
									<a href="/search?keyword=${cudlfn:uriEnc(results.spellingSuggestedTerm)}">
										<c:out value="${results.spellingSuggestedTerm}"/>
									</a>
								</c:if>

								<div class="_mib"></div>
								<div class="campl-column12 resultcount">
									About <fmt:formatNumber value="${results.numberOfResults}"/> results (<span id="reqtime"></span></span>)
								</div>
							</div>

							<c:if test="${results.numberOfResults > 0}">
								<div class="campl-content-container">
									<h5>Refine by:</h5>

									<ol id="tree" class="campl-unstyled-list">
										<c:forEach items="${results.facets}" var="facetGroup">
											<%-- Do not show a facet for a field we're already faceting on --%>
											<c:if test="${empty form.facets[facetGroup.field]}">
												<li>
													<%-- FIXME: Add these arrows in CSS instead --%>
													<strong>
														▾ <c:out value="${facetGroup.fieldLabel}"/>
													</strong>
													<ul class="campl-unstyled-list">
														<c:forEach items="${facetGroup.facets}" var="facet">
															<li>
																<a href="?${fn:escapeXml(cudlfn:urlParamsWithFacet(form, facetGroup.field, facet.band))}">
																	<c:out value="${facet.band}"/>
																</a>
																(<c:out value="${facet.occurences}"/>)
															</li>
														</c:forEach>
													</ul>
												</li>
											</c:if>
										</c:forEach>
									</ol>
								</div>
							</c:if>
						</div>
					</div>

					<div class="campl-column8 camp-content">
						<c:if test="${empty results.results}">
							<div class="searchexample campl-content-container">
								<p class="box">We couldn't find any items your query.</p>

								<h5>Example Searches</h5>
								<br>
								<p>
									Searching for <span class="search">newton</span> Searches the metadata for 'newton'<br>
									Searching for <span class="search">isaac newton</span> Searches for 'isaac' AND 'newton'<br>
									Searching for <span class="search">"isaac newton"</span> Searches for the phrase 'isaac newton'<br>
									The characters <b>?</b> and <b>*</b> can be used as wildcards in your search.<br>
									Use <b>?</b> to represent one unknown character and <b>*</b> to represent any number of unknown characters.<br/>
								</p>
							</div>
						</c:if>


						<!-- start of list -->
						<div id="collections_carousel" class="collections_carousel">
						</div>
						<!-- end of list -->
						<div class="pagination toppagination"></div>
					</div>
				</div>
			</div>
		</div>
	</jsp:body>
</cudl:generic-page>
