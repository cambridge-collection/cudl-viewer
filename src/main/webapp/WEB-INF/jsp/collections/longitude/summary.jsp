<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>
<%
    Collection collection = (Collection) request.getAttribute("collection");

	List<Collection> subCollections = (List<Collection>) request
			.getAttribute("subCollections");

	// Proxy url is used for local installations.  Not used on dev or live. 
	String imageServer = (String) request.getAttribute("imageServer");
			
%>

<div class="campl-column6  campl-main-content" id="content">

	<div class="campl-content-container">

		<div class="campl-column12">
			<h2>Introduction</h2>
		</div>

		<p>The archives of the Royal Greenwich Observatory, held in
			Cambridge University Library, include the complete run of the
			surviving papers of the Board of Longitude through the eighteenth
			century until its abolition in 1828. These papers throw a vivid light
			on the role of the British state in encouraging invention and
			discovery, on the energetic culture of technical ingenuity in the
			long eighteenth century, and on many aspects of exploration and
			maritime travel in the Pacific Ocean and the Arctic.</p>

		<p>
			This project, a partnership between <a
				href="http://www.lib.cam.ac.uk">Cambridge University Library</a>,
			the <a href="http://www1.rmg.co.uk/">National Maritime Museum</a> and
			the AHRC-funded <a href="http://blogs.rmg.co.uk/longitude/">Board
				of Longitude Project</a>, presents fully digitised versions of the
			complete archive and associated materials, alongside detailed
			metadata, contextual essays, video, educational resources and
			hundreds of links through to relevant objects in the National
			Maritime Museum's online collections.
		</p>

	</div>
	<div class="campl-content-container">
		<iframe width="510" height="345"
			src="//www.youtube.com/embed/Ey_jsHOZH2Q?list=PLBO5xEgOdGVLTZ_ErwnA9AyvNZ4PP1Jlz&showinfo=1&modestbranding=1&rel=0"
			frameborder="0" allowfullscreen></iframe>
	</div>

	<div class="campl-content-container">
		<h2>Board of Longitude Collections</h2>
	</div>
	<div class="grid_20">
		<%
			for (int i = 0; i < subCollections.size(); i++) {
				Collection c = subCollections.get(i);
		%>

		<div class="campl-column8">
			<div class="campl-content-container campl-side-padding">
				<div
					class="campl-horizontal-teaser campl-teaser clearfix campl-focus-teaser">
					<div class="campl-focus-teaser-img">
						<div class="campl-content-container campl-horizontal-teaser-img">
							<a href="<%=c.getURL()%>"><img
								src="/images/collectionsView/collection-longitude-<%=c.getId()%>.jpg"
								height="128px" width="128px" /> </a>
						</div>
					</div>
					<div class="campl-focus-teaser-txt">
						<div class="campl-content-container campl-horizontal-teaser-txt">
							<h3 class='campl-teaser-title'>
								<a href="<%=c.getURL()%>"><%=c.getTitle()%></a>
							</h3>
							<a href="<%=c.getURL()%>" class="ir campl-focus-link">Read
								more</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%
			}
		%>

	</div>

	<div class="campl-content-container campl-column12">
		<h2>Additional Resources</h2>
	</div>
	<div class="campl-content-container campl-column8">

		<a href="http://www.rmg.co.uk/boardoflongitude" target="_blank"><img
			src="/images/collectionsView/collection-longitude-archivestories.jpg"
			width="454px" height="150px" /></a>

	</div>

	<div class="campl-content-container campl-column8">
		<a href="http://www.rmg.co.uk/boardoflongitude/schools"
			target="_blank"><img
			src="/images/collectionsView/collection-longitude-educationalresources.jpg"
			width="454px" height="150px" /></a>

	</div>

</div>


<div class="campl-column3 campl-secondary-content">
	<div class="campl-content-container">
		<div class="campl-heading-container">
			<h2>Collection Statistics</h2>
		</div>

		<div>
			<ul class='campl-unstyled-list '>

				<!--  summary -->
				<li>
					<div class="longitudestatisticstitle">Board of Longitude
						Collection</div>
					<div class="longitudestatistics">
						<ol class='campl-unstyled-list'>
							<li>63,697 images</li>
							<li>242 volumes</li>
							<li>304 ships</li>
							<li>1337 people</li>
							<li>777 places</li>
						</ol>
					</div>
				</li>


				<!-- Ships -->
				<li>
					<div class="longitudestatisticstitle">Ships mentioned most</div>
					<div class="longitudestatistics">
						<ol class='campl-unstyled-list'>
							<li><a href="/search?keyword=HMS+Investigator">HMS
									Investigator, survey sloop</a></li>
							<li><a href="/search?keyword=Daedalus">Daedalus, store
									ship</a></li>
							<li><a href="/search?keyword=Providence">HMS Providence,
									sloop</a></li>
							<li><a href="/search?keyword=Hecla">HMS Hecla, bomb
									vessel</a></li>
							<li><a href="/search?keyword=HMS+Discovery">HMS
									Discovery</a></li>
						</ol>
					</div>
				</li>

				<!-- Places -->
				<li>
					<div class="longitudestatisticstitle">Places mentioned most</div>
					<div class="longitudestatistics">
						<ol class='campl-unstyled-list'>
							<li><a href="/search/advanced/results?location=London">London</a></li>
							<li><a href="/search/advanced/results?location=Greenwich">Greenwich</a></li>
							<li><a href="/search/advanced/results?location=Portsmouth">Portsmouth</a></li>
							<li><a href="/search/advanced/results?location=Cape+Town">Cape
									Town</a></li>
							<li><a href="/search/advanced/results?location=Australia">Australia</a></li>

						</ol>
					</div>
				</li>

				<!--  People mentioned -->
				<li>
					<div class="longitudestatisticstitle">People mentioned most</div>
					<div class="longitudestatistics">
						<ol class='campl-unstyled-list'>
							<li><a href="/search?keyword=Sir+John+Barrow">Sir John
									Barrow</a></li>
							<li><a href="/search?keyword=Thomas+Young">Thomas Young</a></li>
							<li><a href="/search?keyword=John+Croker">John Croker</a></li>
							<li><a href="/search?keyword=Nevil+Maskelyne">Nevil
									Maskelyne</a></li>
							<li><a href="/search?keyword=Thomas+Taylor">Thomas
									Taylor</a></li>

						</ol>
					</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="campl-content-container ">
		<div class="campl-heading-container ">
			<h2>Featured Items</h2>
		</div>
		<ul
			class='campl-unstyled-list campl-related-links campl-horizontal-teaser-img '>

			<li><a href="/view/MS-RGO-00014-00051/358"><img
					alt="MS-RGO-00014-00051/358"
					src="<%=imageServer%>/content/images/MS-RGO-00014-00051-000-00359_files/8/0_0.jpg"
					style='height: 100%' width='120px' /></a>
				<h5>
					<a href="/view/MS-RGO-00014-00051/358">Matthew Flinders on the
						naming of Australia</a>
				</h5></li>
			<li><a href="/view/MS-RGO-00014-00024/451"><img
					alt="MS-RGO-00014-00024/451"
					src="<%=imageServer%>/content/images/MS-RGO-00014-00024-000-00451_files/8/0_0.jpg"
					style='height: 100%' width='120px' /></a>
				<h5>
					<a href="/view/MS-RGO-00014-00024/451">Captain Bligh on the
						loss of his timekeeper to mutineers</a>
				</h5></li>
			<li><a href="/view/MS-RGO-00014-00039/83"><img
					alt="MS-RGO-00014-00039/83"
					src="<%=imageServer%>/content/images/MS-RGO-00014-00039-000-00083_files/8/0_0.jpg"
					style='height: 100%' width='120px' /></a>
				<h5>
					<a href="/view/MS-RGO-00014-00039/83">Drawings of globe
						instruments including a movable compass</a>
				</h5></li>

			<li><a href="/view/MS-RGO-00014-00044/197"><img
					alt="MS-RGO-00014-00044/197"
					src="<%=imageServer%>/content/images/MS-RGO-00014-00044-000-00197_files/8/0_0.jpg"
					style='height: 100%' width='120px' /></a>
				<h5>
					<a href="/view/MS-RGO-00014-00044/197">Lieutenant Couch's
						Callista</a>
				</h5></li>

			<li><a href="/view/MS-RGO-00014-00058/255"><img
					alt="MS-RGO-00014-00058/255"
					src="<%=imageServer%>/content/images/MS-RGO-00014-00058-000-00256_files/8/0_0.jpg"
					style='height: 100%' width='120px' /></a>
				<h5>
					<a href="/view/MS-RGO-00014-00058/255">First map of Palliser's
						Islands from Cook's Second Voyage</a>
				</h5></li>
		</ul>
	</div>


</div>