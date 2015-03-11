<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.List,java.util.ArrayList,java.util.Iterator,ulcambridge.foundations.viewer.ItemFactory"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");

	ItemFactory factory = (ItemFactory) request
			.getAttribute("itemFactory");

	Iterator<String> ids = collection.getItemIds().iterator();
	List<Item> items = new ArrayList<Item>();

	while (ids.hasNext()) {
		String id = ids.next();
		Item item = factory.getItemFromId(id);
		items.add(item);
	}
%>
<jsp:include page="header/header-full.jsp">
	<jsp:param name="title" value="<%=collection.getTitle()%>" />
</jsp:include>
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Browse our collections" />
</jsp:include>


<div class="clear"></div>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">

		<div class="campl-column12  campl-main-content" id="content">
			<div class="campl-content-container">

				<jsp:include page="<%=collection.getSummary()%>" />

			</div>
			<div class="campl-content-container campl-column12">

				<ol id="collections_carousel">
					<%
						Iterator<Item> itemIterator = items.iterator();
						int itemNum = 0;

						while (itemIterator.hasNext()) {
							Item item = itemIterator.next();
							itemNum++;

							String imageDimensions = "";
							if (item.getThumbnailOrientation().equals("portrait")) {
								imageDimensions += " style='height:100%' ";
							} else if (item.getThumbnailOrientation().equals("landscape")) {
								imageDimensions += " style='width:100%' ";
							}

							out.print("<li class='campl-column5'><div class='collections_carousel_item'><div class='virtual_collections_carousel_image_box campl-column6'>"
									+ "<div class='collections_carousel_image' id='collections_carousel_item"
									+ itemNum
									+ "'><a href='/view/"
									+ item.getId()
									+ "/1'><img src='"
									+ item.getThumbnailURL()
									+ "' "
									+ "alt='"
									+ item.getId()
									+ "' "
									+ imageDimensions
									+ "></a></div></div> \n ");
							out.print("<div class='collections_carousel_text campl-column6'><h5>"
									+ item.getTitle() + " (" + item.getShelfLocator()
									+ ")</h5> " + item.getAbstractShort()
									+ " ... <a href='/view/" + item.getId()
									+ "/1'>more</a> "
									+ "</div><div class='clear'></div></div></li>\n\n");
						}
					%>
				</ol>

			</div>

			<div class="campl-column12 campl-content-container">
				<jsp:include page="<%=collection.getSponsors()%>" />
			</div>
		</div>
	</div>
</div>

<jsp:include page="header/footer-full.jsp" />



