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

					<p>Cambridge University Library has released the first items in
						its Foundations of Faith collection: a selection of Islamic
						manuscripts from its Near and Middle Eastern Department. The
						Library's collection of Islamic manuscripts began in the 1630s and
						has since grown substantially in its size and diversity. It now
						contains more than 5,000 works.</p>

					<p>
						Our <a href="/collections/islamic">online selection</a> includes
						several important early Qur'ans.
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

					<p>The AHRC-funded project “The intellectual and religious
						traditions of South Asia as seen through the Sanskrit manuscript
						collections of the University Library, Cambridge” has begun a
						systematic investigation aiming to produce a full catalogue of the
						manuscripts and to digitise a substantial proportion of them.
						These comprise more than 1,600 works in Sanskrit, Prakrit, Pali,
						Tamil and other ancient and medieval South Asian languages,
						produced over a time-span of more than 1,000 years, and written in
						over a dozen scripts and on different writing materials, such as
						paper, palm leaf and birch bark.</p>
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

					<p>Cambridge University Library has an
						impressive and rich collection of what have been 
						termed ‘no-books’. Usually referred to in English as chapbooks,
						and in Spanish as sueltos, or pliegos sueltos (loose leaves or
						folded loose leaves), these predecessors of the yellow press
						provide a fascinating bird’s eye view of popular culture from the
						eighteenth century onwards. They show us, among other things,
						versions of how forms of wrongdoing (of different kinds, and of
						different degrees of severity) were perceived, or were presented
						to the populace as constructed forms of wrongdoing.</p>
				</div> <a href="/collections/spanishchapbooks"><img
					src="/images/index/carousel-spanishchapbooks.jpg"
					alt="Spanish Chapbooks" width="540" height="394" /> </a>
			</li>
		</ol>
	</div>


</div>

<!-- cookie notice -->
<div style="display: none">
	<div id="cookienotice">Lorem ipsum dolor sit amet, consectetur
		adipiscing elit.</div>
</div>

<jsp:include page="footer/footer.jsp" />


