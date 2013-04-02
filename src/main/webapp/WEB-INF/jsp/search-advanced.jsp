<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="ulcambridge.foundations.viewer.forms.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<div class="clear"></div>
<%
	SearchForm form = (SearchForm) request.getAttribute("form");
%>

<section id="content" class="grid_20 content">
	<h4 style="margin-left: 8px">Advanced Search</h4>

	<div class="grid_18">
		<div class="advancedsearchform box">

			<form:form commandName="searchForm" class="grid_17" method="GET"
				action="/search/advanced/results">

				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="keyword">Keywords</form:label>
					</div>
					<div class="grid_13">
						<form:input path="keyword" size="45" type="text" value=""
							name="keyword" placeholder="Search" />
						<br /> <br />
					</div>
				</div>

				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="fullText">Full Text</form:label>
					</div>
					<div class="grid_8">
						<form:input path="fullText" type="text" size="45" value=""
							name="fullText" />
						<br />
						<form:label path="excludeText"> &nbsp; excluding </form:label>
						<form:input path="excludeText" type="text" size="35" value=""
							name="excludeText" />
						<br /> <br />
					</div>
					<div class="grid_6">
						<form:radiobutton path="textJoin" class="radiobutton" type="radio"
							value="and" name="textJoin" />
						All of these words<br />
						<form:radiobutton path="textJoin" class="radiobutton" type="radio"
							value="or" name="textJoin" />
						Any of these words <br /> <br />
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="fileID">CUDL ID</form:label>
					</div>
					<div class="grid_14">

						<form:input path="fileID" type="text" size="35" value=""
							name="fileID" />
						<br /> <br />
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_16">
						<h5>Metadata</h5><br/>
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="title">Title</form:label>
					</div>
					<div class="grid_14">
						<form:input path="title" type="text" value="" name="title" />
						<br />
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="author">Author</form:label>
					</div>
					<div class="grid_14">
						<form:input path="author" type="text" value="" name="author" />
						<br />
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="subject">Subject</form:label>
					</div>
					<div class="grid_14">
						<form:input path="subject" type="text" value="" name="subject" />
						<br />
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_2">
						<form:label class="right" path="yearStart">Year</form:label>
					</div>
					<div class="grid_14">
						<form:input path="yearStart" type="text" value="" name="yearStart" />
						<form:label path="yearEnd"> to </form:label>
						<form:input path="yearEnd" type="text" value="" name="yearEnd" />
						<br /> <br />
					</div>
				</div>
				<div class="grid_17">
					<div class="grid_16">
						<input id="submit" type="submit" value="Submit" /> <input
							id="reset" type="reset" value="Reset" />
					</div>

				</div>
			</form:form>

			<div class="altsearchlink grid_17">
				<form:form commandName="searchForm" action="/search" method="GET">

					<input type="hidden" value="<%=form.getKeyword()%>" name="keyword" />

					<input class="altsearchlink" type="submit"
						value="back to simple search" />

					<br />
					<br />
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



</section>


<jsp:include page="footer/footer.jsp" />



