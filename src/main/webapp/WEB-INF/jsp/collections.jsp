<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-browse.jsp" />

<script type="text/javascript">
	/*	function pageinit() {
	 var collections_carousel = new glow.widgets.Carousel("#collections_carousel", {
	 loop : true,
	 size : 3,
	 step : 3,
	 vertical : true,
	 pageNav : true
	 });
	 }
	 */
</script>

<div class="clear"></div>

<section id="content" class="grid_20 content">
<h3 style="margin-left: 8px;">Our Collections</h3>

<div class="grid_13">

	<div class="collection">
		<a href="/collections/treasures"> <img class="collectionImage"
			src="/images/collectionsView/collection-treasures.jpg"
			alt="Treasures" width="150" height="150" /> </a>

		<h5>Treasures of the Library</h5>
		<br /> Text. <br /> <br /> <a href="/collections/treasures">View
			collection</a>.<br />
	</div>

	<div class="collection collection-science">

		<a href="/collections/newton"> <img class="collectionImage"
			src="/images/collectionsView/collection-newton.jpg" alt="Newton"
			width="150" height="150" /> </a>

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
	<div class="collection collection-faith">

		<a href="/collections/islamic"> <img class="collectionImage"
			src="/images/collectionsView/collection-islamic.jpg" alt="Islamic"
			width="150" height="150" /> </a>

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
	<div class="collection collection-faith">

		<a href="/collections/genizah"> <img class="collectionImage"
			src="/images/collectionsView/collection-genizah.jpg" alt="Genizah"
			width="150" height="150" /> </a>

		<h5>The Cairo Genizah Collection</h5>
		<br /> The Taylor-Schechter Cairo Genizah Collection at Cambridge
		University Library is the world's largest and most important single
		collection of medieval Jewish manuscripts.<br /> <br /> <a
			href="/collections/genizah">View collection</a>. <br />
		<div class="foundations-link-faith">
			<a href="/about#foundations">Foundations of Faith</a>
		</div>
	</div>
</div>

<div class="grid_6">
	<h4>Featured Items</h4>
	<div class="featuredItem">

		<!--  3975  -->
		<div class="featuredItem-text">
			<h5>
				<a href="/view/MS-ADD-03975/21">Newton's Laboratory Notebook</a>
			</h5>
		</div>
		<a href="/view/MS-ADD-03975/21"><img alt="MS-ADD-03975"
			src="/images/collectionsView/featured-newton.jpg" /> </a>
	</div>
	<div class="featuredItem">
		<!--  PR-INC-00000-A-00007-00002-00888 -->
		<div class="featuredItem-text">
			<h5>
				<a href="/view/PR-INC-00000-A-00007-00002-00888/87">Nuremberg
					Chronicle </a>
			</h5>
		</div>
		<a href="/view/PR-INC-00000-A-00007-00002-00888/87"><img
			alt="PR-INC-00000-A-00007-00002-00888"
			src="/images/collectionsView/featured-nuremberg.jpg" /> </a>

	</div>

</div>
</section>

<jsp:include page="footer/footer.jsp" />



