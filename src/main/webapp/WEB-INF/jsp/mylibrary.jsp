<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.*,java.util.Iterator,java.util.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<div class="clear"></div>

<script type="text/javascript">
	/*
	function pageinit() {

	 var collections_carousel = new glow.widgets.Carousel("#collections_carousel", {
	 loop : false,
	 size : 2,
	 step : 1,
	 vertical : false,
	 pageNav : false
	 }); 
	}*/
</script>

<div class="clear"></div>

<section id="content" class="grid_20 content matrix_carousel">

	<div class="grid_20">

		<h3 style="margin-left: 8px;">My Library</h3>
		<div class="grid_18 box">
			<div class="grid_18">
				<h4>My Bookmarks</h4>

				<ol id="collections_carousel">
					<%
						Set<Item> s = new TreeSet<Item>();
						s.add(ItemFactory.getItemFromId("MS-ADD-03996"));
						s.add(ItemFactory.getItemFromId("MS-ADD-04000"));
						s.add(ItemFactory.getItemFromId("MS-ADD-00875"));
						s.add(ItemFactory.getItemFromId("MS-MOSSERI-IV-00227"));
						s.add(ItemFactory.getItemFromId("PR-F180-B-00008-00001-00100"));
						s.add(ItemFactory.getItemFromId("PR-MONTAIGNE-00001-00004-00004"));
						s.add(ItemFactory.getItemFromId("PR-INC-00000-A-00007-00002-00888"));
						s.add(ItemFactory.getItemFromId("MS-ADD-03988"));
						s.add(ItemFactory.getItemFromId("MS-ADD-03989"));
						s.add(ItemFactory.getItemFromId("MS-ADD-03987"));
						s.add(ItemFactory.getItemFromId("MS-ADD-03970"));
						s.add(ItemFactory.getItemFromId("MS-ADD-03958"));

						Iterator<Item> items = s.iterator();

						int itemNum = 0;

						while (items.hasNext()) {
							Item item = items.next();
							itemNum++;

							String imageDimensions = "";
							if (item.getThumbnailOrientation().equals("portrait")) {
								imageDimensions += " style='height:100%' ";
							} else if (item.getThumbnailOrientation().equals("landscape")) {
								imageDimensions += " style='width:100%' ";
							}

							out.print("<li><div class='collections_carousel_image_box'>"
									+ "<div class='collections_carousel_image' ><a href='/view/"
									+ item.getId() + "/1'><img src='"
									+ item.getThumbnailURL() + "' " + " title='"
									+ item.getTitle() + " (" + item.getShelfLocator()
									+ ")' " + "alt='" + item.getId() + "' "
									+ imageDimensions + "></a></div></div> \n ");
							out.print("<div class='clear'></div></li>\n\n");
						}
					%>
				</ol>
			</div>
		</div>
	</div>
	<div class="grid_20">&nbsp;</div>

</section>

<jsp:include page="footer/footer.jsp" />



