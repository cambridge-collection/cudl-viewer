<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,
				 java.net.URLEncoder,
				 ulcambridge.foundations.viewer.genizah.*"%>

<jsp:include page="header.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#authorsTable').dataTable( {
				"sPaginationType": "full_numbers"
		} );
	} );
</script>
</head>
<jsp:include page="bodyStart.jsp" />

<%
	List<BibliographySearchResult> titles = 
		((List<BibliographySearchResult>) request.getAttribute("titles"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
%>

<div class="clear"></div>

<jsp:include page="searchForm.jsp">
	<jsp:param name="queryString" value="<%=query.getQueryString()%>"/>
	<jsp:param name="checkedOption" value="<%=query.getQueryType()%>"/>
</jsp:include>

<section id="content" class="grid_20 content">
	<div class="pagination"></div>
	<div>

		<%
			// No results were returned. So print out some help.
			if (titles.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any authors matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table id=\"authorsTable\">");
				out.println("<thead><tr>");
				out.println("<th>Authors</th>");
				out.println("<th>Title</th>");
				out.println("<th>Type</th>");
				out.println("<th>Year</th>");
				//out.println("<th>Publisher</th>");
				//out.println("<th>Number</th>");
				//out.println("<th>Date</th>");
				out.println("<th>Fragment Refs</th>");
				out.println("<th>Link to Refs</th>");
				out.println("</tr></thead><tbody>");
				for (BibliographySearchResult bibliographySearchResult : titles) {
					BibliographyEntry bibliographyEntry = bibliographySearchResult.getBibliographyEntry();
					out.println("<tr>");
					List<String> authors = bibliographyEntry.getAuthors();
					out.println("<td>");
					String searchAuthor = "";	// FIXME
					for (int authorIndex = 0; authorIndex < authors.size(); authorIndex++) {
						String author = authors.get(authorIndex);
						if (author.equals(searchAuthor)) {
							out.println("<span class=\"searchTermHighlight\">"
									+ author + "</span>");
						} else {
							out.println(author);
						}
						if (authorIndex < authors.size() - 1) {
							out.println(";");
						}
					}
					out.println("</td>");
					out.println("<td>" + bibliographyEntry.getTitle() + "</td>");
					out.println("<td>" + bibliographyEntry.getType() + "</td>");
					out.println("<td>" + bibliographyEntry.getYear() + "</td>");
					//out.println("<td>" + bibliographyEntry.getPublisher() + "</td>");
					//out.println("<td>" + bibliographyEntry.getNumber() + "</td>");
					//out.println("<td>" + bibliographyEntry.getDate() + "</td>");
					int refCount = bibliographySearchResult.getRefCount();
					out.println("<td>" + refCount + "</td>");
					int id = bibliographyEntry.getId();
					String url = "/genizah?query=" + id + "&queryType=TITLEID";
					out.println("<td><a href=\"" + url + "\">RefList</a></td>");
					out.println("</tr>");
				}
				out.println("</tbody></table>");
			}
		%>
	</div>
</section>

<jsp:include page="footer.jsp" />



