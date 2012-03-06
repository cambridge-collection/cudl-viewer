<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*,ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-about.jsp" />

<div class="clear"></div>

<section id="content" class="grid_20 content">

<h3 style="margin-left: 8px">Search</h3>

<div class="grid_5">
	<div class="box">
		<%
			SearchResultSet resultSet = ((SearchResultSet) request
					.getAttribute("results"));
		SearchQuery query = ((SearchQuery) request
				.getAttribute("query"));		

			int resultsNum = 100; // This is the max number of results displayed

			if (resultSet.getNumberOfResults() < 100) {
				resultsNum = resultSet.getNumberOfResults();
			}
			
			out.print("Searched for ");
			if (query.getKeyword()!=null && !query.getKeyword().equals("")) {
				out.print(" <b>" + query.getKeyword()+ "</b><br>");				
			}
			Iterator<String> facetsUsed = query.getFacets().keySet().iterator();
			while (facetsUsed.hasNext()) {
				String facetName = facetsUsed.next();
				String facetValue = query.getFacets().get(facetName);
				out.print("" + facetName + ":<b> " +facetValue+ "</b><br>");				
			}

			out.print("<br/><b>" + resultsNum +"</b>"
					+ " results were returned.<br/><br/>");
			
			
		%>
		<h5>Refine by:</h5>
			<ul id="facets">
		<%
		List<SearchResultFacet> facets = resultSet.getFacets();

		if (facets != null) {

			for (int i = 0; i < facets.size(); i++) {
				SearchResultFacet facet = (SearchResultFacet) facets.get(i);
				String field = facet.getField();
				List<String> groups = facet.getGroups();
				out.println("<li><span>"+field+"</span><ul>");
				
				for (int j = 0; j < groups.size(); j++) {
					String group = groups.get(j);					
				    out.print("<li><a href='?query="+request.getParameter("query")
				    +"&facets="+facet.getField()+":"+group+"&sort='>");				    
				    out.print(group+"</a></li>");
				}
				out.println("</ul></li>");
			}
		}
			
		%>
	</ul>


	</div>
</div>
<div class="grid_14">
	<%
		List<SearchResult> results = resultSet.getResults();

		if (results != null) {

			for (int i = 0; i < resultsNum; i++) {
				SearchResult result = (SearchResult) results.get(i);

				if (result != null) {
					Item item = ItemFactory.getItemFromId(result.getId());
					if (item != null) {
						// print out title
						out.print("<div class='grid_8'><a href='/view/"
								+ result.getId() + "'>" + result.getTitle()
								+ " (" + item.getShelfLocator() + ")"
								+ "</a><br/>");
						out.print(item.getAbstractShort()
								+ " ... <br/><br/>\n");

						// print out snippets
						List<String> snippets = result.getSnippets();
						out.print("<div class='search-result-snippet'>");
						for (int j = 0; j < snippets.size(); j++) {
							out.print("..." + snippets.get(j)
									+ "...\n<br/>");
						}
						out.print("</div>");

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



