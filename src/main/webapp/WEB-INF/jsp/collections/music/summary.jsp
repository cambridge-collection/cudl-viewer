<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>

<div class="grid_10">
	<h3 style="margin-left: 8px"><%=collection.getTitle()%></h3>
	<blockquote class="grid_5 omega print">

		Quote - replace
		
		<br /> <br />
		
		
		<cite>&mdash; citation - replace</cite>

	</blockquote>
	<br /> <br />
	<div class="grid_4">
		<img class="collectionOrganisationalImage"
			src="/images/collectionsView/japanese.jpg"
			alt="Japanese Works" width="150" height="225" />
	</div>
	<div class="grid_9">
		<br />
		<p>Collection text - replace.</p>
		
		
		
		

<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Music">Search the
			Music collection</a> <br /> <br />
-->			
	</div>
</div>