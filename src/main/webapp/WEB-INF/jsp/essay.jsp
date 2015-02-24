<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>
<%
	Collection organisationalCollection = (Collection) request
			.getAttribute("organisationalCollection");
	Collection parentCollection = (Collection) request
			.getAttribute("parentCollection");
	String itemTitle = (String) request.getAttribute("itemTitle");
	String content = (String) request.getAttribute("content");
	String itemThumbnailURL = (String) request
			.getAttribute("itemThumbnailURL");
	String itemThumbnailOrientation = (String) request
			.getAttribute("itemThumbnailOrientation");
	List<String> relatedItems = (List<String>) request
			.getAttribute("relatedItems");
	ItemFactory itemFactory = (ItemFactory) request
			.getAttribute("itemFactory");

	EssayItem essayItem = (EssayItem) request.getAttribute("essayItem");
%>

<jsp:include page="header/header-full.jsp">
	<jsp:param name="title"
		value="<%=organisationalCollection.getTitle()%>" />
</jsp:include>
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title"
		value="<%=organisationalCollection.getTitle()%>" />
</jsp:include>


<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column12  campl-main-content" id="content">

			<div class="campl-column8">
				<div class="campl-content-container">
					<div class="campl-column10">
						<%
							if (parentCollection != null) {
						%>
						<a href="<%=parentCollection.getURL()%>"><%=parentCollection.getTitle()%></a>
						>
						<%
							}
						%>
						<a href="<%=organisationalCollection.getURL()%>"><%=organisationalCollection.getTitle()%></a>
						<br /> <br />

						<h2><%=itemTitle%></h2>
						<div class="grid_12 essaytext">
							<img class="essay_image" src="<%=itemThumbnailURL%>" /><%=content%>
						</div>
						<div class="grid_12 longitudeessaymetadata">

							<%
								if (essayItem.getAssociatedPeople() != null
										&& essayItem.getAssociatedPeople().size() > 0) {
							%>
							<strong>Associated People</strong>:
							<%
								for (String person : essayItem.getAssociatedPeople()) {
							%>
							<a href="/search?keyword=<%=person%>"><%=person%></a>;&nbsp;
							<%
								}
								}

								if (essayItem.getAssociatedOrganisations() != null
										&& essayItem.getAssociatedOrganisations().size() > 0) {
							%>
							<br /> <br /> <strong>Organisations</strong>:
							<%
 	for (String organisation : essayItem
 				.getAssociatedOrganisations()) {
 %>
							<a href="/search?keyword=<%=organisation%>"><%=organisation%></a>;&nbsp;
							<%
								}
								}

								if (essayItem.getAssociatedSubjects() != null
										&& essayItem.getAssociatedSubjects().size() > 0) {
							%>
							<br /> <br /> <strong>Subjects</strong>:
							<%
 	for (String subject : essayItem.getAssociatedSubjects()) {
 %>
							<a href="/search/advanced/results?subject=<%=subject%>"><%=subject%></a>;&nbsp;
							<%
								}
								}

								if (essayItem.getAssociatedPlaces() != null
										&& essayItem.getAssociatedPlaces().size() > 0) {
							%>
							<br /> <br /> <strong>Places</strong>:
							<%
 	for (String place : essayItem.getAssociatedPlaces()) {
 %>
							<a href="/search/advanced/results?location=<%=place%>"><%=place%></a>;&nbsp;
							<%
								}
								}
							%>
						</div>
					</div>
				</div>

			</div>
			<div class="campl-column4 campl-secondary-content">
				<div class="campl-content-container">

					<h4>Related Items</h4>

					<%
						for (int i = 0; i < relatedItems.size(); i++) {
							Item item = itemFactory.getItemFromId(relatedItems.get(i));
					%>
					<div class="relatedItem grid_6">
						<h5>
							<a href="/view/<%=item.getId()%>"><%=item.getTitle()%> (<%=item.getShelfLocator()%>)</a>
						</h5>

						<%
							String imageDimensions = "";
								if (item.getThumbnailOrientation().trim().equals("portrait")) {
									imageDimensions = " style='height:100%' ";
								} else if (item.getThumbnailOrientation().trim()
										.equals("landscape")) {
									imageDimensions = " style='width:100%' ";
								}
						%>
						<div class="left">
							<div class="collections_carousel_image_box">
								<div class="collections_carousel_image">
									<a href="/view/<%=item.getId()%>/"><img
										alt="<%=item.getId()%>" src="<%=item.getThumbnailURL()%>"
										<%=imageDimensions%> /> </a>
								</div>
							</div>
						</div>
					</div>

					<%
						}
					%>
				</div>
			</div>

		</div>

	</div>
</div>



<jsp:include page="header/footer-full.jsp" />



