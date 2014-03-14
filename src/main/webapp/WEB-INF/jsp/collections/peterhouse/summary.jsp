<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>

<div class="grid_10">
	<h3 style="margin-left: 8px"><%=collection.getTitle()%></h3>
	<blockquote class="grid_5 omega print">

		All scripture is given by inspiration of God, and is profitable for
		doctrine, for reproof, for correction, for instruction in
		righteousness <br /> <br /> <cite>&mdash; St Paul's 2nd
			letter to Timothy (3:16) </cite>

	</blockquote>
	<br /> <br />
	<div class="grid_4">
		<img class="collectionOrganisationalImage"
			src="/images/collectionsView/peterhouse.jpg"
			alt="Peterhouse Crest" width="150" height="225" />
	</div>
	<div class="grid_9">
		<br />
		<p>Peterhouse (founded 1284) is the oldest of the Cambridge Colleges. Its founder, Hugh of Balsham, Bishop of Ely, bequeathed a number of books to the College on his death in 1286. The continuing generosity of members of Peterhouse built up a substantial library during the next two and a half centuries. A catalogue begun in 1418 and maintained until 1481 gives details of over 450 manuscripts, more than two hundred of which survive today, overwhelming still in the College's possession. In addition, manuscripts were added by donors from the end of the fifteenth century until the mid-seventeenth century. A total of 276 medieval manuscripts belonging to Peterhouse are now on deposit in the University Library, where they may be consulted.</p>
		<p>As part of the process of recataloguing and preserving the College's collections, it has been decided to present highlights from the Peterhouse manuscripts in digital form. The first group of manuscripts to be digitised was the independent collection of sixteenth- and seventeenth-century part books of choral music, which are available through the <a href='http://www.diamm.ac.uk/' target='_blank'>Digital Image Archive of Medieval Music (DIAMM)</a>. It is now hoped to present some of the College's medieval manuscripts with appropriate commentary as part of the Cambridge Digital Collections. The first manuscript selected for this purpose is the Equatorie of the Planetis (Peterhouse Ms. 75.1). As funding becomes available, further manuscripts will be added to the online collection.</p>

<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Peterhouse+Collection">Search the
			Peterhouse collection</a> <br /> <br />
-->			
	</div>
</div>