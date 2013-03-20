<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,ulcambridge.foundations.viewer.search.*,ulcambridge.foundations.viewer.model.Item,ulcambridge.foundations.viewer.ItemFactory"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<div class="clear"></div>

<section id="content" class="grid_20 content">
	<h4 style="margin-left: 8px">Advanced Search</h4>

	<div class="grid_18">
		<div class="advancedsearchform box">

			<form:form commandName="searchForm" class="grid_17" method="GET" action="/search/advanced/results">

                <div class="grid_17">
                    <form:label path="keyword">Keywords</form:label>
					<form:input path="keyword" size="45" type="text" value=""
						name="keyword" placeholder="Search"/>
						<br /><br />
                </div>
				<div class="grid_9">
				    
                    <form:label path="fullText">Full Text</form:label>
					<form:input path="fullText" type="text" size="45" value="" name="fullText" /> 

					<form:label path="excludeText"> excluding </form:label>
					<form:input path="excludeText" type="text" size="35" value="" name="excludeText" /> 
					<br /><br />
				</div>
				<div class="grid_6">
					<form:radiobutton path="textJoin" class="search" type="radio" value="and" name="textJoin" /> 
				    All of these words<br /> 
					<form:radiobutton path="textJoin" class="search" type="radio" value="or" name="textJoin" />
					Any of these words 
					<br /><br />
				</div>
				<div class="grid_17">
				

					<form:label path="fileID">File ID</form:label>
					<form:input path="fileID" type="text" size="35" value="" name="fileID" /> <br />
					<br />					
					<h5>Metadata</h5>
					  <form:label path="title">Title</form:label>
					  <form:input path="title" type="text" value="" name="title" />
					  <br /> 
					  <form:label path="author">Author</form:label>
					  <form:input path="author" type="text" value="" name="author" />
					  <br /> 
					  <form:label path="subject">Subject</form:label>
					  <form:input path="subject" type="text" value="" name="subject" />
					  <br /> 
					  <form:label path="yearStart">Year</form:label>
					  <form:input path="yearStart" type="text" value="" name="yearStart" /> 
					  <form:label path="yearEnd"> to </form:label>
					  <form:input path="yearEnd" type="text" value="" name="yearEnd" />
					  <br /><br /> 
					  <input id="submit" type="submit" value="Submit" />  					  
					  <input id="reset" type="reset" value="Reset" /> 
					  <a href="/search"	class="altsearchlink">back to simple search&nbsp;</a>
				</div>
			</form:form>

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



