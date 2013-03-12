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
				out.println("<th>Author</th>");
				out.println("<th>Title</th>");
				out.println("<th>Year</th>");
				out.println("<th>Publisher</th>");
				out.println("<th>Number</th>");
				out.println("<th>Date</th>");
				out.println("<th>DOI</th>");
				out.println("<th>Edition</th>");
				for (AuthorBibliography bibliography : resultSet) {
					String author = bibliography.getAuthor();
					for (BibliographyEntry bibliographyEntry : bibliography.getBibliography()) {
						out.println("<tr>");
						out.println("<td>" + author + "</td>");
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



