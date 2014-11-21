<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="0" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<%
	String downtimeWarning = (String) request
			.getAttribute("downtimeWarning");
	String itemCount = (String) request.getAttribute("itemCount");
	List<Collection> rootCollections = (List<Collection>) request.getAttribute("rootCollections");
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
			theme : "light",
			pageNav : true

		});

		// Goto a random item in the carousel
		var randomnumber = Math.floor(Math.random()
				* (index_carousel._countRealItems));
		index_carousel.moveTo(randomnumber, false);

		// auto-scroll through items until the carousel is clicked. 
		var intervalId = window.setInterval(function() {
			index_carousel.next();
		}, 7000);
		
		// Need to check for addEventListener as is not supported in ie8. 
		if (document.getElementById("index_carousel_parent").addEventListener) {
			document.getElementById("index_carousel_parent").addEventListener(
				"click", function(event) {
					window.clearInterval(intervalId);
				});
		} else {
			document.getElementById("index_carousel_parent").attachEvent(
					"onclick", function(event) {
						window.clearInterval(intervalId);
					});
		}

		$(".collection_gallery").fancybox();
	}
</script>

<%=downtimeWarning%>

<div id="content" class="grid_20">

	<!-- side panel -->


	<div id="index_carousel_parent">

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
					alt="Treasures of the Library" width="944" height="394" /> </a>
			</li>

			<!-- darwin-hooker collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/darwinhooker">Darwin-Hooker Letters</a>
					</h4>

					<p>No single set of letters was more important to Darwin than
						those exchanged with his closest friend, the botanist Joseph
						Dalton Hooker.</p>
					<p>
						The <a href="/collections/darwinhooker">1,200 letters</a>
						published here are a connecting thread that spans forty years of
						Darwin's mature working life from 1843 until his death in 1882.
						They bring into sharp focus every aspect of Darwin's scientific
						work throughout that period, and illuminate the mutual friendships
						he and Hooker shared with other scientists, but they also provide
						a window of unparalleled intimacy into the personal lives of the
						two men.
					</p>

				</div> <a href="/collections/darwinhooker"><img id="darwinhookerImage"
					src="/images/index/carousel-darwinhooker.jpg"
					alt="Darwin-Hooker Letters" width="944" height="394" /> </a>
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
					width="944" height="394" /> </a>
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
					alt="The Cairo Genizah Collection" width="944" height="394" /> </a>
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
					width="944" height="394" /> </a>
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
					width="944" height="394" /> </a>
			</li>
			
			<!-- christian works -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/christian">Christian Works</a>
					</h4>

					<p>
						A selection of some of our finest Christian manuscripts and early
						printed books, from the Bible to the liturgy, spanning over 1000
						years of worship and debate. From the early bilingual New
						Testament known as the <a href="/view/MS-NN-00002-00041/1">Codex
							Bezae</a> to the tenth-century pocket Gospel Book the <a
							href="/view/MS-II-00006-00032/1">Book of Deer</a>, and from a
						beautifully illuminated sixteenth-century <a
							href="/view/MS-NN-00004-00001/1">book of services and
							ceremonies</a> commissioned for a Cistercian abbot in Northern
						Flanders to a sumptuous <a href="/view/MS-EE-00003-00059/1">Life
							of St Edward the Confessor</a> produced for royalty in
						mid-thirteenth-century England, the collection includes works of
						scripture, theology and liturgy, reflecting both Church ceremonial
						and private devotion.
					</p>

				</div> <a href="/collections/christian"><img
					src="/images/index/carousel-christian.jpg" alt="Christian Works"
					width="944" height="394" /> </a>
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
						produced over a time-span of more than 1,000 years.
					</p>
				</div> <a href="/collections/sanskrit"><img
					src="/images/index/carousel-sanskrit.jpg"
					alt="Sanskrit Manuscripts" width="944" height="394" /> </a>
			</li>
			
			
			<!-- The Darwin Manuscripts -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/darwin_mss">Darwin Manuscripts</a>
					</h4>

					<p>
						The <i>Charles Darwin Papers</i> in the Manuscripts Department of Cambridge University Library hold nearly the entire extant collection of Darwin’s working scientific papers.
				Paramount among these documents are <i>Charles Darwin’s Evolution Manuscripts</i>, which are being published online at the Cambridge Digital Library and simultaneously at the
				<a href="http://www.amnh.org/our-research/darwin-manuscripts-project">Darwin Manuscripts Project</a> in collaboration with the
				<a href="http://www.darwinproject.ac.uk/">Darwin Correspondence Project</a>.
					</p>
				</div> <a href="/collections/darwin_mss"><img
					src="/images/index/carousel-darwin_mss.jpg"
					alt="Darwin Manuscripts" width="944" height="394" /> </a>
			</li>
			
			
			<!-- The Japanese Works collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/japanese">Japanese Works</a>
					</h4>

					<p>
						Japan possesses an exceptionally rich heritage of written culture founded in its long literary tradition and history of paper production, and in its connections with the civilizations of neighbouring China and Korea. Cambridge University Library holds an extremely important collection of early Japanese books and manuscripts, representing a wide range of the various aspects of early Japanese learning and excellent samples of Japan’s early printing culture.
					</p>
				</div> <a href="/collections/japanese"><img
					src="/images/index/carousel-japanese.jpg"
					alt="Japanese Works" width="944" height="394" /> </a>
			</li>
			
			
			<!-- The Music collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/music">Music</a>
					</h4>

					<p>
						The music collections at Cambridge University Library play an important role in the preservation of musical heritage, both on a national and international level. From printed and manuscript music scores and texts on music to ephemera and concert programmes, archival materials documenting the life and work of composers, music scholars and performers, a huge variety and breadth of material has been collected over several centuries. We launch with a selection of important early lute music, digitised and described in collaboration with the <href="http://www.lutesociety.org/">Lute Society</a>
					</p>
				</div> <a href="/collections/music"><img
					src="/images/index/carousel-music.jpg"
					alt="Music" width="944" height="394" /> </a>
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
					alt="Spanish Chapbooks" width="944" height="394" /> </a>
			</li>

			<!-- longitude collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/longitude">Board of Longitude</a>
					</h4>

					<p>
						The archives of the Royal Greenwich Observatory include the complete run of the papers
						of the Board of Longitude through the eighteenth century until its
						abolition in 1828. These papers throw a vivid light on the role of
						the British state in encouraging invention and discovery, on the
						energetic culture of technical ingenuity in the long eighteenth
						century, and on many aspects of exploration and maritime travel in
						the Pacific Ocean and the Arctic.
					</p>


				</div> <a href="/collections/longitude"><img
					src="/images/index/carousel-longitude.jpg" alt="Board of Longitude"
					width="944" height="394" /> </a>
			</li>
			
			<!-- peterhouse collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/peterhouse">Peterhouse Manuscripts</a>
					</h4>

					<p>
						Peterhouse (founded 1284) is the oldest of the Cambridge Colleges.
						As part of the process of recataloguing and preserving the College's collections, it has been decided to present highlights from the Peterhouse manuscripts in digital form.
						The first manuscript selected for this purpose is the Equatorie of the Planetis (Peterhouse Ms. 75.1).
						As funding becomes available, further manuscripts will be added to the online collection.
					</p>


				</div> <a href="/collections/peterhouse"><img
					src="/images/index/carousel-peterhouse.jpg" alt="Peterhouse"
					width="944" height="394" /> </a>
			</li>
			
			<!-- sassoon collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/sassoon">Sassoon Journals</a>
					</h4>

					<p>
						The notebooks kept by the soldier-poet Siegfried Sassoon (1886–1967) during
						his service in the British Army in the First World War are among the most remarkable documents of their kind, and provide
						an extraordinary insight into his participation in one of the defining conflicts of European history.
					</p>


				</div> <a href="/collections/sassoon"><img
					src="/images/index/carousel-sassoon.jpg" alt="Sassoon Journals"
					width="944" height="394" /> </a>
			</li>
			
		</ol>

	</div>

	<div class="grid_10" style="margin-bottom: 18px;">

		<h4>Collections</h4>

		<%
			for (int i=0; i<rootCollections.size(); i++) { 
				       Collection c = rootCollections.get(i);
		%>

		<div class="featuredcollection grid_3">
			<a href="<%=c.getURL()%>"><img
				src="/images/collectionsView/collection-<%=c.getId()%>.jpg"
				height="128px" width="128px" /> <span
				class="featuredcollectionlabel"><%=c.getTitle()%></span></a>
		</div>
		<%
			}
		%>

		<div class="grid_9">
			<br /> <a class="right" href="/collections/">View all
				collections &gt;</a>
		</div>
	</div>


	<div class="grid_9" style="margin-bottom: 18px;">

		<h4>Search</h4>
		<div class="searchform box">
			<form class="grid_5" action="/search">
				<input class="search" type="text" autocomplete="off"
					placeholder="Search" name="keyword" value=""> <input
					id="submit" type="submit" value="Search"> <input
					type="hidden" value="" name="fileID">
			</form>
			<br /> <br /> <b><%=itemCount%></b> books and manuscripts now
			online.

		</div>


		<!--  news -->
		<div>
			<h4>Latest News</h4>
			<div class="grid_3">
				<a href="/news"> <img
					alt="Darwin Manuscripts"
					title="November Update"
					src="/images/index/news-darwin_mss.jpg" height="100" width="100"></a>
			</div>

			<div>
				<span class="date">24/11/2014</span>
				<h5>
					<a href="/news">November Release</a></h5>
					<p>Our November release sees the launch of our <a href='http://cudl.lib.cam.ac.uk/collections/darwin_mss'>Darwin Manuscripts</a> collection, with a selection of papers charting the development of Darwin’s evolutionary theory - from early theoretical reflections made while on board HMS Beagle to the publication of <i>On the Origin of Species</i>. Amongst many highlights are documents of worldwide importance such as the “Transmutation” and “Metaphysical” notebooks of the 1830s and the 1842 “Pencil Sketch” which sees Darwin’s first use of the term <a href='http://cudl.lib.cam.ac.uk/view/MS-DAR-00006/41'>“natural selection”</a>.</p>
			</div>
		</div>

		<!-- twitter feed -->
		<a class="twitter-timeline" href="https://twitter.com/CamDigLib"
			data-widget-id="309321526665154560">Tweets by @CamDigLib</a>
		<script>
			!function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (!d.getElementById(id)) {
					js = d.createElement(s);
					js.id = id;
					js.src = "//platform.twitter.com/widgets.js";
					fjs.parentNode.insertBefore(js, fjs);
				}
			}(document, "script", "twitter-wjs");
		</script>

		<!-- end of twitter feed -->
	</div>





</div>


<jsp:include page="footer/footer.jsp" />
