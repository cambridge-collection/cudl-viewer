<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory,java.util.List"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Browse our collections" />
</jsp:include>

<%
	List<Item> featuredItems = (List<Item>) request
			.getAttribute("featuredItems");
%>


<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">
			<div class="campl-content-container">

				<!-- Treasures of the library -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/treasures" class="campl-teaser-img-link"><img
									alt="Treasures of the Library" title="Treasures of the Library"
									src="/images/collectionsView/collection-treasures.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/treasures">Treasures of the Library</a>
								</h3>
								<p>In this special collection we draw together books,
									manuscripts and other items of particular significance. Many of
									them have been displayed in Library exhibitions in the past –
									now they can be accessed at any time, from anywhere in the
									world, and browsed cover to cover.</p>
								<a href="/collections/treasures" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>

				<hr class="campl-teaser-divider">

				<!-- Board of Longitude -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/longitude" class="campl-teaser-img-link"><img
									alt="Board of Longitude" title="Board of Longitude"
									src="/images/collectionsView/collection-longitude.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/longitude">Board of Longitude</a>
								</h3>
								<p>Cambridge University Library holds the entire archive of
									the Board of Longitude, the state organization established in
									the eighteenth century to evaluate methods for finding ships’
									position and to encourage a host of projects in instrument
									design, clock-making, mapping and voyaging.</p>
								<a href="/collections/longitude" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>

				<hr class="campl-teaser-divider">

				<!-- Darwin Manuscripts -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/darwin_mss" class="campl-teaser-img-link"><img
									alt="Darwin Manuscripts" title="Darwin Manuscripts"
									src="/images/collectionsView/collection-darwin_mss.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/darwin_mss">Darwin Manuscripts</a>
								</h3>
								<p>
									The <i>Charles Darwin Papers</i> in the Manuscripts Department
									of Cambridge University Library hold nearly the entire extant
									collection of Darwin’s working scientific papers. Paramount
									among these documents are <i>Charles Darwin’s Evolution
										Manuscripts</i>, which are being published online at the Cambridge
									Digital Library and simultaneously at the <a
										href="http://www.amnh.org/our-research/darwin-manuscripts-project">Darwin
										Manuscripts Project</a> in collaboration with the <a
										href="http://www.darwinproject.ac.uk/">Darwin
										Correspondence Project</a>.
								</p>
								<a href="/collections/darwin_mss" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>

				<hr class="campl-teaser-divider">

				<!-- Newton Papers -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/newton" class="campl-teaser-img-link"><img
									alt="Newton Papers" title="Newton Papers"
									src="/images/collectionsView/collection-newton.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/newton">Newton Papers</a>
								</h3>
								<p>Cambridge University Library holds the largest and most
									important collection of the scientific works of Isaac Newton
									(1642-1727). We present here an initial selection of Newton's
									manuscripts, concentrating on his mathematical work in the
									1660s.</p>
								<a href="/collections/newton" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>

				<hr class="campl-teaser-divider">

				<!-- Darwin-Hooker Letters -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/darwinhooker"
									class="campl-teaser-img-link"><img
									alt="Darwin-Hooker Letters" title="Darwin-Hooker Letters"
									src="/images/collectionsView/collection-darwinhooker.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/darwinhooker">Darwin-Hooker Letters</a>
								</h3>
								<p>These letters are a connecting thread that spans forty
									years of Darwin's mature working life from 1843 until his death
									in 1882. They bring into sharp focus every aspect of Darwin's
									scientific work throughout that period, and illuminate the
									mutual friendships he and Hooker shared with other scientists,
									but they also provide a window of unparalleled intimacy into
									the personal lives of the two men.</p>
								<a href="/collections/darwinhooker" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>

				<hr class="campl-teaser-divider">

				<!-- Sassoon Journals -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/sassoon" class="campl-teaser-img-link"><img
									alt="Sassoon Journals" title="Sassoon Journals"
									src="/images/collectionsView/collection-sassoon.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/sassoon">Sassoon Journals</a>
								</h3>
								<p>The notebooks kept by the soldier-poet Siegfried Sassoon
									(1886–1967) during his service in the British Army in the First
									World War are among the most remarkable documents of their
									kind, and provide an extraordinary insight into his
									participation in one of the defining conflicts of European
									history.</p>
								<a href="/collections/sassoon" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>


				<hr class="campl-teaser-divider">

				<!-- Music -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/music" class="campl-teaser-img-link"><img
									alt="Music" title="Music"
									src="/images/collectionsView/collection-music.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/music">Music</a>
								</h3>
								<p>
									The music collections at Cambridge University Library play an
									important role in the preservation of musical heritage, both on
									a national and international level. From printed and manuscript
									music scores and texts on music to ephemera and concert
									programmes, archival materials documenting the life and work of
									composers, music scholars and performers, a huge variety and
									breadth of material has been collected over several centuries.
									We launch with a selection of important early lute music,
									digitised and described in collaboration with the
									<href="http://www.lutesociety.org/">Lute Society</a>. 
								</p>
								<a href="/collections/music" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>



				<hr class="campl-teaser-divider">

				<!-- The Cairo Genizah Collection -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/genizah" class="campl-teaser-img-link"><img
									alt="The Cairo Genizah Collection"
									title="The Cairo Genizah Collection"
									src="/images/collectionsView/collection-genizah.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/genizah">The Cairo Genizah Collection</a>
								</h3>
								<p>The Taylor-Schechter Cairo Genizah Collection at
									Cambridge University Library is the world's largest and most
									important single collection of medieval Jewish manuscripts.</p>
								<a href="/collections/genizah" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>


				<hr class="campl-teaser-divider">

				<!-- Hebrew Manuscripts -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/hebrew" class="campl-teaser-img-link"><img
									alt="Hebrew Manuscripts" title="Hebrew Manuscripts"
									src="/images/collectionsView/collection-hebrew.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/hebrew">Hebrew Manuscripts</a>
								</h3>
								<p>For over five hundred years Cambridge University has been
									building up one of the world’s most important collections of
									Hebrew manuscripts.</p>
								<a href="/collections/hebrew" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>


				<hr class="campl-teaser-divider">

				<!-- Islamic Manuscripts -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/islamic" class="campl-teaser-img-link"><img
									alt="Islamic Manuscripts" title="Islamic Manuscripts"
									src="/images/collectionsView/collection-islamic.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/islamic">Islamic Manuscripts</a>
								</h3>
								<p>The Library's Islamic Manuscripts collection began in the
									1630s and now numbers over 5,000 works, which shed light on
									many aspects of the Islamic world, its beliefs and learning.
									Our initial selection includes several early Qur'anic fragments
									on parchment.</p>
								<a href="/collections/islamic" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>




				<hr class="campl-teaser-divider">

				<!-- Christian Works -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/christian" class="campl-teaser-img-link"><img
									alt="Christian Works" title="Christian Works"
									src="/images/collectionsView/collection-christian.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/christian">Christian Works</a>
								</h3>
								<p>A selection of some of our finest Christian manuscripts
									and early printed books, from the Bible to the liturgy,
									spanning over 1000 years of worship and debate.</p>
								<a href="/collections/christian" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>




				<hr class="campl-teaser-divider">

				<!-- Sanskrit Manuscripts -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/sanskrit" class="campl-teaser-img-link"><img
									alt="Sanskrit Manuscripts" title="Sanskrit Manuscripts"
									src="/images/collectionsView/collection-sanskrit.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/sanskrit">Sanskrit Manuscripts</a>
								</h3>
								<p>A full catalogue of our Sanskrit manuscript collection,
									comprising more than 1,600 works in Sanskrit, Prakrit, Pali,
									Tamil and other ancient and medieval South Asian languages,
									produced over a time-span of more than 1,000 years.</p>
								<a href="/collections/sanskrit" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>



				<hr class="campl-teaser-divider">

				<!-- Japanese Works -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/japanese" class="campl-teaser-img-link"><img
									alt="Japanese Works" title="Japanese Works"
									src="/images/collectionsView/collection-japanese.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/japanese">Japanese Works</a>
								</h3>
								<p>Items from Cambridge University Library's important
									collection of early Japanese books and manuscripts,
									representing a wide range of the various aspects of early
									Japanese learning and excellent samples of Japan’s early
									printing culture.</p>
								<a href="/collections/japanese" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>




				<hr class="campl-teaser-divider">

				<!-- Peterhouse Manuscripts -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/peterhouse" class="campl-teaser-img-link"><img
									alt="Peterhouse Manuscripts" title="Peterhouse Manuscripts"
									src="/images/collectionsView/collection-peterhouse.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/peterhouse">Peterhouse Manuscripts</a>
								</h3>
								<p>Peterhouse (founded 1284) is the oldest of the Cambridge
									Colleges, and holds a significant collection of medieval
									manuscripts. As part of the process of recataloguing and
									preserving the College's collections, it has been decided to
									present highlights from the Peterhouse manuscripts in digital
									form.</p>
								<a href="/collections/peterhouse" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>




				<hr class="campl-teaser-divider">

				<!-- Spanish Chapbooks -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/spanishchapbooks"
									class="campl-teaser-img-link"><img alt="Spanish Chapbooks"
									title="Spanish Chapbooks"
									src="/images/collectionsView/collection-spanishchapbooks.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/spanishchapbooks">Spanish Chapbooks</a>
								</h3>
								<p>These predecessors of the yellow press provide a
									fascinating bird’s eye view of Spanish popular culture from the
									eighteenth century onwards.</p>
								<a href="/collections/spanishchapbooks"
									class="campl-primary-cta">Read more</a>
							</div>
						</div>
					</div>
				</div>




				<hr class="campl-teaser-divider">

				<!-- Exhibition Items -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/exhibitions" class="campl-teaser-img-link"><img
									alt="Exhibition Items" title="Exhibition Items"
									src="/images/collectionsView/collection-exhibitions.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/exhibitions">Exhibition Items</a>
								</h3>
								<p>The Library holds regular exhibitions, usually organised
									around a particular theme or collection. Depending on the
									chosen theme, the exhibition cases might contain anything from
									irreplaceable works of world importance to newspapers or
									printed ephemera. They demonstrate the breadth of the Library's
									holdings and often yield surprises.</p>
								<a href="/collections/exhibitions" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>



				<hr class="campl-teaser-divider">

				<!-- Waterloo  -->
				<div class="campl-content-container ">
					<div class="campl-horizontal-teaser campl-teaser clearfix">
						<div class="campl-column3">
							<div class="campl-content-container campl-horizontal-teaser-img">
								<a href="/collections/waterloo" class="campl-teaser-img-link"><img
									alt="Waterloo" title="Waterloo"
									src="/images/collectionsView/collection-waterloo.jpg"
									class="campl-scale-with-grid"></a>
							</div>
						</div>
						<div class="campl-column6">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<h3 class="campl-teaser-title">
									<a href="/collections/waterloo">Waterloo</a>
								</h3>
								<p>The Battle of Waterloo was the climactic engagement of a
									campaign that pitted an invading French army under Napoleon
									Bonaparte against a combined force of Allied troops. The French
									were routed, and the warfare that had plagued Europe for more
									than two decades was definitively ended: there were to be no
									hostilities on such a scale on the continent until the outbreak
									of the First World War in 1914. Through its impact on the
									politics and power-relationships of a Europe approaching the
									height of its worldwide influence, the outcome of Waterloo
									remains significant to this day.</p>
								<a href="/collections/waterloo" class="campl-primary-cta">Read
									more</a>
							</div>
						</div>
					</div>
				</div>


			</div>
		</div>


		<div class="campl-column3 campl-secondary-content">
			<div class="campl-content-container ">
				<div class="campl-heading-container ">
					<h2>Featured Items</h2>
				</div>
				<ul
					class='campl-unstyled-list campl-related-links campl-horizontal-teaser-img '>
					<%
						for (int i = 0; i < featuredItems.size(); i++) {
							Item item = featuredItems.get(i);

							String imageDimensions = "";
							if (item.getThumbnailOrientation().trim().equals("portrait")) {
								imageDimensions = " style='height:100%' width='120px' ";
							} else if (item.getThumbnailOrientation().trim()
									.equals("landscape")) {
								imageDimensions = " style='width:100%' width='120px' ";
							}
					%>
					<li><a href="/view/<%=item.getId()%>"><img
							alt="<%=item.getId()%>" src="<%=item.getThumbnailURL()%>"
							<%=imageDimensions%> /></a>
						<h5>
							<a href="/view/<%=item.getId()%>"><%=item.getTitle()%></a>
						</h5></li>

					<%
						}
					%>
				</ul>
			</div>
			<!-- 
			<div class="campl-content-container">
				<div class="campl-vertical-teaser campl-teaser campl-promo-teaser">
					<div class="campl-content-container campl-vertical-teaser-txt">
						<p class='campl-teaser-title'>
							<a href="#">Promo teaser with title above</a>
						</p>
					</div>
					<div class="campl-content-container campl-vertical-teaser-img">
						<a href="#" class="campl-teaser-img-link"><img alt=""
							src="../images/content/homepage-promo-placeholder.png"
							class="campl-scale-with-grid" /></a>
					</div>
					<div
						class="campl-content-container campl-vertical-teaser-txt clearfix">
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit,
							sed do eiusmod tempor incididunt ut labore et dolore magna
							aliqua.</p>
						<a href="#4" class="campl-secondary-cta campl-float-right">Read
							more</a>
					</div>
				</div>
			</div> -->
		</div>
	</div>
</div>


<jsp:include page="header/footer-full.jsp" />



