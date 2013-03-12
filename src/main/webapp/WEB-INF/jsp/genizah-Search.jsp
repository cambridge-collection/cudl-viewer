<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<% 
	String queryString = request.getParameter("queryString");
	String checkedOption = request.getParameter("checkedOption"); 
%>

<div id="searchControls">
	<form action="genizah" id="searchForm">
		Query : <input type="text" name="query" value="<%=queryString%>"/><br/>
		<input type="radio" name="queryType" value="AUTHOR" 
				<%=checkedOption.equals("AUTHOR")? "checked" : "" %>/> Author<br/>
		<input type="radio" name="queryType" value="KEYWORD"
				<%=checkedOption.equals("KEYWORD")? "checked" : "" %>/> Keyword<br/>
		<input type="radio" name="queryType" value="CLASSMARK"
				<%=checkedOption.equals("CLASSMARK")? "checked" : "" %>/>Classmark<br/>
		<input type="submit" value="submit"/>
	</form>

</div>