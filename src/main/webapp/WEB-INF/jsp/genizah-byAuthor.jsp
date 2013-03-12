<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,
				 java.net.URLEncoder,
				 ulcambridge.foundations.viewer.genizah.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<%
	List<AuthorBibliography> resultSet = 
		((List<AuthorBibliography>) request.getAttribute("titles"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
%>

<%!
	public String output(String value) { return (value == null)? "?" : value; }
%>

<div class="clear"></div>

<section id="content" class="grid_20 content">
	<div>

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table>");
				for (AuthorBibliography bibliography : resultSet) {
					String author = bibliography.getAuthor();
					for (BibliographyEntry bibliographyEntry : bibliography.getBibliography()) {
						out.println("<tr>");
						out.println("<td>" + author + "</td>");
						out.println("<td>" + output(bibliographyEntry.getTitle()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getDate()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getDoi()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getEdition()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getNumber()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getPublisher()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getYear()) + "</td>");
						out.println("</tr>");
					}
				}
				out.println("</table>");
			}
		%>
</div>
</section>

<jsp:include page="footer/footer.jsp" />



