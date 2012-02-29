<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List,ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-about.jsp" />

<div class="clear"></div>

<section id="content" class="grid_20 content">
<div class="grid_16">

	<div class="panel light">

		<h3 style="margin-left: 8px">Search</h3>

		<div class="grid_18">
<%
  SearchResultSet resultSet = ((SearchResultSet) request.getAttribute("results"));

  out.print("searched for <b>" + resultSet.getSearchQuery() + "</b> and "
	+ resultSet.getNumberOfResults() + " results returned:<br/><br/>");

  List results = resultSet.getResults();

  if (results != null) {

	for (int i = 0; i < results.size(); i++) {
		SearchResult result = (SearchResult) results.get(i);

		if (result != null) {
			Item item = ItemFactory.getItemFromId(result.getId());
			if (item != null) {				
				// print out title
				out.print("<div class='grid_8'><a href='/view/"	+ result.getId() + "'>" 
						+ result.getTitle() + " (" + item.getShelfLocator() +")"
						+ "</a><br/>");
				out.print(item.getAbstractShort()+" ... <br/><br/>\n");
				
				// print out snippets
				List<String> snippets = result.getSnippets();
				out.print("<div class='search-result-snippet'>");
				for (int j=0; j<snippets.size(); j++) {
					out.print("..."+snippets.get(j)+"...\n<br/>");
				}
				out.print("</div>");

			    // print out image
				out.print("</div><div class='grid_9'>");
				out.print("<div class='search-result-imgcontainer'>");
				out.print("<a href='/view/"	+ result.getId() + "'>");
				if (item.getThumbnailOrientation().equals("landscape")) {

				  out.print("<img style='width:100%;margin:auto;' src='"+item.getThumbnailURL()+"'/>");
				} else {
				  out.print("<img style='height:100%;margin:auto;' src='"+item.getThumbnailURL()+"'/>");
				}
				out.print("</a></div></div>\n\n");
			}
		}
	}
}
			%>


		</div>
	</div>
</div>

</section>

<script language="javascript">
	// default to hide error if JS enabled. 
	toggleDiv('snippetdiv');
</script>

<jsp:include page="footer/footer.jsp" />



