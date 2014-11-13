<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>

<div class="grid_10">
	<h3 style="margin-left: 8px"><%=collection.getTitle()%></h3>
	<blockquote class="grid_5 omega print">

		Music is enough for a lifetime, but a lifetime is not enough for music
		
		<br /> <br />
		
		
		<cite>&mdash; Sergei Rachmaninov</cite>

	</blockquote>
	<br /> <br />
	<div class="grid_4">
		<img class="collectionOrganisationalImage"
			src="/images/collectionsView/music.jpg"
			alt="Music" width="150" height="225" />
	</div>
	<div class="grid_9">
		<br />
		<p>The music collections at Cambridge University Library play an important role in the preservation of musical heritage, both on a national and international level. From printed and manuscript music scores and texts on music to ephemera and concert programmes, archival materials documenting the life and work of composers, music scholars and performers, a huge variety and breadth of material has been collected over several centuries.</p>
		
		<p>Within the printed music collections a large section is devoted to music published in the British Isles, much of which has been received under the provisions of the various Copyright Acts since 1709. As a result the library holds for example an amazing collection of Victorian songs.  Named special collections provide additional collections strengths such as 18th-century music treatises in the Arnold Bequest, the Kikutei scrolls in the Picken Collection, and historical performance sets in the CUMS orchestral library, to name just a few. 
The archival collections focus primarily, but by no means exclusively, on British 20th-century music. Composer archives such as the Arthur Bliss, William Alwyn, Roberto Gerhard and Peter Tranchell Archives typically contain archival material as well as music manuscripts. There are also collections of autograph music by James Hook, Alan Gray, C.B. Rootham, Peter Warlock and many other composers.</a>

		<p>The music manuscripts collections contain key works from various periods of music history. The oldest works date back to medieval times; the library holds medieval liturgical manuscripts that form key resources for research into English plainsong as well as sixth century treatises on music. One of the most important collections of manuscript music in the Library is the group of nine lute manuscripts copied by Mathew Holmes in the early years of the seventeenth century. A selection of these have been digitised and described in collaboration with the <a href="http://www.lutesociety.org/">Lute Society</a>, and we are very pleased to present these as the first items in our Music Collection.</p>
		
		
		
		

<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Music">Search the
			Music collection</a> <br /> <br />
-->			
	</div>
</div>