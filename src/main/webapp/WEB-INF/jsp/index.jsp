<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-home.jsp" />

<script type="text/javascript">
	var index_carousel;
	function init() {
		index_carousel = new glow.widgets.Carousel("#index_carousel", {
			loop : true,
			size : 1,
			step : 1,
			theme : "light"
		});
		
		// Goto a random item in the carousel
		var randomnumber=Math.floor(Math.random()*(index_carousel._countRealItems));
		index_carousel.moveTo(randomnumber, false);
	}
</script>

<div id="content" class="grid_20 content">

	<!-- side panel -->
	<div class="grid_6" style="margin-bottom: 18px;">

		<div id="news" class="panel light">

			<div class="featured-item-list">
				<a href="/collections/treasures"><img alt="Treasures of the Library"
					title="Treasures of the Library" src="/images/index/slice-treasures.jpg"> </a>

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
				<a href="/collections/genizah"><img alt="Cairo Genizah Collection"
					title="Cairo Genizah Collection" src="/images/index/slice-genizah.jpg"> </a>
					
				<h4>
					<a href="/collections/genizah">Cairo Genizah Collection</a>
				</h4>
			</div>

			<!-- <a href="" onclick="index_carousel.moveTo(1,true);return false;">Islamic Manuscripts</a> -->

		</div>
	</div>

	<div class="grid_13">

		<ol id="index_carousel">

			<!-- treasures collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/treasures">Treasures of the Library</a>
					</h4>

					<p>Treasures.</p>

				</div> <a href="/collections/treasures"><img
					src="/images/index/carousel-treasures.jpg" alt="Treasures of the Library"
					width="540" height="394" /> </a></li>
					
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
						This first release features some of Newton's most important work
						from the 1660s, including his <a href="/collections/newton">college
							notebooks</a> and '<a href="/view/MS-ADD-04004/">Waste Book</a>'.
					</p>
				</div> <a href="/collections/newton"><img id="newtonImage"
					src="/images/index/carousel-newton.jpg" alt="Newton Papers"
					width="540" height="394" /> </a></li>


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
						Our <a href="/collections/islamic">initial selection</a> includes
						several important early Qur'ans.
					</p>
				</div> <a href="/collections/islamic"><img
					src="/images/index/carousel-islamic.jpg" alt="Islamic Manuscripts"
					width="540" height="394" /> </a></li>

			<!-- The Cairo Genizah collection -->
			<li>
				<div class="panel" style="overflow: auto;">
					<h4>
						<a href="/collections/genizah">The Cairo Genizah Collection</a>
					</h4>

					<p>The Taylor-Schechter Cairo Genizah Collection at Cambridge
			University Library is the world's largest and most important single
			collection of medieval Jewish manuscripts. For a thousand years, the
			Jewish community of Fustat (Old Cairo), placed their left-over,
			no-longer usable manuscripts in a storeroom (genizah) of the Ben Ezra
			Synagogue, and in 1896–97 the Cambridge scholar, Dr Solomon
			Schechter, arrived to examine it. He received permission from
			the Jewish community of Egypt to take away what he wanted, so he
			brought 193,000 manuscripts back to Cambridge, where they form the
			Taylor-Schechter Cairo Genizah Collection.</p>
				</div> <a href="/collections/genizah"><img 
					src="/images/index/carousel-genizah.jpg" alt="The Cairo Genizah Collection"
					width="540" height="394" /> </a></li>
		</ol>
	</div>

</div>

<jsp:include page="footer/footer.jsp" />

