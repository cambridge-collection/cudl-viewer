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
			قراطس مصر لمغرب ككواغد سمرقنند للمشرق
			<br />
			<br />
			The Papyri from Egypt are for the West what the papers from Samarqand are for the East
			<br /> <br /> <cite>al-Jāhiz d.868/9</cite>
			</blockquote>
		</div>
		<div class="campl-column4">
			<img class="collectionOrganisationalImage"
				src="/images/collectionsView/michaelides.jpg" alt="Michaelides"
				width="150" height="225" />
		</div>
	</div>
	<div class="campl-column12">

		<p>Papyrus was the main writing material of the Greek, Roman, Byzantine and Early Islamic societies in Egypt for 4,000 years before paper took its place. The bulk of historical, linguistic, social and economic evidence of the early Islamic Era can be found on papyrus originally produced in Egypt. The Michaelides fragments comprise the largest private collection of Arabic papyri to be found in any institution worldwide.</p>
		
		<p>Cambridge University Library bought the collection in 1977 from the heirs of the antiques dealer Georges Anastase Michaelides (1900-1973). Born in Cairo and educated in Egypt and France, G.A. Michaelides developed a profound interest in the history and life of Egypt from its early civilisations to far beyond the Islamic conquest. At the time of his death he was in possession of over 1,700 fragments of papyri, paper and other materials in the ancient Egyptian languages, Coptic, Greek and predominantly Arabic.  This collection comprises personal letters, legal texts, accounts, literary texts, recipes and other documents. Only a small number have been subject to academic study.</p>
		
		<p>In this first phase of the project, we will make available on-line the Arabic papyri together with the descriptions produced in an <a href="http://www.lib.cam.ac.uk/deptserv/neareastern/michaelides.html">unpublished hand-list</a> by Professor Geoffrey Khan and enhanced by the project team at CUL. This first selection of fragments will be added to over the course of the next two years.</p>


		<!-- 
		Want to view items by subject or date? <br /> <a
			href="/search?facet-collection=Michaelides">Search the Michaelides collection</a> <br /> <br />
-->
	</div>
</div>