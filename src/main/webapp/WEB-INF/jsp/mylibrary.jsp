<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.*,java.util.Iterator,java.util.*"%>
<jsp:include page="header/header-full.jsp" >
	<jsp:param name="loggedin" value="true" />
</jsp:include>
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<%
	List<Item> itemList = (List<Item>) request.getAttribute("items");
	List<Bookmark> bookmarkList = (List<Bookmark>) request
			.getAttribute("bookmarks");
	
	// Username ONLY used for embedding in html for debugging purposes.
	// (so we can tell by inspecting the HTML who the user is)
	String username = (String) request.getAttribute("username");
%>


<form>
<input type="hidden" name="username" value="<%=username %>"/>
</form>

<script>
function checkRemove() {
	var result = confirm("Are you sure you want to remove this item from your bookmarks?");
	if (result==true) {
		return true;
	} else {
	    return false;
	}
}
</script>

<div class="clear"></div>

<section id="content" class="grid_20 content ">

	<div class="grid_20">

		<div class="grid_9">
			<h3>My Library</h3>
			Create your own collection by bookmarking any page within Cambridge
			Digital Library, and it will appear here. <br /> <br />
			<jsp:include page="mylibrary-help.jsp" />
		</div>
		<div class="grid_10">
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

						out.print("<li><div class='collections_carousel_item'><div class='collections_carousel_image_box'>"
								+ "<div class='collections_carousel_image' id='collections_carousel_item"
								+ itemNum
								+ "'><a href='/view/"
								+ item.getId()
								+ "/"
								+ pageNum
								+ "'><img src='"
								+ thumbnailURL
								+ "' "
								+ "alt='"
								+ item.getId()
								+ "' "
								+ imageDimensions
								+ "></a></div></div> \n ");
						out.print("<div class='collections_carousel_text'><h5>"
								+ item.getTitle()
								+ " ("
								+ item.getShelfLocator()
								+ ")</h5> "
								+ "Image: "
								+ pageNum
								+ "<br/>"
								+ item.getAbstractShort()
								+ " ... <a href='/view/"
								+ item.getId()
								+ "/"
								+ pageNum
								+ "'>more</a> "
								+ "</div>"
								+ "<a onclick='return checkRemove()' style='float:right' href='/mylibrary/deletebookmark/?itemId="
								+ item.getId() + "&page=" + pageNum + "&redirect=true'>remove</a>"
								+ "<div class='clear'></div></div></li>\n\n");
					}

					// If the person has no bookmarks
					if (itemNum == 0) {
						out.println("<div>You don't have any bookmarks. Try browsing <a href='/collections/'>our collections</a>.</div>");
					}
				%>


			</ol>
		</div>
	</div>
	<div class="grid_20">&nbsp;</div>

</section>

<jsp:include page="footer/footer.jsp" />



