<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.*,java.util.Iterator,java.util.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<div class="clear"></div>

<%
	List<Item> itemList = (List<Item>) request.getAttribute("items");
    List<Bookmark> bookmarkList = (List<Bookmark>) request.getAttribute("bookmarks");
%>

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

						Iterator<Item> items = itemList.iterator();
					    Iterator<Bookmark> bookmarks = bookmarkList.iterator();					
					
						int itemNum = 0;

						while (items.hasNext() && bookmarks.hasNext()) {
							Item item = items.next();
							Bookmark bookmark = bookmarks.next();
							int pageNum = bookmark.getPage();
							String thumbnailURL = bookmark.getThumbnailURL();
							
							itemNum++;

							String imageDimensions = "";
							if (item.getThumbnailOrientation().equals("portrait")) {
								imageDimensions += " style='height:100%' ";
							} else if (item.getThumbnailOrientation().equals("landscape")) {
								imageDimensions += " style='width:100%' ";
							}

							out.print("<li><div class='collections_carousel_image_box'>"
									+ "<div class='collections_carousel_image' ><a href='/view/"
									+ item.getId() + "/" +pageNum+ "'><img src='"
									+ thumbnailURL + "' " + " title='"
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



