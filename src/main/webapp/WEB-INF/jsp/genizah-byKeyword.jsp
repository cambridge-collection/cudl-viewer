<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,ulcambridge.foundations.viewer.genizah.*"%>

<jsp:include page="header/genizah-header.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#keywordsTable').dataTable( {
				"sPaginationType": "full_numbers"
		} );
	} );
</script>
</head>
<jsp:include page="genizah-bodyStart.jsp" />
<jsp:include page="header/nav-search.jsp" />

<%
	List<BibliographyEntry> resultSet = ((List<BibliographyEntry>) request.getAttribute("titles"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
%>

<jsp:include page="genizah-Search.jsp">
	<jsp:param name="queryString" value="<%=query.getQueryString()%>"/>
	<jsp:param name="checkedOption" value="KEYWORD"/>
</jsp:include>

<section id="content" class="grid_20 content">
		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any titles containing <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table id=\"keywordsTable\">");
				out.println("<thead><tr>");
				out.println("<th>Authors</th>");
				out.println("<th>Title</th>");
				out.println("<th>Year</th>");
				out.println("</tr></thead><tbody>");
				String queryString = query.getQueryString();
				for (BibliographyEntry bibliographyEntry : resultSet) {
					out.println("<tr>");
					List<String> authors = bibliographyEntry.getAuthors();
					out.println("<td>");
					for (int authorIndex = 0; authorIndex < authors.size(); authorIndex++) {
						if (authorIndex < authors.size() - 1) {
							out.println(authors.get(authorIndex) + ",");
						} else {
							out.println(authors.get(authorIndex));
						}
					}
					out.println("</td>");
					String title = bibliographyEntry.getTitle();
					int subStringIndex = title.toLowerCase().indexOf(queryString.toLowerCase());
					if (subStringIndex > -1) {
						out.print("<td>" + title.substring(0, subStringIndex));
						//out.println("<span class=\"searchTermHighlight\">" + queryString + "</span>");
						out.print("<span class=\"searchTermHighlight\">" + queryString + "</span>");
						out.println(title.substring(subStringIndex + queryString.length()) + "</td>");
					} else {
						out.println("<td>" + title + "</td>");	// essentially, a bug...
					}
					out.println("<td>" + bibliographyEntry.getYear() + "</td>");
					out.println("</tr>");
				}
				out.println("</tbody></table>");
			}
		%>
</section>

<jsp:include page="footer/footer.jsp" />



