<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,ulcambridge.foundations.viewer.search.*,ulcambridge.foundations.viewer.model.Item,ulcambridge.foundations.viewer.ItemFactory"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<script type="text/javascript">
 function pageinit() {
	
	 $(document).ready(function(){
			$('#paging_container').pajinate({
				items_per_page : 8,
				num_age_links_to_display:10,
				item_container_id:'.search_carousel'
			});
			if ($('#paging_container ol li').length<=8) {
				$('.page_navigation').each(function() { $(this).hide(); });

			}

		});

 }
	
</script>

<div class="clear"></div>

<%
	SearchResultSet resultSet = ((SearchResultSet) request
			.getAttribute("results"));
	SearchQuery query = ((SearchQuery) request.getAttribute("query"));

	int resultsNum = 150; // This is the max number of results displayed

	if (resultSet.getNumberOfResults() < resultsNum) {
		resultsNum = resultSet.getNumberOfResults();
	}
%>

<section id="content" class="grid_20 content searchform"> <!-- <h3 style="margin-left: 8px">Search</h3>  -->

<div class="grid_6 ">
	<div class="box">

	<form class="grid_5" action="/search">
		<input id="search" type="text" value="<%=query.getKeywordDisplay() %>" name="keyword" placeholder="Search"
			autocomplete="off" /> <input id="submit" type="submit"
			value="Search" />

		<%
			Iterator<String> facetsUsedHidden = query.getFacets().keySet().iterator();
			while (facetsUsedHidden.hasNext()) {
				String facetName = facetsUsedHidden.next();
				String facetValue = query.getFacets().get(facetName);
		%>
		          <input type="hidden" name="facet-<%=facetName %>" value="<%=facetValue %>">
		<%  } %>
		</form>
		
		<%
			Iterator<String> facetsUsed = query.getFacets().keySet().iterator();
			while (facetsUsed.hasNext()) {
				String facetName = facetsUsed.next();
				String facetValue = query.getFacets().get(facetName);
		%>
		<div class="search-facet-selected"><a class="search-close" href="?<%=query.getURLParametersWithoutFacet(facetName)%>&amp;"></a>
		<%
			out.print(facetValue);
		%></div>
		<%
			}


				out.print("<br /><br /><b>" + resultsNum + "</b>"
						+ " results were returned.<br/><br/>");
			
		%>
		<%
			if (resultsNum > 0) {
		%>
		<h5>Refine by:</h5>

		<ul id="tree" >
			<%
				List<FacetGroup> facetGroups = resultSet.getFacets();

						if (facetGroups != null) {

							for (int i = 0; i < facetGroups.size(); i++) {
								FacetGroup facetGroup = (FacetGroup) facetGroups
										.get(i);
								String fieldLabel = facetGroup.getFieldLabel();
								String field = facetGroup.getField();
								List<Facet> facets = facetGroup.getFacets();

								// Do not print out the facet for a field already faceting on
								if (!query.getFacets().containsKey(field)) {
									
									out.println("<li>" + fieldLabel + "<ul>");

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

<div class="grid_13 search_results" id="paging_container" >
<div class="page_navigation"></div><div class='clear'></div>
<ol id="search_carousel" class="search_carousel">
	<%
		List<SearchResult> results = resultSet.getResults();

		if (results != null) {

			for (int i = 0; i < resultsNum; i++) {
				SearchResult result = (SearchResult) results.get(i);

				if (result != null) {
					Item item = ItemFactory.getItemFromId(result.getId());
					if (item != null) {
						// print out title
						out.print("<li><div class='search_carousel_item'><div class='grid_7'><a href='/view/"
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

						out.print("</a></div></div></div>\n\n");
					}
				}
			}
		}
		
		
		if (resultsNum==0) {
			out.print("<div>We couldn't find any items matching <b>"+query.getKeywordDisplay()+"</b>. Try <a href='/search'>browsing our items.</a></div>");
		}
	%>
	
	

</ol>
<div class='clear'></div><br/><div class="page_navigation"></div>
</div>
</section>


<jsp:include page="footer/footer.jsp" />



