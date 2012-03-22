<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.Iterator"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp" >    
   <jsp:param name="activeMenuIndex" value="1" />
   <jsp:param name="displaySearch" value="true" />
</jsp:include>	
<jsp:include page="header/nav-browse-submenu.jsp" />

<% Collection collection = (Collection) request.getAttribute( "collection" ); %>
<script type="text/javascript">
 function pageinit() {
/*
	 var collections_carousel = new glow.widgets.Carousel("#collections_carousel", {
	 loop : false,
	 size : 4,
	 step : 3,
	 vertical : true,
	 pageNav : false
	 }); 
	 */
	 
	 $(document).ready(function(){
			$('#paging_container').pajinate({
				items_per_page : 8,
				num_age_links_to_display:10,
				item_container_id:'.collections_carousel'
			});
		});
	 
//		nav_label_first:'<<',
//		nav_label_prev:'<',
//		nav_label_next:'>',
//		nav_label_last:'>>'			
 }
	
</script>

<div class="clear"></div>

<section id="content" class="grid_20 content">

<jsp:include page="<%=collection.getSummary() %>" />
			
<div class="grid_9 container" id="paging_container">

    <div class="page_navigation"></div>
	<ol id="collections_carousel" class="collections_carousel">
	<% 
	   Iterator<Item> items = collection.getItems().iterator();
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

				out.print("<li><div class='collections_carousel_item'><div class='collections_carousel_image_box'>"
				        + "<div class='collections_carousel_image' id='collections_carousel_item"+itemNum+"'><a href='/view/" + item.getId()
						+ "/'><img src='" + item.getThumbnailURL()+ "' " + "alt='"
						+ item.getId() + "' "+imageDimensions
						+ "></a></div></div> \n ");
				out.print("<div class='collections_carousel_text'><h5>" + item.getTitle() + " ("
						+ item.getShelfLocator() + ")</h5> "
						+ item.getAbstractShort() + " ... <a href='/view/"
						+ item.getId() + "/'>more</a> " + "</div><div class='clear'></div></div></li>\n\n");
			}
		%>
	</ol>
    <div class="page_navigation"></div>

</div>

<jsp:include page="<%=collection.getSponsors() %>" /> </section>

<jsp:include page="footer/footer.jsp" />



