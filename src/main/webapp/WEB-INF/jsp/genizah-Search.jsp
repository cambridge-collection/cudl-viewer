<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<% String checkedOption = request.getParameter("checkedOption"); %>

<div id="searchControls">
	<!--   <b><%=checkedOption %></b> -->
	<form action="genizah" id="searchForm">
		Query : <input type="text" name="query"/><br/>
		<input type="radio" name="queryType" value="AUTHOR" 
				<%=checkedOption.equals("AUTHOR")? "checked" : "" %>/> Author<br/>
		<input type="radio" name="queryType" value="KEYWORD"
				<%=checkedOption.equals("KEYWORD")? "checked" : "" %>/> Keyword<br/>
		<input type="radio" name="queryType" value="CLASSMARK"
				<%=checkedOption.equals("CLASSMARK")? "checked" : "" %>/>Classmark<br/>
		<input type="submit" value="submit"/>
	</form>

</div>