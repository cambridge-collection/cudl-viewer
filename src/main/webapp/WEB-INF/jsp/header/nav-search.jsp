<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,ulcambridge.foundations.viewer.search.*"%>
<nav id="navPrimary" class="grid_20">
<ul>
	<li class="first"><a href="/" title="Home"> Home </a></li>
	<li><a href="/collections/" title="Browse"> Browse </a></li>
	<li><a href="/news/" title="News"> News </a></li>
	<li><a href="/about/" title="About"> About </a></li>
	<li><a href="/help/" title="Help"> Help </a></li>
</ul>

<%
  SearchQuery query = ((SearchQuery) request.getAttribute("query"));
%>
	<form class="grid_5 prefix_1 omega" action="/search">
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
</nav>
<!-- end #navPrimary -->