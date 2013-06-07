<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>
<%
	List<Collection> subCollections = (List<Collection>) request
			.getAttribute("subCollections");

    // Proxy url is used for local installations.  Not used on dev or live. 
    String proxyURL = (String) request.getAttribute("proxyURL");
    if (proxyURL==null) { proxyURL = ""; }
%>
<div class="grid_20">
	<h3 style="margin-left: 8px">Board of Longitude</h3>

	<div class="grid_8">
		<br />
		<p>The archives of the Royal Greenwich Observatory, held in
			Cambridge University Library, include the complete run of the papers
			of the Board of Longitude through the eighteenth century until its
			abolition in 1828. These papers throw a vivid light on the role of
			the British state in encouraging invention and discovery, on the
			energetic culture of technical ingenuity in the long eighteenth
			century, and on many aspects of exploration and maritime travel in
			the Pacific Ocean and the Arctic.</p>

		<p>This project, a partnership between <a href="http://www.lib.cam.ac.uk">Cambridge University Library</a>,
			the <a href="http://www1.rmg.co.uk/">National Maritime Museum</a> and the AHRC-funded <a href="http://blogs.rmg.co.uk/longitude/">Board of Longitude Project</a>,
			presents fully digitised versions of the complete archive and associated materials,
			alongside detailed metadata, contextual essays, video, educational resources and
			hundreds of links through to relevant objects in the National Maritime Museum's 
			online collections.</p>

	</div>
	<div class="grid_11 right">
		<iframe width="510" height="345"
			src="http://www.youtube.com/embed/videoseries?list=PL49D9939209E5BAEC&showinfo=1&modestbranding=1"
			frameborder="0" allowfullscreen></iframe>
	</div>
	<div class="grid_20">
		<div class="grid_15">
			<h4>Board of Longitude Collections</h4>
		</div>
		<div class="grid_4 right">
			<h4>Essays</h4>
			<a href="/collections/longitudeessays"><img src="/images/collectionsView/collection-longitude-essays.jpg"
				height="165px" width="165px" /></a>
			<div>
				In July 1714, an act of parliament established a large prize for
				discovery of longitude, the determination of position at sea east or
				west from a fixed meridian line.<br /> <br />
			</div>

		</div>
		<%
			for (int i = 0; i < subCollections.size(); i++) {
				Collection c = subCollections.get(i);
				
				// skip the essays sub collection, that's displayed separately. 
				if (c.getId().equals("longitudeessays")) {
					continue;
				}
		%>

		<div class="featuredcollection grid_3">
			<a href="<%=c.getURL()%>"><img
				src="/images/collectionsView/collection-longitude-<%=c.getId()%>.jpg"
				height="128px" width="128px" /> <span
				class="featuredcollectionlabellong"><%=c.getTitle()%></span></a>
		</div>
		<%
			}
		%>

		<div class="grid_4 box"
			style="height: 545px; border-style: solid; border-color: #FFFFFF; border-left-width: 4px;">Some
			Collection Statistics</div>

		<div class="grid_10">
			<h4>Featured Items</h4>
		</div>
		<div class="grid_10 box" style="background: #333; height: 500px">
			<div class="grid_5">
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
			<div class="grid_4">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00058/309"> <img
							style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00058-000-00252_files/8/0_0.jpg"
							alt="MS-RGO-00014-00058">
						</a>
					</div>
				</div>
			</div>
			<div class="grid_5">
				<font color="#fff">Log book of HMS 'Resolution'<br/>Map of Easter Island<br/><br/></font>
			</div>
			<div class="grid_4">
				<font color="#fff">Log book of HMS 'Resolution'<br/>Map of the Norfolk Islands<br/><br/></font>
			</div>
			<div class="grid_5">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00058/251"> <img
							style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00058-000-00252_files/8/0_0.jpg"
							alt="MS-RGO-00014-00058">
						</a>
					</div>
				</div>
			</div>

			<div class="grid_4">
				<div class="parent_featured_item_image_box">
					<div class="parent_featured_item_image">
						<a href="/view/MS-RGO-00014-00058/24"> <img
							style="height: 100%"
							src="<%=proxyURL%>/content/images/MS-RGO-00014-00058-000-00025_files/8/0_0.jpg"
							alt="MS-RGO-00014-00058">
						</a>
					</div>
				</div>
			</div>
			<div class="grid_5">
				<font color="#fff">Log book of HMS 'Resolution'<br/>Map of the Isles of the Friendly People<br/><br/></font>
			</div>
			<div class="grid_4">
				<font color="#fff">Log book of HMS 'Resolution'<br/>Wales reports sightings of whales , penguins<br/><br/></font>
			</div>
		</div>

		<div class="grid_4 right">
			<h4>Educational Resources</h4>
			<img src="/images/collectionsView/collection-longitude-educationalresources.jpg"
				height="165px" width="165px" />
			<div>In July 1714, an act of parliament established a large
				prize for discovery of longitude, the determination of position at
				sea east or west from a fixed meridian line.</div>

		</div>

	</div>


</div>
