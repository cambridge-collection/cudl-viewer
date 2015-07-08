<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.List,java.util.ArrayList,java.util.Iterator,ulcambridge.foundations.viewer.ItemFactory"%>
<%@taglib prefix="c" 
       uri="http://java.sun.com/jsp/jstl/core" %>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");

	ItemFactory factory = (ItemFactory) request
			.getAttribute("itemFactory");
	
	String contentHTMLURL = (String) request.getAttribute("contentHTMLURL");

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
            <div id="summaryDiv" class="campl-content-container" contenteditable="true">

				<% String summaryURL = contentHTMLURL+"/"+collection.getSummary();  %>
				<c:import charEncoding="UTF-8" url="<%=summaryURL%>" /> 				

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

			<div id="sponsorDiv" class="campl-column12 campl-content-container" contenteditable="true">
				<% String sponsorsURL = contentHTMLURL+"/"+collection.getSummary();  %>
		        <c:import charEncoding="UTF-8" url="<%=sponsorsURL%>" /> 
			</div>
		</div>
	</div>
</div>

<% String filenames = collection.getSummary()+","+collection.getSponsors(); %>
<jsp:include page="collection-editor.jsp" >
  <jsp:param name='dataElements' value='summaryDiv,sponsorDiv'/>
  <jsp:param name='filenames' value='<%=filenames%>'/>
</jsp:include>

<jsp:include page="header/footer-full.jsp" />



