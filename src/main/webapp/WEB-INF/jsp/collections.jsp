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
			<a href="/collections/treasures"> <img class="collectionImage"
				src="/images/collectionsView/collection-treasures.jpg"
				alt="Treasures" width="150" height="150" />
			</a>

			<h5>Treasures of the Library</h5>
			<br /> In this special collection we draw together books,
			manuscripts and other items of particular significance. Many of them
			have been displayed in Library exhibitions in the past – now they can
			be accessed at any time, from anywhere in the world, and browsed
			cover to cover. <br /> <br /> <a href="/collections/treasures">View
				collection</a>.<br />
		</div>
		<br />
		<hr />
		<div class="collection collection-science">

			<a href="/collections/newton"> <img class="collectionImage"
				src="/images/collectionsView/collection-newton.jpg" alt="Newton"
				width="150" height="150" />
			</a>

			<h5>Newton Papers</h5>
			<br /> Cambridge University Library holds the largest and most
			important collection of the scientific works of Isaac Newton
			(1642-1727). We present here an initial selection of Newton's
			manuscripts, concentrating on his mathematical work in the 1660s. <br />
			<br /> <a href="/collections/newton">View collection</a>. <br />
			<div class="foundations-link-science">
				<a href="/about#foundations">Foundations of Science</a>
			</div>
		</div>
		<br />
		<hr />
		<div class="collection collection-faith">

			<a href="/collections/islamic"> <img class="collectionImage"
				src="/images/collectionsView/collection-islamic.jpg" alt="Islamic"
				width="150" height="150" />
			</a>

			<h5>Islamic Manuscripts</h5>
			<br /> The Library's Islamic Manuscripts collection began in the
			1630s and now numbers over 5,000 works, which shed light on many
			aspects of the Islamic world, its beliefs and learning. Our initial
			selection includes several early Qur'anic fragments on parchment. <br />
			<br /> <a href="/collections/islamic">View collection</a>. <br />
			<div class="foundations-link-faith">
				<a href="/about#foundations">Foundations of Faith</a>
			</div>
		</div>
		<br />
		<hr />
		<div class="collection collection-faith">

			<a href="/collections/genizah"> <img class="collectionImage"
				src="/images/collectionsView/collection-genizah.jpg" alt="Genizah"
				width="150" height="150" />
			</a>

			<h5>The Cairo Genizah Collection</h5>
			<br /> The Taylor-Schechter Cairo Genizah Collection at Cambridge
			University Library is the world's largest and most important single
			collection of medieval Jewish manuscripts.<br /> <br /> <a
				href="/collections/genizah">View collection</a>. <br />
			<div class="foundations-link-faith">
				<a href="/about#foundations">Foundations of Faith</a>
			</div>
		</div>
		<br />
		<!--		
		<hr />
		<div class="collection collection-faith">

			<a href="/collections/christian"> <img class="collectionImage"
				src="/images/collectionsView/collection-christian.jpg"
				alt="Christian" width="150" height="150" />
			</a>

			<h5>Christian Manuscripts</h5>
			<br /> The Taylor-Schechter Cairo Genizah Collection at Cambridge
			University Library is the world's largest and most important single
			collection of medieval Jewish manuscripts.<br /> <br /> <a
				href="/collections/christian">View collection</a>. <br />
			<div class="foundations-link-faith">
				<a href="/about#foundations">Foundations of Faith</a>
			</div>
		</div>
		<br />
		<hr />
		<div class="collection collection-faith">

			<a href="/collections/hebrew"> <img class="collectionImage"
				src="/images/collectionsView/collection-hebrew.jpg" alt="Hebrew"
				width="150" height="150" />
			</a>

			<h5>Hebrew Collection</h5>
			<br /> For over five hundred years Cambridge University has been
			building up one of the world’s most important collections of Hebrew
			manuscripts. <br /> <br /> <a
				href="/collections/hebrew">View collection</a>. <br />
			<div class="foundations-link-faith">
				<a href="/about#foundations">Foundations of Faith</a>
			</div>
		</div>
		<br />
-->
		<hr />
		<div class="collection collection-faith">

			<a href="/collections/sanskrit"> <img class="collectionImage"
				src="/images/collectionsView/collection-sanskrit.jpg" alt="Sanskrit"
				width="150" height="150" />
			</a>

			<h5>Sanskrit Manuscripts</h5>
			<br /> A wealth of cultural and historical information on South Asia
			of great potential significance for contemporary scholarship, which
			will be gradually made available through the Digital Library.<br />
			<br /> <a href="/collections/sanskrit">View collection</a>. <br />
			<div class="foundations-link-faith">
				<a href="/about#foundations">Foundations of Faith</a>
			</div>
		</div>
		<br />
		<hr />
		<div class="collection">

			<a href="/collections/spanishchapbooks"> <img
				class="collectionImage"
				src="/images/collectionsView/collection-spanishchapbooks.jpg"
				alt="Spanish Pamphlets" width="150" height="150" />
			</a>

			<h5>Spanish Chapbooks</h5>
			<br />These predecessors of the yellow press provide a fascinating
			bird’s eye view of popular culture from the eighteenth century
			onwards.<br /> <br /> <a href="/collections/spanishchapbooks">View
				collection</a>. <br />
		</div>
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

			<div style="float: left;">
				<div class="collections_carousel_image_box">
					<div class="collections_carousel_image">
						<a href="/view/<%=item.getId()%>/"><img
							alt="<%=item.getId()%>" src="<%=item.getThumbnailURL()%>"
							style="height: 100%" /> </a>
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



