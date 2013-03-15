<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<% 
	String queryString = request.getParameter("queryString");
	queryString = (queryString == null)? "" : queryString;
	String checkedOption = request.getParameter("checkedOption");
	
	String authorChecked = (checkedOption == null || checkedOption.equals("AUTHOR"))? " checked" : "";
	String keywordChecked = (checkedOption == null || checkedOption.equals("KEYWORD"))? " checked" : "";
	String classmarkChecked = (checkedOption == null || checkedOption.equals("CLASSMARK"))? " checked" : "";
%>

<div id="searchControls">
	<form action="genizah" id="searchForm">
		Query : <input type="text" name="query" value="<%= queryString %>"/><br/>
		<input type="radio" name="queryType" value="AUTHOR" <%= authorChecked %>/> Author<br/>
		<input type="radio" name="queryType" value="KEYWORD" <%= keywordChecked %>/> Keyword<br/>
		<input type="radio" name="queryType" value="CLASSMARK" <%= classmarkChecked %>/>Classmark<br/>
		<input type="submit" value="submit"/>
	</form>

</div>