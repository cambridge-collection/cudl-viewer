<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory,java.util.List"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="0" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<%
	List<Item> featuredItems = (List<Item>) request
	.getAttribute("featuredItems");
	String downtimeWarning = (String) request
	.getAttribute("downtimeWarning");
%>

<script type="text/javascript">
	var index_carousel;

	function pageinit() {
		// display image carousel
		document.getElementById("index_carousel").style.display = "block";

		index_carousel = new glow.widgets.Carousel("#index_carousel", {
			loop : true,
			size : 1,
			step : 1,
			theme : "light"
		});

		// Goto a random item in the carousel
		var randomnumber = Math.floor(Math.random()
				* (index_carousel._countRealItems));
		index_carousel.moveTo(randomnumber, false);

		// generate random featuredItem banner
		var bannerItems = [ "MS-II-00006-00032", "MS-NN-00003-00074" ];

		var randomitem = Math.floor(Math.random() * (bannerItems.length));
		var bannerItem = bannerItems[randomitem];
		document.getElementById(bannerItem).style.display = "inline";
	}
</script>

<%=downtimeWarning%>

<div id="content" class="grid_20 content">

	<!-- side panel -->
	<div class="grid_6" style="margin-bottom: 18px;">

		<div id="featuredCollections">

			<div class="featured-item-list">
				<a href="/collections/treasures"><img
					alt="Treasures of the Library" title="Treasures of the Library"
					src="/images/index/slice-treasures.jpg"> </a>

				<h4>
					<a href="/collections/treasures">Treasures of the Library</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/newton"><img alt="Newton Papers"
					title="Newton Papers" src="/images/index/slice-newton.jpg"> </a>
				<h4>
					<a href="/collections/newton">Newton Papers</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/islamic"><img alt="Islamic Manuscripts"
					title="Islamic Manuscripts" src="/images/index/slice-islamic.jpg">
				</a>

				<h4>
					<a href="/collections/islamic">Islamic Manuscripts</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/genizah"><img
					alt="Cairo Genizah Collection" title="Cairo Genizah Collection"
					src="/images/index/slice-genizah.jpg"> </a>

				<h4>
					<a href="/collections/genizah">Cairo Genizah Collection</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/sanskrit"><img alt="Sanskrit Manuscripts"
					title="Sanskrit Manuscripts" src="/images/index/slice-sanskrit.jpg">
				</a>

				<h4>
					<a href="/collections/sanskrit">Sanskrit Manuscripts</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/spanishchapbooks"><img
					alt="Spanish Chapbooks" title="Spanish Chapbooks"
					src="/images/index/slice-spanishchapbooks.jpg"> </a>

				<h4>
					<a href="/collections/spanishchapbooks">Spanish Chapbooks</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/hebrew"><img alt="Hebrew Manuscripts"
					title="Hebrew Manuscripts" src="/images/index/slice-hebrew.jpg">
				</a>

				<h4>
					<a href="/collections/hebrew">Hebrew Manuscripts</a>
				</h4>
			</div>

			<div class="featured-item-list">
				<a href="/collections/christian"><img alt="Christian Works"
					title="Christian Works" src="/images/index/slice-christian.jpg">
				</a>

				<h4>
					<a href="/collections/christian">Christian Works</a>
				</h4>
			</div>

			<!-- <a href="" onclick="index_carousel.moveTo(1,true);return false;">Islamic Manuscripts</a> -->

		</div>
	</div>

	<div class="grid_13" id="index_carousel_parent">

		<ol id="index_carousel" style="display: none;">

			<!-- treasures collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/treasures">Treasures of the Library</a>
					</h4>

					<p>Many items within the Library’s collections deserve to be
						highlighted. This may be because of their historical importance,
						uniqueness, beauty, fascinating content, or perhaps their personal
						associations. In this special collection within the Cambridge
						Digital Library we will draw together books, manuscripts and other
						items from across our collections that are especially significant.
						Many of them have been displayed in Library exhibitions in the
						past – now they can be accessed at any time, from anywhere in the
						world, and browsed cover to cover.</p>

				</div> <a href="/collections/treasures"><img
					src="/images/index/carousel-treasures.jpg"
					alt="Treasures of the Library" width="540" height="394" /> </a>
			</li>

			<!-- newton collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/newton">Newton Papers</a>
					</h4>

					<p>Cambridge University Library is pleased to present the first
						items in its Foundations of Science collection: a selection from
						the Papers of Sir Isaac Newton. The Library holds the most
						important and substantial collection of Newton's scientific and
						mathematical manuscripts and over the next few months we intend to
						make most of our Newton papers available on this site.</p>

					<p>
						This collection features some of Newton's most important work from
						the 1660s, including his <a href="/collections/newton">college
							notebooks</a> and '<a href="/view/MS-ADD-04004/">Waste Book</a>'.
					</p>
				</div> <a href="/collections/newton"><img id="newtonImage"
					src="/images/index/carousel-newton.jpg" alt="Newton Papers"
					width="540" height="394" /> </a>
			</li>


			<!-- islamic collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/islamic">Islamic Manuscripts</a>
					</h4>

					<p>
						Islamic manuscripts were first added to the Library’s collections
						in the 1630s and since then, the holdings have grown substantially
						both in size and diversity. A number of our religious and literary
						texts, outstanding for their content, rarity, and historical
						value, have been added to the Library’s ‘Foundations of Faith’
						collection; many of these examples also have beautiful illuminated
						pages. Our <a href="/collections/islamic">online selection</a> 
						also includes examples of some of the earliest existing Qur'anic
						fragments which are known to date from the first centuries of the
						Hijra.
					</p>

				</div> <a href="/collections/islamic"><img
					src="/images/index/carousel-islamic.jpg" alt="Islamic Manuscripts"
					width="540" height="394" /> </a>
			</li>

			<!-- The Cairo Genizah collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/genizah">The Cairo Genizah Collection</a>
					</h4>

					<p>The Taylor-Schechter Cairo Genizah Collection at Cambridge
						University Library is the world's largest and most important
						single collection of medieval Jewish manuscripts.</p>
					<p>For a thousand years, the Jewish community of Fustat (Old
						Cairo), placed their worn-out books and other writings in a
						storeroom (genizah) of the Ben Ezra Synagogue, and in 1896–97 the
						Cambridge scholar, Dr Solomon Schechter, arrived to examine it. He
						received permission from the Jewish community of Egypt to take
						away what he liked, and he brought 193,000 manuscripts back to
						Cambridge.</p>

				</div> <a href="/collections/genizah"><img
					src="/images/index/carousel-genizah.jpg"
					alt="The Cairo Genizah Collection" width="540" height="394" /> </a>
			</li>

			<!-- The Sanskrit collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/sanskrit">Sanskrit Manuscripts</a>
					</h4>

					<p>
						The AHRC-funded project “The intellectual and religious traditions
						of South Asia as seen through the Sanskrit manuscript collections
						of the University Library, Cambridge” has begun a systematic
						investigation aiming to produce a full catalogue of the
						manuscripts and to digitise a substantial proportion of them.
						These comprise more than 1,600 works in Sanskrit, Prakrit, Pali,
						Tamil and other ancient and medieval South Asian languages,
						produced over a time-span of more than 1,000 years. We present an
						<a href="/collections/sanskrit">initial selection</a> from the
						collection to demonstrate its richness.
					</p>
				</div> <a href="/collections/sanskrit"><img
					src="/images/index/carousel-sanskrit.jpg"
					alt="Sanskrit Manuscripts" width="540" height="394" /> </a>
			</li>

			<!-- The Spanish Chapbooks -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/spanishchapbooks">Spanish Chapbooks</a>
					</h4>

					<p>Cambridge University Library has an impressive and rich
						collection of what have been termed ‘no-books’. Usually referred
						to in English as chapbooks, and in Spanish as sueltos, or pliegos
						sueltos (loose leaves or folded loose leaves), these predecessors
						of the yellow press provide a fascinating bird’s eye view of
						popular culture from the eighteenth century onwards. They show us,
						among other things, versions of how forms of wrongdoing (of
						different kinds, and of different degrees of severity) were
						perceived, or were presented to the populace as constructed forms
						of wrongdoing.</p>
				</div> <a href="/collections/spanishchapbooks"><img
					src="/images/index/carousel-spanishchapbooks.jpg"
					alt="Spanish Chapbooks" width="540" height="394" /> </a>
			</li>

			<!-- Hebrew Manuscripts -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/hebrew">Hebrew Manuscripts</a>
					</h4>

					<p>Cambridge University Library holds one of the world’s
						foremost collections of Hebrew manuscripts, on account of the
						University’s long interest in the literature of Judaism. Chief
						among them are the famous Nash Papyrus, one of the earliest known
						artefacts containing the words of the Hebrew Bible, and the
						Cambridge Mishnah, a complete codex of this work copied in
						fifteenth-century Byzantium. With 1000 items acquired over more
						than 500 years, the Collection ranges wide from Samaritan Hebrew
						Bibles to important compositions of halakha and exegesis, through
						to manuscripts of philosophical, kabbalistic and scientific works.</p>

				</div> <a href="/collections/hebrew"><img
					src="/images/index/carousel-hebrew.jpg" alt="Hebrew Manuscripts"
					width="540" height="394" /> </a>
			</li>

			<!-- christian works -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/christian">Christian Works</a>
					</h4>

					<p>A selection of some of our finest Christian manuscripts and
						early printed books, from the Bible to the liturgy, spanning over
						1000 years of worship and debate. From the early bilingual New
						Testament known as the <a href="/view/MS-NN-00002-00041/1">Codex Bezae</a> to the tenth-century pocket
						Gospel Book the <a href="/view/MS-II-00006-00032/1">Book of Deer</a>, and from a beautifully illuminated
						sixteenth-century <a href="/view/MS-NN-00004-00001/1">book of services and ceremonies</a> commissioned for
						a Cistercian abbot in Northern Flanders to a sumptuous <a href="/view/MS-EE-00003-00059/1">Life of St
						Edward the Confessor</a> produced for royalty in
						mid-thirteenth-century England, the collection includes works of
						scripture, theology and liturgy, reflecting both Church ceremonial
						and private devotion.</p>

				</div> <a href="/collections/christian"><img
					src="/images/index/carousel-christian.jpg" alt="Christian Works"
					width="540" height="394" /> </a>
			</li>
		</ol>
	</div>
	<!--  featured items -->
	<ol>
		<li><div id="MS-II-00006-00032" class="grid_13"
				style="display: none; height: 96px">
				<a href="/view/MS-II-00006-00032/1"><img
					src="images/index/banner-bookofdeer.jpg" /></a>
			</div></li>
		<li><div id="MS-NN-00003-00074" class="grid_13"
				style="display: none; height: 96px">
				<a href="/view/MS-NN-00003-00074/1"><img
					src="images/index/banner-islamicitem.jpg" /></a>
			</div></li>
	</ol>


</div>

<jsp:include page="footer/footer.jsp" />


