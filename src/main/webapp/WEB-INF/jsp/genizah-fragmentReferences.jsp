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
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		$("#referenceTable").dataTable();
	} );
</script>


<!--   <link rel="stylesheet" href="/styles/genizah.css"/> -->

<%
	List<FragmentBibliography> resultSet = ((List<FragmentBibliography>) request.getAttribute("fragmentReferences"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
	ItemFactory itemFactory = (ItemFactory) request.getAttribute("itemFactory");
%>

<!--   <div class="clear"></div> -->
<section id="content" class="grid_20 content">

	<jsp:include page="genizah-Search.jsp">
		<jsp:param name="queryString" value="<%=query.getQueryString()%>"/>
		<jsp:param name="checkedOption" value="CLASSMARK"/>
	</jsp:include>

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				String fragmentBaseURL = "http://cudl.lib.cam.ac.uk/view/";
				for (FragmentBibliography fragmentReference : resultSet) {
					Fragment fragment = fragmentReference.getFragment(); 
					String classmark = fragment.getClassmark();
					String label = fragment.getLabel();
					out.println("<div style=\"border: solid 1px gray\" class=\"fragmentDetails\">");
					out.println("<div class=\"fragmentLabel\">Fragment : " + label + "</div>");
					
					Item item = null;
					if (itemFactory != null) {
						item = itemFactory.getItemFromId(classmark);
					}
					
					// show any available data from CUDL
					if (item == null) {
						out.println("(Not in collection)");
					} else {
						out.println("<div class=\"fragmentLinkToCUDL\">Link: ");
						out.println("<a href=\"" + fragmentBaseURL + classmark + "\">");
						out.println(label + "</a></div>");
						out.println("<div class=\"fragmentTitle\">Title:" + item.getTitle() + "</div>");
						String itemAbstract = item.getAbstract();
						if (!itemAbstract.equals("")) {
							out.println("<div class=\"fragmentAbstract\">Abstract:" + itemAbstract + "</div>");
						}
					}
					out.println("</div>");
					
					out.println("<table id=\"referenceTable\">");
					out.println("<tr>");
					out.println("<th>RefType</th>");
					out.println("<th>Reference Title</th>");
					out.println("<th>Authors</th>");
					out.println("</tr>");
					for (Reference reference : fragmentReference.getBibliographyReferences()) {
						BibliographyEntry bibliographyEntry = reference.getEntry();
						out.println("<tr>");
						out.println("<td>" + reference.getTypeReadableForm() + "</td>");
						out.println("<td>" + bibliographyEntry.getTitle() + "</td>");
						
						out.println("<td>");
						List<String> authors = bibliographyEntry.getAuthors();
						for (int authorIndex = 0; authorIndex < authors.size(); authorIndex++) {
							String author = authors.get(authorIndex);
							out.println(author);
							if (authorIndex < authors.size() - 1) {
								out.println(",");
							}
						}
						out.println("</td>");
						out.println("</tr>");
					}
					out.println("</table>");
				}
			}
		%>
</section>
<script type="text/javascript">
	//$("table").find("tr:odd").css("background-color", "#bbbbff");
	//$(document).ready(function() {
	//	$("#referenceTable").dataTable();
	//}
</script>

<jsp:include page="footer/footer.jsp" />



