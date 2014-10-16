<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory,java.util.List"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<%
	List<Item> featuredItems = (List<Item>) request
			.getAttribute("featuredItems");
%>

<div class="clear"></div>

<section id="content" class="grid_20 content">
	<h3 style="margin-left: 8px;">Our Collections</h3>

	<div class="grid_13">

		<div class="collection">
			<div class="grid_4">
				<a href="/collections/treasures"> <img class="collectionImage"
					src="/images/collectionsView/collection-treasures.jpg"
					alt="Treasures" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Treasures of the Library</h5>
				<br /> In this special collection we draw together books,
				manuscripts and other items of particular significance. Many of them
				have been displayed in Library exhibitions in the past – now they
				can be accessed at any time, from anywhere in the world, and browsed
				cover to cover. <br /> <br /> <a href="/collections/treasures">View
					collection</a>.<br /> <br />
			</div>
		</div>
		<hr />

		<div class="collection collection-science">
			<div class="grid_4">
				<a href="/collections/longitude"> <img class="collectionImage"
					src="/images/collectionsView/collection-longitude.jpg"
					alt="Board of Longitude" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Board of Longitude</h5>
				<br /> Cambridge University Library holds the entire archive of the
				Board of Longitude, the state organization established in the
				eighteenth century to evaluate methods for finding ships’ position
				and to encourage a host of projects in instrument design,
				clock-making, mapping and voyaging.<br /> <br /> <a
					href="/collections/longitude">View collection</a>.<br />
				<div class="foundations-link-science">
					<a href="/about#foundations">Foundations of Science<br /> <br /></a>
				</div>
				<br /> <br />

			</div>
		</div>
		<hr />
		<div class="collection collection-science">
			<div class="grid_4">
				<a href="/collections/newton"> <img class="collectionImage"
					src="/images/collectionsView/collection-newton.jpg" alt="Newton"
					width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Newton Papers</h5>
				<br /> Cambridge University Library holds the largest and most
				important collection of the scientific works of Isaac Newton
				(1642-1727). We present here an initial selection of Newton's
				manuscripts, concentrating on his mathematical work in the 1660s. <br />
				<br /> <a href="/collections/newton">View collection</a>. <br />
				<div class="foundations-link-science">
					<a href="/about#foundations">Foundations of Science</a>
				</div>
				<br />
			</div>
		</div>
		<hr />

		<div class="collection collection-science">
			<div class="grid_4">
				<a href="/collections/darwinhooker"> <img
					class="collectionImage"
					src="/images/collectionsView/collection-darwinhooker.jpg"
					alt="Darwin-Hooker Letters" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Darwin-Hooker Letters</h5>
				<br /> These letters are a connecting thread that spans forty years
				of Darwin's mature working life from 1843 until his death in 1882.
				They bring into sharp focus every aspect of Darwin's scientific work
				throughout that period, and illuminate the mutual friendships he and
				Hooker shared with other scientists, but they also provide a window
				of unparalleled intimacy into the personal lives of the two men. <br />
				<br /> <a href="/collections/darwinhooker">View collection</a>. <br />
				<div class="foundations-link-science">
					<a href="/about#foundations">Foundations of Science</a>
				</div>
				<br />
			</div>
		</div>
		<hr />
		
		
		<div class="collection">
			<div class="grid_4">
				<a href="/collections/sassoon"> <img
					class="collectionImage"
					src="/images/collectionsView/collection-sassoon.jpg"
					alt="Sassoon Journals" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Sassoon Journals</h5>
				<br /> The notebooks kept by the soldier-poet Siegfried Sassoon (1886–1967) during
				his service in the British Army in the First World War are among the most remarkable documents of their kind, and provide
				an extraordinary insight into his participation in one of the defining conflicts of European history. <br />
				<br /> <a href="/collections/sassoon">View collection</a>. <br />
				
				<br />
			</div>
		</div>
		<hr />
		

		<div class="collection collection-faith">
			<div class="grid_4">
				<a href="/collections/genizah"> <img class="collectionImage"
					src="/images/collectionsView/collection-genizah.jpg" alt="Genizah"
					width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>The Cairo Genizah Collection</h5>
				<br /> The Taylor-Schechter Cairo Genizah Collection at Cambridge
				University Library is the world's largest and most important single
				collection of medieval Jewish manuscripts.<br /> <br /> <a
					href="/collections/genizah">View collection</a>. <br />
				<div class="foundations-link-faith">
					<a href="/about#foundations">Foundations of Faith</a>
				</div>
				<br /> <br />
			</div>
		</div>

		<hr />

		<div class="collection collection-faith">
			<div class="grid_4">
				<a href="/collections/hebrew"> <img class="collectionImage"
					src="/images/collectionsView/collection-hebrew.jpg" alt="Hebrew"
					width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Hebrew Collection</h5>
				<br /> For over five hundred years Cambridge University has been
				building up one of the world’s most important collections of Hebrew
				manuscripts. <br /> <br /> <a href="/collections/hebrew">View
					collection</a>. <br />
				<div class="foundations-link-faith">
					<a href="/about#foundations">Foundations of Faith</a>
				</div>
				<br /> <br />
			</div>
		</div>

		<hr />

		<div class="collection collection-faith">
			<div class="grid_4">
				<a href="/collections/islamic"> <img class="collectionImage"
					src="/images/collectionsView/collection-islamic.jpg" alt="Islamic"
					width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Islamic Manuscripts</h5>
				<br /> The Library's Islamic Manuscripts collection began in the
				1630s and now numbers over 5,000 works, which shed light on many
				aspects of the Islamic world, its beliefs and learning. Our initial
				selection includes several early Qur'anic fragments on parchment. <br />
				<br /> <a href="/collections/islamic">View collection</a>. <br />
				<div class="foundations-link-faith">
					<a href="/about#foundations">Foundations of Faith</a>
				</div>
				<br />
			</div>
		</div>

		<hr />

		<div class="collection collection-faith">
			<div class="grid_4">
				<a href="/collections/christian"> <img class="collectionImage"
					src="/images/collectionsView/collection-christian.jpg"
					alt="Christian" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Christian Works</h5>
				<br /> A selection of some of our finest Christian manuscripts and
				early printed books, from the Bible to the liturgy, spanning over
				1000 years of worship and debate.<br /> <br /> <a
					href="/collections/christian">View collection</a>. <br />
				<div class="foundations-link-faith">
					<a href="/about#foundations">Foundations of Faith</a>
				</div>
				<br /> <br />
			</div>
		</div>

		<hr />

		<div class="collection collection-faith">
			<div class="grid_4">
				<a href="/collections/sanskrit"> <img class="collectionImage"
					src="/images/collectionsView/collection-sanskrit.jpg"
					alt="Sanskrit" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Sanskrit Manuscripts</h5>
				<br />
				A full catalogue of our Sanskrit manuscript collection, comprising more than 1,600 works in Sanskrit, Prakrit, Pali, Tamil and other ancient and medieval South Asian languages, produced over a time-span of
				more than 1,000 years.
				
				<br /> <br /> <a href="/collections/sanskrit">View
					collection</a>. <br />
				<div class="foundations-link-faith">
					<a href="/about#foundations">Foundations of Faith</a>
				</div>
				<br /> <br />
			</div>
		</div>

		<hr />
		
		
		<div class="collection">
			<div class="grid_4">
				<a href="/collections/japanese"> <img class="collectionImage"
					src="/images/collectionsView/collection-japanese.jpg"
					alt="Japanese" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Japanese Works</h5>
				<br /> Items from Cambridge University Library's important collection of early Japanese books and manuscripts, representing a wide range of the various aspects of early Japanese learning
				and excellent samples of Japan’s early printing culture.<br /> <br /> <a href="/collections/japanese">View collection</a>. <br />
				
				<br /> <br />
			</div>
		</div>

		<hr />

		<div class="collection">
			<div class="grid_4">
				<a href="/collections/peterhouse"> <img class="collectionImage"
					src="/images/collectionsView/collection-peterhouse.jpg"
					alt="Peterhouse" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Peterhouse Manuscripts</h5>
				<br /> Peterhouse (founded 1284) is the oldest of the Cambridge Colleges, and holds a significant collection of medieval manuscripts.
				As part of the process of recataloguing and preserving the College's collections, it has been decided to present highlights from the
				Peterhouse manuscripts in digital form.
				<br /> <br /> <a href="/collections/peterhouse">View
					collection</a>. <br />
				<br />
				
			</div>
		</div>
		
		<hr />


		<div class="collection">
			<div class="grid_4">
				<a href="/collections/spanishchapbooks"> <img
					class="collectionImage"
					src="/images/collectionsView/collection-spanishchapbooks.jpg"
					alt="Spanish Pamphlets" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Spanish Chapbooks</h5>
				<br />These predecessors of the yellow press provide a fascinating bird’s eye view of Spanish popular culture from the eighteenth century onwards.<br /> <br />
				<a href="/collections/spanishchapbooks">View collection</a>. <br />
			</div>
		</div>
		<br />
		<hr />

		<div class="collection">
			<div class="grid_4">
				<a href="/collections/exhibitions"> <img class="collectionImage"
					src="/images/collectionsView/collection-exhibitions.jpg"
					alt="Exhibition Items" width="150" height="150" />
				</a>
			</div>
			<div class="grid_8">
				<h5>Exhibition Items</h5>
				<br />The Library holds regular exhibitions, usually organised
				around a particular theme or collection. Depending on the chosen
				theme, the exhibition cases might contain anything from
				irreplaceable works of world importance to newspapers or printed
				ephemera. They demonstrate the breadth of the Library's holdings and
				often yield surprises.<br /> <br /> <a
					href="/collections/exhibitions">View collection</a>. <br /> <br />

			</div>
		</div>
		<br />

	</div>

	<div class="grid_6 box">
		<h4>Featured Items</h4>
		<div class="featuredItem grid_6">

			<%
				for (int i = 0; i < featuredItems.size(); i++) {
					Item item = featuredItems.get(i);
			%>
			<div class="featuredItem-text">
				<h5>
					<a href="/view/<%=item.getId()%>"><%=item.getTitle()%></a>
				</h5>
			</div>

			<%
				String imageDimensions = "";
					if (item.getThumbnailOrientation().trim().equals("portrait")) {
						imageDimensions = " style='height:100%' ";
					} else if (item.getThumbnailOrientation().trim()
							.equals("landscape")) {
						imageDimensions = " style='width:100%' ";
					}
			%>
			<div style="float: left;">
				<div class="collections_carousel_image_box">
					<div class="collections_carousel_image">
						<a href="/view/<%=item.getId()%>/"><img
							alt="<%=item.getId()%>" src="<%=item.getThumbnailURL()%>"
							<%=imageDimensions%> /> </a>
					</div>
				</div>
			</div>

			<%
				}
			%>
		</div>
	</div>
</section>

<jsp:include page="footer/footer.jsp" />



