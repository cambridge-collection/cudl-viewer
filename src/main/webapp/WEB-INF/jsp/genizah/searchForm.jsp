<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String queryString = request.getParameter("queryString");
	queryString = (queryString == null) ? "" : queryString;
	String checkedOption = request.getParameter("checkedOption");

	String authorChecked = (checkedOption == null || checkedOption
			.equals("AUTHOR")) ? " checked" : "";
	String keywordChecked = (checkedOption == null || checkedOption
			.equals("KEYWORD")) ? " checked" : "";
	String classmarkChecked = (checkedOption == null || checkedOption
			.equals("CLASSMARK")) ? " checked" : "";
%>
<br />
<div>
	<div class="searchform box grid_9" id="searchControls">
	<h4>Bibliography Search</h4>
		<form action="genizah" id="searchForm">
			<div class="grid_5">
				<input class="search" type="text" name="query"
					value="<%=queryString%>" /> 
				<input id="submit" type="submit"
					value="submit" />
			</div>
			<div class="grid_3 right">
				<input type="radio" name="queryType" value="AUTHOR"
					<%=authorChecked%> /> Author<br /> <input type="radio"
					name="queryType" value="KEYWORD" <%=keywordChecked%> /> Keyword<br />
				<input type="radio" name="queryType" value="CLASSMARK"
					<%=classmarkChecked%> /> Classmark<br />
			</div>

		</form>
	</div>
</div>