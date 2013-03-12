<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,
				 java.net.URLEncoder,
				 ulcambridge.foundations.viewer.genizah.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<link rel="stylesheet" href="/styles/genizah.css"/>

<%
	List<AuthorBibliography> resultSet = 
		((List<AuthorBibliography>) request.getAttribute("titles"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
%>

<%!
	public String output(String value) {
		return output(value, (value == null)? 0 : value.length());
	}

	public String output(String value, int maxLen) { 
		return (value == null)? "?" : 
			(value.length() > maxLen)? 
					value.substring(0, maxLen) + "..." : value; 
	}
%>

<script type="text/javascript">
	var numResults = <%=resultSet.size()%>;
	var table = $('table tr');
	var Paging = $(".pagination").paging(
			numResults,
			perpage : 10,
			lapping : 0,
			format : "[< (q-) ncnnnnnn (-p) >]",
			onFormat : function(type) {
				switch (type) {
					case 'block': return '<a>' + this.value + '</a>';
					case 'next': return '<a>&gt;</a>';
					case 'prev': return '<a>&lt;</a>';
					case 'first': return '<a>|&lt;</a>';
					case 'last': return '<a>&gt;|</a>';
					default: return '';
				}
			});
					
	);
</script>

<div class="clear"></div>

<jsp:include page="genizah-Search.jsp">
	<jsp:param name="checkedOption" value="AUTHOR"/>
</jsp:include>

<section id="content" class="grid_20 content">
	<div class="pagination"></div>
	<div>

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Authors</th>");
				out.println("<th>Title</th>");
				out.println("<th>Year</th>");
				out.println("<th>Publisher</th>");
				out.println("<th>Number</th>");
				out.println("<th>Date</th>");
				out.println("<th>DOI</th>");
				out.println("<th>Edition</th>");
				for (AuthorBibliography bibliography : resultSet) {
					String searchAuthor = bibliography.getSearchAuthor();
					for (BibliographyEntry bibliographyEntry : bibliography.getBibliography()) {
						out.println("<tr>");
						List<String> authors = bibliographyEntry.getAuthors();
						out.println("<td>");
						for (int authorIndex = 0; authorIndex < authors.size(); authorIndex++) {
							String author = authors.get(authorIndex);
							if (author.equals(searchAuthor)) {
								out.println("<span class=\"searchTermHighlight\">" + author + "</span>");
							} else {
								out.println(author);
							}
							if (authorIndex < authors.size() - 1) {
								out.println(",");
							}
						}
						out.println("</td>");
						out.println("<td>" + output(bibliographyEntry.getTitle(), 45) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getYear()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getPublisher(), 25) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getNumber()) + "</td>");
						out.println("<td>" + output(bibliographyEntry.getDate()) + "</td>");
						String doi = bibliographyEntry.getDoi();
						if (doi != null) {
							out.println("<td><a href=\"" + doi + "\">DOI</a></td>");
						} else {
							out.println("<td>?</td>");
						}
						out.println("<td>" + output(bibliographyEntry.getEdition()) + "</td>");
						out.println("</tr>");
					}
				}
				out.println("</table>");
			}
		%>
	</div>
</section>

<jsp:include page="footer/footer.jsp" />



