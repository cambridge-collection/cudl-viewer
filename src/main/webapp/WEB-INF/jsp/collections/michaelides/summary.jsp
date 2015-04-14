<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>

<div class="grid_10">
	<h1><%=collection.getTitle()%></h1>
	<div class="campl-column12">

		<div class="campl-column8">
			<blockquote class="cam-quote-mark">

				[Quote] <br /> <br /> <cite>[Cite]</cite>
			</blockquote>
		</div>
		<div class="campl-column4">
			<img class="collectionOrganisationalImage"
				src="/images/collectionsView/michaelides.jpg" alt="Michaelides"
				width="150" height="225" />
		</div>
	</div>
	<div class="campl-column12">

		<p>[Text]</p>



		<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Michaelides">Search the Michaelides collection</a> <br /> <br />
-->
	</div>
</div>