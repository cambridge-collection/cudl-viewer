<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>
<%
	List<Collection> subCollections = (List<Collection>) request
			.getAttribute("subCollections");

	// Proxy url is used for local installations.  Not used on dev or live. 
	String imageServer = (String) request.getAttribute("imageServer");
	
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
			src="//www.youtube.com/embed/Ey_jsHOZH2Q?list=PLBO5xEgOdGVLTZ_ErwnA9AyvNZ4PP1Jlz&showinfo=1&modestbranding=1&rel=0"
			frameborder="0" allowfullscreen></iframe>
	</div>

	<div class="grid_20">
		<h4>Board of Longitude Collections</h4>
	</div>
	<div class="grid_20" style="margin-left: -4px; margin-bottom: 18px">
		<%
			for (int i = 0; i < subCollections.size(); i++) {
				Collection c = subCollections.get(i);
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
					<li>63,697 images</li>
					<li>242 volumes</li>
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
						href="/search?keyword=HMS+Investigator">HMS
							Investigator, survey sloop</a></li>
					<li><a
						href="/search?keyword=Daedalus">Daedalus,
							store ship</a></li>
					<li><a
						href="/search?keyword=Providence">HMS
							Providence, sloop</a></li>
					<li><a
						href="/search?keyword=Hecla">HMS
							Hecla, bomb vessel</a></li>
					<li><a
						href="/search?keyword=HMS+Discovery">HMS
							Discovery</a></li>
				</ol>
			</div>

			<!-- Places -->
			<div class="longitudestatisticstitle">Places mentioned most</div>
			<div class="longitudestatistics">
				<ol>
					<li><a
						href="/search/advanced/results?location=London">London</a></li>
					<li><a
						href="/search/advanced/results?location=Greenwich">Greenwich</a></li>
					<li><a
						href="/search/advanced/results?location=Portsmouth">Portsmouth</a></li>
					<li><a
						href="/search/advanced/results?location=Cape+Town">Cape
							Town</a></li>
					<li><a
						href="/search/advanced/results?location=Australia">Australia</a></li>

				</ol>
			</div>


			<!--  People mentioned -->
			<div class="longitudestatisticstitle">People mentioned most</div>
			<div class="longitudestatistics">
				<ol>
					<li><a
						href="/search?keyword=Sir+John+Barrow">Sir
							John Barrow</a></li>
					<li><a
						href="/search?keyword=Thomas+Young">Thomas
							Young</a></li>
					<li><a
						href="/search?keyword=John+Croker">John
							Croker</a></li>
					<li><a
						href="/search?keyword=Nevil+Maskelyne">Nevil
							Maskelyne</a></li>
					<li><a
						href="/search?keyword=Thomas+Taylor">Thomas
							Taylor</a></li>

				</ol>
			</div>


		</div>


		<div class="grid_14 longitudefeaturedItemsBox">
			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00051/358"> <img
							style="height: 100%"
							src="<%=imageServer%>/content/images/MS-RGO-00014-00051-000-00359_files/8/0_0.jpg"
							alt="MS-RGO-00014-00051">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00024/451"> <img
							style="height: 100%"
							src="<%=imageServer%>/content/images/MS-RGO-00014-00024-000-00451_files/8/0_0.jpg"
							alt="MS-RGO-00014-00024">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00039/83"> <img
							style="height: 100%"
							src="<%=imageServer%>/content/images/MS-RGO-00014-00039-000-00083_files/8/0_0.jpg"
							alt="MS-RGO-00014-00083">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<p>Matthew Flinders on the naming of Australia<br /> <br /></p>
			</div>
			<div class="longitudefeatureditem">
				<p>Captain Bligh on the loss of his timekeeper to mutineers <br />
					<br />
				</p>
			</div>
			<div class="longitudefeatureditem">
				<p>Drawings of globe instruments including a movable compass<br /> <br />
				</p>
			</div>

			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-MM-00006-00048/368"> <img
							style="height: 100%"
							src="<%=imageServer%>/content/images/MS-MM-00006-00048-000-00368_files/8/0_0.jpg"
							alt="MS-MM-00006-00368">
						</a>
					</div>
				</div>
			</div>

			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00044/197"> <img
							style="height: 100%"
							src="<%=imageServer%>/content/images/MS-RGO-00014-00044-000-00197_files/8/0_0.jpg"
							alt="MS-RGO-00014-00197">
						</a>
					</div>
				</div>
			</div>

			<div class="longitudefeatureditem">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00058/255"> <img style="height: 100%"
							src="<%=imageServer%>/content/images/MS-RGO-00014-00058-000-00256_files/8/0_0.jpg"
							alt="MS-RGO-00014-00058">
						</a>
					</div>
				</div>
			</div>
			<div class="longitudefeatureditem">
				<p>William Gooch's sketch of a 'Chief of Ohitahoo'<br /> <br /></p>
			</div>

			<div class="longitudefeatureditem">
				<p>Lieutenant Couch's Callista<br /> <br />
				</p>
			</div>
			<div class="longitudefeatureditem">
				<p>First map of Palliser's Islands from Cook's Second Voyage<br /> <br />
				</p>
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
