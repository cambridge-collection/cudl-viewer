<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>

<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>
<%
	Collection organisationalCollection = (Collection) request
			.getAttribute("organisationalCollection");
	String itemTitle = (String) request.getAttribute("itemTitle");
	String content = (String) request.getAttribute("content");
	List<String> relatedItems = (List<String>) request
			.getAttribute("relatedItems");
	ItemFactory itemFactory = (ItemFactory) request
			.getAttribute("itemFactory");
%>
<nav id="navSecondary" class="grid_20">
	<ul>
		<li><a class="active"
			title="<%=organisationalCollection.getTitle()%>"
			href="<%=organisationalCollection.getURL()%>"> <%=organisationalCollection.getTitle()%></a></li>
	</ul>
</nav>


<div class="clear"></div>

<section id="content" class="grid_20 content">
	<div class="grid_20">

		<div class="panel light grid_13">

			<h3 style="margin-left: 8px"><%=itemTitle%></h3>
			<div class="grid_12 essaytext">
				<%=content%>
			</div>
		</div>

		<div class="featuredItem grid_6">
		    <h4>Related Items</h4>
			<%
				for (int i = 0; i < relatedItems.size(); i++) {
					Item item = itemFactory.getItemFromId(relatedItems.get(i));
			%>
			<div class="featuredItem-text">
				<h5>
					<a href="/view/<%=item.getId()%>"><%=item.getTitle()%></a>
				</h5>
			</div>

			<%
				String imageDimensions = "";
				if (item.getThumbnailOrientation().trim().equals("portrait")) {
					imageDimensions = " style='height:100%' ";
				} else if (item.getThumbnailOrientation().trim()
						.equals("landscape")) {
					imageDimensions = " style='width:100%' ";
				}
			%>
			<div style="float: left;">
				<div class="collections_carousel_image_box">
					<div class="collections_carousel_image">
						<a href="/view/<%=item.getId()%>/"><img
							alt="<%=item.getId()%>" src="<%=item.getThumbnailURL()%>"
							<%=imageDimensions%> /> </a>
					</div>
				</div>
			</div>

			<%
				}
			%>
		</div>

	</div>

</section>

<jsp:include page="footer/footer.jsp" />



