<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>

<div class="grid_10">
	<h3 style="margin-left: 8px"><%=collection.getTitle()%></h3>
	<blockquote class="grid_5 omega print">

		One may say there is a force like a hundred thousand wedges trying force ‹into› every kind of adapted structure into the gaps ‹of› in the œconomy of Nature, or rather forming gaps by thrusting out weaker ones
		
		<br /> <br />
		
		
		<cite>&mdash;  Charles Darwin<br/>
		Notebook D 135e, 28 September 1838</cite>

	</blockquote>
	<br /> <br />
	<div class="grid_4">
		<img class="collectionOrganisationalImage"
			src="/images/collectionsView/japanese.jpg"
			alt="Japanese Works" width="150" height="225" />
	</div>
	<div class="grid_9">
		<br />
		<p>Collection text - replace</p>
		
		
		
		

<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Darwin Manuscripts">Search the
			Darwin Manuscripts collection</a> <br /> <br />
-->			
	</div>
</div>