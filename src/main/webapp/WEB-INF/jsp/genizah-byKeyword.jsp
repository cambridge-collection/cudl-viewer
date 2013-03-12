<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,ulcambridge.foundations.viewer.genizah.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<link rel="stylesheet" href="/styles/genizah.css"/>

<%
	List<BibliographyEntry> resultSet = ((List<BibliographyEntry>) request.getAttribute("titles"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
%>

<div class="clear"></div>
<jsp:include page="genizah-Search.jsp">
	<jsp:param name="checkedOption" value="KEYWORD"/>
</jsp:include>

<section id="content" class="grid_20 content">
		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table>");
				String queryString = query.getQueryString();
				for (BibliographyEntry bibliographyEntry : resultSet) {
					out.println("<tr>");
					List<String> authors = bibliographyEntry.getAuthors();
					out.println("<td>");
					for (String author : authors) {
						out.println(author + ",");
					}
					out.println("</td>");
					String title = bibliographyEntry.getTitle();
					int subStringIndex = title.indexOf(queryString);
					if (subStringIndex > -1) {
						out.println("<td>" + title.substring(0, subStringIndex));
						out.println("<span class=\"searchTermHighlight\">" + queryString + "</span>");
						out.println(title.substring(subStringIndex + queryString.length()) + "</td>");
					} else {
						out.println("<td>" + title + "</td>");	// essentially, a bug...
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}
		%>
</section>

<jsp:include page="footer/footer.jsp" />



