<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="ulcambridge.foundations.viewer.forms.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Advanced Search" />
</jsp:include>

<link rel="stylesheet" href="/styles/hint.min.css">

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">
			<div class="campl-content-container">

				<%
					SearchForm form = (SearchForm) request.getAttribute("form");
				%>

				<h2>Advanced Search</h2>

				<div class="grid_18">
					<div class="advancedsearchform box">

						<form:form commandName="searchForm" method="GET"
							action="/search/advanced/results">

							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="keyword">Keywords</form:label>
								</div>
								<div class="campl-column8">
									<span class="hint--right"
										data-hint="Search keywords in metadata or transcriptions"><form:input
											path="keyword" style="width:400px" size="45" type="text" value="" name="keyword"
											placeholder="Search" /></span> <br /> <br />
								</div>
							</div>

							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="fullText">Full Text</form:label>
								</div>
								<div class="campl-column10">
									<span class="hint--right" data-hint="Search transcription data"><form:input
											path="fullText" type="text" size="45" style="width:400px" value=""
											name="fullText" /></span>
								</div>
								<div class="campl-column2">&nbsp;
								</div>
								<div class="campl-column5">			
									<form:label path="excludeText"> &nbsp; excluding </form:label>
									<span class="hint--right"
										data-hint="Exclude transcription results that mention these words"><form:input
											path="excludeText" type="text" size="35" style="width:268px" value=""
											name="excludeText" /></span> <br /> <br />
								</div>
								<div class="campl-column4">
									<span class="hint--right"
										data-hint="Applies to full text search"><form:radiobutton
											path="textJoin" class="radiobutton" type="radio" value="and"
											name="textJoin" /> All of these words<form:radiobutton
											path="textJoin" class="radiobutton" type="radio" value="or"
											name="textJoin" /> Any of these words </span><br /> <br />
								</div>
							</div>

							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="shelfLocator">Classmark</form:label>
								</div>
								<div class="campl-column8">

									<span class="hint--right" data-hint="e.g. MS Add.3996"><form:input
											path="shelfLocator" type="text" size="35" value=""
											name="shelfLocator" /></span> <br />
								</div>
							</div>


							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="title">Title</form:label>
								</div>
								<div class="campl-column8">
									<span class="hint--right"
										data-hint="Search for titles that includes these words, e.g. Letter"><form:input
											path="title" type="text" size="35" value="" name="title" /></span>
									<br />
								</div>
							</div>
							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="author">Author</form:label>
								</div>
								<div class="campl-column8">
									<span class="hint--right"
										data-hint="Search for items by this person, e.g. Darwin"><form:input
											path="author" type="text" size="35" value="" name="author" />
										<br /></span>
								</div>
							</div>
							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="subject">Subject</form:label>
								</div>
								<div class="campl-column8">
									<span class="hint--right"
										data-hint="Search for items about this subject, e.g. Mathematics"><form:input
											path="subject" type="text" size="35" value="" name="subject" /></span>
									<br />
								</div>
							</div>
							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="location">Location</form:label>
								</div>
								<div class="campl-column8">
									<span class="hint--right"
										data-hint="Search for items related to a specific place, e.g. London"><form:input
											path="location" type="text" size="35" value=""
											name="location" /></span> <br />
								</div>
							</div>
							<div class="campl-column12">
								<div class="campl-column1">
									<form:label class="right" path="yearStart">Year</form:label>
								</div>
								<div class="campl-column8">
									<span class="hint--right"
										data-hint="Limit results to this range of years"> <form:input
											path="yearStart" type="text" value="" name="yearStart" /> <form:label
											path="yearEnd"> to </form:label> <form:input path="yearEnd"
											type="text" value="" name="yearEnd" />
									</span> <br /> <br />
								</div>
							</div>
							<div class="campl-column12">
								<div class="grid_16">									
									<button type="reset" class="campl-btn">Reset</button>									
									<button type="submit" class="campl-btn campl-primary-cta">Submit</button>										
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

								<input type="hidden" value="<%=form.getKeyword()%>"
									name="keyword" />

								<input class="altsearchlink" type="submit"
									value="back to simple search" />

							</form:form>
							
						</div>


					</div>
				</div>

				<div class="grid_13 container" id="pagination_container">

					<div class="pagination toppagination"></div>
					<!-- start of list -->
					<div id="collections_carousel" class="collections_carousel"></div>
					<!-- end of list -->
					<div class="pagination toppagination"></div>

				</div>



			</div>
		</div>
	</div>
</div>


<jsp:include page="header/footer-full.jsp" />



