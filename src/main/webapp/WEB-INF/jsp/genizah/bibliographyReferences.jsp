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
				"sPaginationType": "full_numbers",
				"aoColumnDefs" : [{"sType" : "classmark", "aTargets" : [0]}]
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
				out.println("<div class=\"grid_10 bibEntryDetails\">");
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
				
				List<String> editors = entry.getEditors();
				if (editors.size() > 0) {
					out.println("<dt>Editors</dt><dd>");
					for (int editorIndex = 0; editorIndex < editors.size(); editorIndex++) {
						String editor = editors.get(editorIndex);
						out.println(editor);
						if (editorIndex < editors.size() - 1) {
							out.println(";");
						}
					}
					out.println("</dd>");
				}
				
				// XXX - 'type' field is not currently useful!
				//out.println("<dt>Type</dt><dd>" + entry.getType() + "</dd>");
				
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
				if (entry.getPublisher() != null) {
					out.println("<dt>Publisher</dt><dd>" + entry.getPublisher() + "</dd>");
				}
				if (entry.getDate() != null) {
					out.println("<dt>Date</dt><dd>" + entry.getDate() + "</dd>");
				}
				if (entry.getPlacePublished() != null) {
					out.println("<dt>Place Published</dt><dd>" + entry.getPlacePublished() + "</dd>");
				}
				if (entry.getDoi() != null) {
					out.println("<dt>DOI</dt><dd>" + entry.getDoi() + "</dd>");
				}
				if (entry.getNumberVols() != null) {
					out.println("<dt>Number of Vols</dt><dd>" + entry.getNumberVols() + "</dd>");
				}
				if (entry.getOriginalPublisher() != null) {
					out.println("<dt>Original Publisher</dt><dd>" + entry.getOriginalPublisher() + "</dd>");
				}
				if (entry.getResearchNotes() != null) {
					out.println("<dt>Research Notes</dt><dd>" + entry.getResearchNotes() + "</dd>");
				}
				if (entry.getReprintEdition() != null) {
					out.println("<dt>Reprint Edition</dt><dd>" + entry.getReprintEdition() + "</dd>");
				}
				if (entry.getIsbn() != null) {
					out.println("<dt>ISBN</dt><dd>" + entry.getIsbn() + "</dd>");
				}
				
				/* XXX - short title is almost always identical to title!
				if (entry.getShortTitle() != null) {
					out.println("<dt>Short Title</dt><dd>" + entry.getShortTitle() + "</dd>");
				}
				*/
				
				if (entry.getSvCol() != null) {
					out.println("<dt>SV?</dt><dd>" + entry.getSvCol() + "</dd>");
				}
				if (entry.getSecondaryTitle() != null) {
					out.println("<dt>Secondary Title</dt><dd>" + entry.getSecondaryTitle() + "</dd>");
				}
				if (entry.getTranslatedTitle() != null) {
					out.println("<dt>Translated Title</dt><dd>" + entry.getTranslatedTitle() + "</dd>");
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



