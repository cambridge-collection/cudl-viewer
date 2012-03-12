<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory"%>
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

<section id="content" class="grid_20 content searchform">

<!-- <h3 style="margin-left: 8px">Search</h3>  -->

<div class="grid_6">
	<div class="box">

		<form:form method="get" commandName="searchForm">

			<form:input path="keyword" size="20" />
			<input type="submit" name=submit value="Search">
			<br />

			<%
				Iterator<String> facetsUsed = query.getFacets().keySet()
							.iterator();
					while (facetsUsed.hasNext()) {
						String facetName = facetsUsed.next();
						String facetValue = query.getFacets().get(facetName);
			%>
			<form:checkbox path="facets"
				value="<%out.print(facetName);%>||<%out.print(facetValue);%>"
				checked='true' />
			<%
				out.print(facetName);
			%>: <%
				out.print(facetValue);
			%><br />

			<%
				}

					if (query.getKeyword()!=null && !query.getKeyword().trim().equals("")) {
						out.print("<br/><b>" + resultsNum + "</b>"
								+ " results were returned.<br/><br/>");
					}
			%>
			<%
					if (resultsNum > 0) {											
			%>
			<h5>Refine by:</h5><br/>
			<ul id="facets">
				<%
					List<SearchResultFacet> facets = resultSet.getFacets();

							if (facets != null) {

								for (int i = 0; i < facets.size(); i++) {
									SearchResultFacet facet = (SearchResultFacet) facets
											.get(i);
									String field = facet.getField();
									List<String> groups = facet.getGroups();
									List<Integer> groupTotalDocs = facet.getGroupTotalDocs();
									
									out.println("<li><span>" + field + "</span><ul>");

									for (int j = 0; j < groups.size(); j++) {
										String group = groups.get(j);
										out.print("<li><a href='?keyword="
												+ URLEncoder.encode(
														request.getParameter("keyword"),
														"UTF-8")
												+ "&facets="
												+ URLEncoder.encode(facet.getField(),
														"UTF-8") + "||"
												+ URLEncoder.encode(group, "UTF-8")
												+ "'>");
										out.print(group + "</a> ("+groupTotalDocs.get(j)+")</li>");
									}
									out.println("</ul></li>");
								}
							}
				%>
			</ul>
			<%
				}
			%>
		
	</div>


	</form:form>
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
						out.print("<div class='grid_4'><a href='/view/"
								+ result.getId() + "'>" + result.getTitle()
								+ " (" + item.getShelfLocator() + ")"
								+ "</a><br/>");

						// print out image
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



