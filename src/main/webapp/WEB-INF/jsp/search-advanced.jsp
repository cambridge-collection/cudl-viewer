<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="ulcambridge.foundations.viewer.forms.*,
org.owasp.encoder.Encode"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Advanced Search" />
</jsp:include>

<link rel="stylesheet" href="/styles/hint.min.css">

<!-- advanced search css -->
<link rel="stylesheet" href="/styles/advancedsearch.css">

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column12  campl-main-content" id="content">
			<div class="campl-content-container">

				<%
					SearchForm form = (SearchForm) request.getAttribute("form");
				%>

				<div class="grid_18">
					<div class="advancedsearchform box">

						<form:form commandName="searchForm" method="GET" action="/search/advanced/results">

							<div class="advancedsearch-section campl-column6 clearfix">
								<h2>Find metadata with ...</h2>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="keyword">Keywords</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Search keywords in metadata or transcriptions">
											<form:input path="keyword" type="text" value="" name="keyword" />
										</span>

                                        <c:if test="${enableTagging}">
                                            <div class="recall-slider">
                                                <input id="recall-slider-input" type="text" name="recallScale"
                                                        data-slider-value="${fn:escapeXml(form.recallScale)}"
                                                        data-slider-min="0"
                                                        data-slider-max="1"
                                                        data-slider-step="0.1"
                                                        data-slider-ticks="[0, 0.5, 1]"
                                                        data-slider-ticks-labels='["Curated<br>metadata", "Secondary<br>literature", "Crowd-<br>sourced"]'
                                                        data-slider-tooltip="hide">
                                                <input type="hidden" name="tagging" value="1">
                                            </div>
                                        </c:if>
									</div>
								</div>
	
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="fullText">Full Text</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Search transcription data">
											<form:input path="fullText" type="text" value="" name="fullText" />
										</span>
									</div>
									<!-- <div class="campl-column12"> -->
										<div class="campl-column10 excludetext">
											<span class="hint--right" data-hint="Exclude transcription results that mention these words" style="display:table;">
												<div style="display:table-cell;width:60px;padding-right:6px;">
													<form:label path="excludeText"> excluding </form:label>
												</div>
												<div style="display:table-cell;">
													<form:input path="excludeText" type="text" value="" name="excludeText" />
												</div>
											</span>
										</div>
									<!-- </div> -->
									<div class="campl-column10" style="float:right;">
										<span class="hint--right" data-hint="Applies to full text search">
											<div style="float:right;">
												<form:radiobutton path="textJoin" class="radiobutton" value="and" name="textJoin" id="textJoin1" /> 
												<form:label path="textJoin" class="_rbl" for="textJoin1"> All of these words </form:label>
												<form:radiobutton path="textJoin" class="radiobutton" value="or" name="textJoin" id="textJoin2"/> 
												<form:label path="textJoin" class="_rbl" for="textJoin2"> Any of these words </form:label>
											</div> 
										</span>
									</div>
								</div>
							</div>

							<div class="_mib"></div>
							
							<div class="advancedsearch-section campl-column6 clearfix">						
								<h2>Narrow your results by ...</h2>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="FacetCollection">Collection</form:label>
									</div>
									<div class="campl-column10">	
									   								     
										<form:select path="FacetCollection">
										<form:option value="" label="--- Select ---"/>
										<form:options items="<%=form.getCollections() %>" itemValue="title" itemLabel="title" multiple="false"/>
										</form:select> 											
									</div>
								</div>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="shelfLocator">Classmark</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="e.g. MS Add.3996">
											<form:input path="shelfLocator" type="text" size="35" value="" name="shelfLocator" />
										</span>
									</div>
								</div>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="title">Title</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Search for titles that includes these words, e.g. Letter">
											<form:input path="title" type="text" size="35" value="" name="title" />
										</span>
									</div>
								</div>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="author">Author</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Search for items by this person, e.g. Darwin">
											<form:input path="author" type="text" size="35" value="" name="author" />
										</span>
									</div>
								</div>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="subject">Subject</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Search for items about this subject, e.g. Mathematics">
											<form:input path="subject" type="text" size="35" value="" name="subject" />
										</span>
									</div>
								</div>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="location">Location</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Search for items related to a specific place, e.g. London">
											<form:input path="location" type="text" size="35" value="" name="location" />
										</span>
									</div>
								</div>
								
								<div class="campl-column12">
									<div class="campl-column2">
										<form:label class="right" path="yearStart">Year</form:label>
									</div>
									<div class="campl-column10">
										<span class="hint--right" data-hint="Limit results to this range of years" style="display:table;"> 
											<div style="display:table-cell;">
												<form:input path="yearStart" type="text" value="" name="yearStart" />
											</div> 
											<div style="display:table-cell;padding-right:6px;padding-left:6px;text-align:center;width:40px;">
												<form:label path="yearEnd"> to </form:label>
											</div>
											<div style="display:table-cell;"> 
												<form:input path="yearEnd" type="text" value="" name="yearEnd" />
											</div>
										</span>
									</div>
								</div>
								
								<div class="campl-column12" style="margin: 20px 0 0 0;">
									<div class="grid_16" style="float:right;">
										<button type="reset" class="campl-btn">Reset</button>									
										<button type="submit" class="campl-btn campl-primary-cta">Advanced Search</button>										
									</div>
								</div>
								
							</div>
						</form:form>

						<div class="grid_16" style="color: #999">
							<br />The characters <b>?</b> and <b>*</b> can be used as
							wildcards in your search.<br /> Use <b>?</b> to represent one
							unknown character and <b>*</b> to represent any number of unknown
							characters.
						</div>

						<div class="altsearchlink grid_4 right">
							<form:form commandName="searchForm" action="/search" method="GET">
								<input type="hidden" value="<%=Encode.forHtmlAttribute(form.getKeyword())%>" name="keyword" />
								<input class="altsearchlink" type="submit" value="back to simple search" />
							</form:form>
						</div>


					</div>
				</div>

				<!-- <div class="grid_13 container" id="pagination_container">
					<div class="pagination toppagination"></div>
					<div id="collections_carousel" class="collections_carousel"></div>
					<div class="pagination toppagination"></div>
				</div> -->

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function(){
	    $('#recall-slider-input').slider();

		// change hint direction
		if ($(window).width() > 767) {
			$('span.hint--bottom').addClass('hint--right');
			$('span.hint--bottom').removeClass('hint--bottom');
		} else {
			$('span.hint--right').addClass('hint--bottom');
			$('span.hint--right').removeClass('hint--right');
		}
	});
	
	$(window).resize(function() {
		// change hint direction
		if ($(window).width() > 767) {
			$('span.hint--bottom').addClass('hint--right');
			$('span.hint--bottom').removeClass('hint--bottom');
		} else {
			$('span.hint--right').addClass('hint--bottom');
			$('span.hint--right').removeClass('hint--right');
		}
	});
	
</script>

<jsp:include page="header/footer-full.jsp" />
