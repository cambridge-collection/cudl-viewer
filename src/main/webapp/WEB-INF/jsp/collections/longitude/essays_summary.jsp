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

				Here are several ways of finding the Longitude; some of them
				certainly will be right, tho' they have never appeared in any
				publication" <br /> <br /> <cite><a
					href="/view/MS-RGO-00014-00040/89">Richard Judson, clerk of
						Beverley in Yorkshire, 15 May 1805</a><br /> </cite>
			</blockquote>
		</div>
		<div class="campl-column4">
			<img class="collectionOrganisationalImage"
				src="/images/collectionsView/longitudeessays.jpg"
				alt="Papers of the Board of Longitude" width="150" height="225" />
		</div>
	</div>
	<div class="campl-column12">

		<p>This collection of essays provide a rich contextual background
			to the Longitude material available on the Digital Library. Covering
			a wide range of subject matter and fully linked into the other
			Longitude collections, they are intended as both a guide and a map to
			the collection.</p>

		<p>The essays include short biographies of key figures in the
			history of the Board, such as Nevil Maskelyne, John Harrison and
			Joseph Banks, explanation of key terms and concepts, such as dead
			reckoning and magnetic variation, and studies of specific instruments
			such as the artificial horizon and marine chair.</p>

		<p>All essays are fully integrated into the Digital Library's
			search, and link back out to relevant items in our collections.</p>


	</div>

	<!-- 
	<div class="box grid_8">
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Longitude+Essays">Search Longitude Essays</a>
	</div>
	-->
</div>