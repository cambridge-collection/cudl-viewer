<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>
<%
	List<Collection> subCollections = (List<Collection>) request
			.getAttribute("subCollections");

	// Proxy url is used for local installations.  Not used on dev or live. 
	String proxyURL = (String) request.getAttribute("proxyURL");
	if (proxyURL == null) {
		proxyURL = "";
	}
%>
<div class="grid_20">
	<h3 style="margin-left: 8px">Board of Longitude</h3>

	<div class="grid_8">
		<br />
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
	<div class="grid_11 right">
		<iframe width="510" height="345"
			src="http://www.youtube.com/embed/videoseries?list=PL49D9939209E5BAEC&showinfo=1&modestbranding=1&rel=0"
			frameborder="0" allowfullscreen></iframe>
	</div>

	<div class="grid_20">
		<h4>Board of Longitude Collections</h4>
	</div>
	<div class="grid_20" style="margin-left: -4px; margin-bottom: 18px">
		<%
			for (int i = 0; i < subCollections.size(); i++) {
				Collection c = subCollections.get(i);

				// skip the nmm print sub collection, that's displayed separately. 
				if (c.getId().equals("nmm_print")) {
		%>
		<!--  disabled nmm print collection -->
		<div
			class="longitudefeaturedcollection longitudefeaturedcollectiondisabled grid_3">
			<img width="128px" height="128px"
				src="/images/collectionsView/collection-longitude-nmm_print.jpg">
			<span class="featuredcollectionlabel">National Maritime Museum
				Print Works</span>

		</div>
		<%
			continue;
				}
		%>

		<div class="longitudefeaturedcollection grid_3">
			<a href="<%=c.getURL()%>"><img
				src="/images/collectionsView/collection-longitude-<%=c.getId()%>.jpg"
				height="128px" width="128px" /> <span
				class="featuredcollectionlabel"><%=c.getTitle()%></span></a>
		</div>
		<%
			}
		%>

	</div>

	<div class="grid_5" style="width: 210px">
		<h4>Collection statistics</h4>
	</div>

	<div class="grid_14">
		<h4>Featured Items</h4>
	</div>

	<div>

		<div class="grid_4 box"
			style="height: 545px; border-style: solid; border-color: #FFFFFF; border-left-width: 4px;">

			<!--  summary -->
			<div class="longitudestatisticstitle">Board of Longitude
				Collection</div>
			<div class="longitudestatistics">
				<ul>
					<li>48,596 images</li>
					<li>162 volumes</li>
					<li>304 ships</li>
					<li>1337 people</li>
					<li>777 places</li>
				</ul>
			</div>

			<!-- Ships -->
			<div class="longitudestatisticstitle">Ships mentioned most</div>
			<div class="longitudestatistics">
				<ol>
					<li><a
						href="/search?keyword=HMS+Investigator&facetCollection=Papers+of+the+Board+of+Longitude">HMS
							Investigator, survey sloop</a></li>
					<li><a
						href="/search?keyword=Daedalus&facetCollection=Papers+of+the+Board+of+Longitude">Daedalus,
							store ship</a></li>
					<li><a
						href="/search?keyword=Providence&facetCollection=Papers+of+the+Board+of+Longitude">HMS
							Providence, sloop</a></li>
					<li><a
						href="/search?keyword=Hecla&facetCollection=Papers+of+the+Board+of+Longitude">HMS
							Hecla, bomb vessel</a></li>
					<li><a
						href="/search?keyword=HMS+Discovery&facetCollection=Papers+of+the+Board+of+Longitude">HMS
							Discovery</a></li>
				</ol>
			</div>

			<!-- Places -->
			<div class="longitudestatisticstitle">Places mentioned most</div>
			<div class="longitudestatistics">
				<ol>
					<li><a
						href="/search/advanced/results?location=London&facetCollection=Papers+of+the+Board+of+Longitude">London</a></li>
					<li><a
						href="/search/advanced/results?location=Greenwich&facetCollection=Papers+of+the+Board+of+Longitude">Greenwich</a></li>
					<li><a
						href="/search/advanced/results?location=Portsmouth&facetCollection=Papers+of+the+Board+of+Longitude">Portsmouth</a></li>
					<li><a
						href="/search/advanced/results?location=Cape+Town&facetCollection=Papers+of+the+Board+of+Longitude">Cape
							Town</a></li>
					<li><a
						href="/search/advanced/results?location=Australia&facetCollection=Papers+of+the+Board+of+Longitude">Australia</a></li>

				</ol>
			</div>


			<!--  People mentioned -->
			<div class="longitudestatisticstitle">People mentioned most</div>
			<div class="longitudestatistics">
				<ol>
					<li><a
						href="/search?keyword=Sir+John+Barrow&facetCollection=Papers+of+the+Board+of+Longitude">Sir
							John Barrow</a></li>
					<li><a
						href="/search?keyword=Thomas+Young&facetCollection=Papers+of+the+Board+of+Longitude">Thomas
							Young</a></li>
					<li><a
						href="/search?keyword=John+Croker&facetCollection=Papers+of+the+Board+of+Longitude">John
							Croker</a></li>
					<li><a
						href="/search?keyword=Nevil+Maskelyne&facetCollection=Papers+of+the+Board+of+Longitude">Nevil
							Maskelyne</a></li>
					<li><a
						href="/search?keyword=Thomas+Taylor&facetCollection=Papers+of+the+Board+of+Longitude">Thomas
							Taylor</a></li>

				</ol>
			</div>


		</div>


		<div class="grid_14 longitudefeaturedItemsBox">
			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00058/203"> <img
							style="width: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00058-000-00204_files/8/0_0.jpg"
							alt="MS-RGO-00014-00058">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00053/543"> <img
							style="width: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00053-000-00543_files/8/0_0.jpg"
							alt="MS-RGO-00014-00053">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00038/391"> <img
							style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00038-000-00391_files/8/0_0.jpg"
							alt="MS-RGO-00014-00038">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<font color="#fff">Log book of HMS 'Resolution'<br />Map of
					Easter Island<br /> <br /></font>
			</div>
			<div class="longitudefeatureditem">
				<font color="#fff">Illustrations of the system of the world <br />
					<br />
				</font>
			</div>
			<div class="longitudefeatureditem">
				<font color="#fff">Joseph Bonasera's scheme for an instrument
					entitled the longitude horizon<br /> <br />
				</font>
			</div>

			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00067/90"> <img
							style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00067-000-00090_files/8/0_0.jpg"
							alt="MS-RGO-00014-00067">
						</a>
					</div>
				</div>
			</div>

			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-ADM-L-C-00082/100"> <img
							style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-ADM-L-C-00082-000-00100_files/8/0_0.jpg"
							alt="MS-ADM-L-C-00082">
						</a>
					</div>
				</div>
			</div>

			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-SAN-F-00004/1"> <img style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-SAN-F-00004-000-00001_files/8/0_0.jpg"
							alt="MS-SAN-F-00004">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<font color="#fff">Computations of lunar distance<br /> <br /></font>
			</div>

			<div class="longitudefeatureditem">
				<font color="#fff">Captain Proctor's log showing longitudes
					from H1 on its first official sea trial, 1736<br /> <br />
				</font>
			</div>
			<div class="longitudefeatureditem">
				<font color="#fff">Letter to the Earl of Sandwich about
					timekeepers including K1<br /> <br />
				</font>
			</div>
		</div>
	</div>

	<div class="grid_20">

		<div class="grid_9">
			<a href="http://www.rmg.co.uk/boardoflongitude" target="_blank"><img
				src="/images/collectionsView/collection-longitude-archivestories.jpg"
				width="454px" height="150px" /></a>

		</div>

		<div class="grid_10 right">
			<a href="http://www.rmg.co.uk/boardoflongitude/schools" target="_blank"><img
				src="/images/collectionsView/collection-longitude-educationalresources.jpg"
				width="454px" height="150px" /></a>

		</div>
	</div>

</div>