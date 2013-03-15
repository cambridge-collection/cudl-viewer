<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder,java.util.Enumeration"%>
<!DOCTYPE html>
<html>

<head>

<%
  // title can be specified in a param when you include this jsp. 
  if (request.getParameter("title")!=null) {
%>
  <title><%=request.getParameter("title")%></title>
<% } else { %>
  <title>Cambridge Digital Library - University of Cambridge</title>
<% } %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="icon" type="image/png" href="/img/favicon.png">

<!-- JQuery -->
<script type="text/javascript" src="/scripts/jquery-1.4.3.min.js"></script>
<script type="text/javascript" src="/scripts/dataTables/js/jquery.dataTables.min.js"></script>	

<link rel="stylesheet" href="/styles/uoc.min.css">
<link rel="stylesheet" type="text/css" href="/scripts/dataTables/css/jquery.dataTables.css" media="screen" />
<link rel="stylesheet" href="/styles/genizah.css" type="text/css"/>	

<%
	// If no javascript enabled, display no javascript page with link to homepage. 
	String redirectURL = "/";
	String encodedRedirectURL = URLEncoder.encode(redirectURL, "UTF-8");

	// ensure we don't loop when displaying the nojavascript page.
	if (!request.getRequestURI().toString().contains("nojavascript")) {
%>
<NOSCRIPT>
	<!--  no javascript redirect. -->
	<META HTTP-EQUIV="refresh"
		content="0; URL=/nojavascript?url=<%=encodedRedirectURL%>" />
</NOSCRIPT>
<%
	}
%>

<script type="text/javascript">
	// initalise page if function is available. 
	function init() {

		// Show or hide cookie notice and call page specific init statement.
		cudl.displayCookieNotice(); 
		if (typeof pageinit == 'function') {
			pageinit();
		}
	}
</script>
