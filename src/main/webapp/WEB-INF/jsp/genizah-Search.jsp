<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="searchControls">
	<form action="genizah" id="searchForm">
		Query : <input type="text" name="query"/><br/>
		<input type="radio" name="queryType" value="AUTHOR" checked>Author<br/>
		<input type="radio" name="queryType" value="KEYWORD"/>Keyword<br/>
		<input type="radio" name="queryType" value="CLASSMARK"/>Classmark<br/>
		<input type="submit" value="submit"/>
	</form>

</div>