<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="ulcambridge.foundations.viewer.model.*, java.util.Iterator"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-browse.jsp" />
<jsp:include page="header/nav-browse-collections.jsp" />

<% Collection collection = (Collection) request.getAttribute( "collection" ); %>
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


<jsp:include page="<%=collection.getSummary() %>" />

<div class="grid_9">

	<ol id="collections_carousel">
	<% 
	   Iterator<Item> items = collection.getItems().iterator();
	   
	   while(items.hasNext()) {
		   Item item = items.next();
		   out.print("<li><a href='/view/"+item.getId()+"/'><img class='bookSelectImage' "+
				   "src='"+item.getThumbnailURL()+"' "+
				   "alt='"+item.getId()+"' width='"+item.getThumbnailWidth()+"' height='"+item.getThumbnailHeight()+"'> </a>\n ");
		   out.print("<h5>"+item.getTitle()+" ("+item.getShelfLocator()+")</h5> " +
				   item.getAbstractShort()+" ... <a href='/view/"+item.getId()+"/'>more</a> "+
				    "</li>\n\n");
	   }
	
	%>
	</ol>


</div>

<jsp:include page="<%=collection.getSponsors() %>" />

</section>

<jsp:include page="footer/footer.jsp" />



