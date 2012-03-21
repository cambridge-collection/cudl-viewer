<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,ulcambridge.foundations.viewer.search.*,ulcambridge.foundations.viewer.model.Item,ulcambridge.foundations.viewer.ItemFactory"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-unselected.jsp" />

<div class="clear"></div>

<%
	SearchResultSet resultSet = ((SearchResultSet) request
			.getAttribute("results"));
	SearchQuery query = ((SearchQuery) request.getAttribute("query"));

	int resultsNum = 100; // This is the max number of results displayed

	if (resultSet.getNumberOfResults() < 100) {
		resultsNum = resultSet.getNumberOfResults();
	}
%>

<section id="content" class="grid_20 content searchform"> <!-- <h3 style="margin-left: 8px">Search</h3>  -->

<div class="grid_6">
	<div class="box">
		<form method="GET" action="?<%=query.getURLParameters()%>">
			<input name="keyword" type="text" size="20"
				value="<%=query.getKeyword()%>" />
				 <input type="submit" name=submit
				value="Search">
		<%
			Iterator<String> facetsUsedHidden = query.getFacets().keySet().iterator();
			while (facetsUsedHidden.hasNext()) {
				String facetName = facetsUsedHidden.next();
				String facetValue = query.getFacets().get(facetName);
		%>
		          <input type="hidden" name="facet-<%=facetName %>" value="<%=facetValue %>">
		<%  } %>
				<span style="color:#ff0000"><%=resultSet.getError()%></span>
		</form>
		<br />

		<%
			Iterator<String> facetsUsed = query.getFacets().keySet().iterator();
			while (facetsUsed.hasNext()) {
				String facetName = facetsUsed.next();
				String facetValue = query.getFacets().get(facetName);
		%>
		<a href="?<%=query.getURLParametersWithoutFacet(facetName)%>&">X</a>
		<%
			out.print(facetName);
		%>:
		<%
			out.print(facetValue);
		%>
		<br />
		<%
			}

				out.print("<b>" + resultsNum + "</b>"
						+ " results were returned.<br/><br/>");
			
		%>
		<%
			if (resultsNum > 0) {
		%>
		<h5>Refine by:</h5>

		<ul id="tree">
			<%
				List<FacetGroup> facetGroups = resultSet.getFacets();

						if (facetGroups != null) {

							for (int i = 0; i < facetGroups.size(); i++) {
								FacetGroup facetGroup = (FacetGroup) facetGroups
										.get(i);
								String field = facetGroup.getField();
								List<Facet> facets = facetGroup.getFacets();

								// Do not print out the facet for a field already faceting on
								if (!query.getFacets().containsKey(field)) {
									
									out.println("<li>" + field + "<ul>");

									for (int j = 0; j < facets.size(); j++) {
										Facet facet = facets.get(j);

										out.print("<li><a href='?"
												+ query.getURLParametersWithExtraFacet(
														field, facet.getBand()) + "'>");
										out.print(facet.getBand() + "</a> ("
												+ facet.getOccurences() + ")</li>");

									}
									out.println("</ul></li>");
								}
							}
						}
			%>
		</ul>
		<%
			}
		%>

	</div>

</div>

<div class="grid_13">
	<%
		List<SearchResult> results = resultSet.getResults();

		if (results != null) {

			for (int i = 0; i < resultsNum; i++) {
				SearchResult result = (SearchResult) results.get(i);

				if (result != null) {
					Item item = ItemFactory.getItemFromId(result.getId());
					if (item != null) {
						// print out title
						out.print("<div class='grid_7'><a href='/view/"
								+ result.getId() + "'>" + result.getTitle()
								+ " (" + item.getShelfLocator() + ")"
								+ "</a><br/>");
						out.print(item
								.getAuthors()
								.toString()
								.substring(
										1,
										item.getAuthors().toString()
												.length() - 1)
								+ "<br/><br/>");
						out.print(item.getAbstractShort()
								+ " ... <br/><br/>\n");

						// print out snippets
						//List<String> snippets = result.getSnippets();
						//out.print("<div class='search-result-snippet'>");
						//for (int j = 0; j < snippets.size(); j++) {
						//	out.print("..." + snippets.get(j)
						//			+ "...\n<br/>");
						//}
						//out.print("</div>");

						// print out image
						out.print("</div><div class='grid_5'>");
						out.print("<div class='search-result-imgcontainer'>");
						out.print("<a href='/view/" + result.getId() + "'>");
						if (item.getThumbnailOrientation().equals(
								"landscape")) {

							out.print("<img style='width:100%;margin:auto;' ");
						} else {
							out.print("<img style='height:100%;margin:auto;' ");
						}
						out.println(" alt=" + item.getId() + " title="
								+ item.getId() + " src='"
								+ item.getThumbnailURL() + "'/>");

						out.print("</a></div></div>\n\n");
					}
				}
			}
		}
	%>


</div>
</section>

<jsp:include page="footer/footer.jsp" />



