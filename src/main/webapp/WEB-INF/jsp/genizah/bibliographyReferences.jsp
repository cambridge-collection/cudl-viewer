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

<jsp:include page="header.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#referenceTable').dataTable( {
				"sPaginationType": "full_numbers"
		} );
	} );
</script>
</head>
<jsp:include page="bodyStart.jsp" />


<%
	BibliographyReferenceList bibRefList = ((BibliographyReferenceList) request.getAttribute("bibliographyReferences"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
	ItemFactory itemFactory = (ItemFactory) request.getAttribute("itemFactory");
%>

<div class="clear"></div>

	<jsp:include page="searchForm.jsp">
		<jsp:param name="queryString" value="<%=query.getQueryString()%>"/>
		<jsp:param name="checkedOption" value="CLASSMARK"/>
	</jsp:include>

		<%
			// No results were returned. So print out some help.
			if (bibRefList.getBibliographyReferences().size() == 0) {
				out.println("<p class=\"box\">We couldn't find any references for title with identifier "
						+ query.getQueryString() + "</b></p>");
			} else {
				String fragmentBaseURL = "http://cudl.lib.cam.ac.uk/view/";
				BibliographyEntry entry = bibRefList.getBibligraphyEntry();
				out.println("<div class=\"bibEntryDetails\">");
				out.println("Fragments referred to by :");
				out.println("<dl>");
				out.println("<dt>Title</dt><dd>" + entry.getTitle() + "</dd>");
				out.println("<dt>Authors</dt><dd>");
				List<String> authors = entry.getAuthors();
				for (int authorIndex = 0; authorIndex < authors.size(); authorIndex++) {
					String author = authors.get(authorIndex);
					out.println(author);
					if (authorIndex < authors.size() - 1) {
						out.println(";");
					}
				}
				out.println("</dd>");
				out.println("<dt>Type</dt><dd>" + entry.getType() + "</dd>");
				if (entry.getYear() != null) {
					out.println("<dt>Year</dt><dd>" + entry.getYear() + "</dd>");
				}
				if (entry.getVolume() != null) {
					out.println("<dt>Volume</dt><dd>" + entry.getVolume() + "</dd>");
				}
				if (entry.getEdition() != null) {
					out.println("<dt>Edition</dt><dd>" + entry.getEdition() + "</dd>");
				}
				if (entry.getNumber() != null) {
					out.println("<dt>Number</dt><dd>" + entry.getNumber() + "</dd>");
				}
				if (entry.getStartPage() != null) {
					out.println("<dt>Start Page</dt><dd>" + entry.getStartPage() + "</dd>");
				}
				out.println("</dl></div>");
				
				out.println("<table id=\"referenceTable\">");
				out.println("<thead><tr>");
				out.println("<th>Classmark</th>");
				out.println("<th>Reference Type</th>");
				out.println("<th>Reference Position</th>");
				out.println("</tr></thead><tbody>");
				for (BibliographyReferences reference : bibRefList.getBibliographyReferences()) {
					Fragment fragment = reference.getFragment();
					String classmark = fragment.getClassmark();
					String label = fragment.getLabel();
					
					out.println("<tr>");
					out.println("<td>" + fragment.getLabel() + "</td>");
					out.println("<td>" + reference.getTypeReadableForm() + "</td>");
					out.println("<td>" + reference.getPosition() + "</td>");
					out.println("</tr>");
				}
				out.println("</tbody></table>");
			}
		%>
<jsp:include page="footer.jsp" />



