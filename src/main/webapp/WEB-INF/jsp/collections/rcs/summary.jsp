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
			The Royal Commonwealth Society Library truly takes the world as its oyster
				<br /> <br /> <cite>John M. MacKenzie</cite>
			</blockquote>
		</div>
		<div class="campl-column4">
			<img class="collectionOrganisationalImage"
				src="/images/collectionsView/rcs.jpg" alt="Royal Commonwealth Society"
				width="150" height="225" />
		</div>
	</div>
	<div class="campl-column12">
	
		

		<p>Put collection text here</p>

		<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Royal Commonwealth Society">Search the Royal Commonwealth Society collection</a> <br /> <br />
-->
	</div>
</div>