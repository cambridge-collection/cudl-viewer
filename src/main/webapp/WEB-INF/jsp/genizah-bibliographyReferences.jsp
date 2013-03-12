<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,
				 java.net.URLEncoder,
				 ulcambridge.foundations.viewer.ItemFactory,
				 ulcambridge.foundations.viewer.JSONReader,
				 ulcambridge.foundations.viewer.dao.*,
				 ulcambridge.foundations.viewer.model.Item,
				 ulcambridge.foundations.viewer.genizah.*
				 "%>

<jsp:include page="header/genizah-header.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#referenceTable').dataTable( {
				"sPaginationType": "full_numbers"
		} );
	} );
</script>
</head>
<jsp:include page="genizah-bodyStart.jsp" />
<jsp:include page="header/nav-search.jsp" />


<%
	List<BibliographyReferences> resultSet = ((List<BibliographyReferences>) request.getAttribute("bibliographyReferences"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
	ItemFactory itemFactory = (ItemFactory) request.getAttribute("itemFactory");
%>

<!--   <div class="clear"></div> -->

	<jsp:include page="genizah-Search.jsp">
		<jsp:param name="queryString" value="<%=query.getQueryString()%>"/>
		<jsp:param name="checkedOption" value="CLASSMARK"/>
	</jsp:include>

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any bibliography entries matching "
						+ query.getQueryString() + "</b></p>");
			} else {
				String fragmentBaseURL = "http://cudl.lib.cam.ac.uk/view/";
				out.println("<table id=\"referenceTable\">");
				out.println("<thead><tr>");
				out.println("<th>Reference Title</th>");
				out.println("<th>Year</th>");
				out.println("<th>Volume</th>");
				out.println("<th>References</th>");
				out.println("<th>Authors</th>");
				out.println("</tr></thead><tbody>");
				for (BibliographyReferences bibliographyReference : resultSet) {
					BibliographyEntry bibliographyEntry = bibliographyReference.getBibligraphyEntry();
					out.println("<tr>");
					out.println("<td>" + bibliographyEntry.getTitle() + "</td>");
					out.println("<td>" + bibliographyEntry.getYear() + "</td>");
					out.println("<td>" + bibliographyEntry.getVolume() + "</td>");
					out.println("<td>" + bibliographyReference.getBibliographyReferences().size() +"</td>");
					out.println("<td>");
					List<String> authors = bibliographyEntry.getAuthors();
					for (int authorIndex = 0; authorIndex < authors.size(); authorIndex++) {
						String author = authors.get(authorIndex);
						out.println(author);
						if (authorIndex < authors.size() - 1) {
							out.println(":");
						}
					}
				}
				out.println("</td>");
				out.println("</tr>");
				out.println("</tbody></table>");
			}
		%>
<jsp:include page="footer/footer.jsp" />



