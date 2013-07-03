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
	Collection parentCollection = (Collection) request
			.getAttribute("parentCollection");
	String itemTitle = (String) request.getAttribute("itemTitle");
	String content = (String) request.getAttribute("content");
	String itemThumbnailURL = (String) request.getAttribute("itemThumbnailURL");
	String itemThumbnailOrientation = (String) request.getAttribute("itemThumbnailOrientation");
	List<String> relatedItems = (List<String>) request
			.getAttribute("relatedItems");
	ItemFactory itemFactory = (ItemFactory) request
			.getAttribute("itemFactory");
	
	EssayItem essayItem = (EssayItem) request
			.getAttribute("essayItem");	
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

			<h3 style="margin-left: 8px"><%=itemTitle%></h3>
			<div class="grid_12 essaytext">
				<img class="left" src="<%=itemThumbnailURL%>"/><%=content%>
			</div>
			<div class="grid_12 longitudeessaymetadata">
			    
			    <%  if (essayItem.getAssociatedPeople()!=null 
			           && essayItem.getAssociatedPeople().size()>0) { %>			    				    
			      <strong>Associated People</strong>: <% 
			      for (String person: essayItem.getAssociatedPeople()) { %> 
			          <a href="/search?keyword=<%=person %>"><%=person %></a>;&nbsp;			          
			    <% 
			        
			      }
			    }
			    
			    if (essayItem.getAssociatedOrganisations()!=null 
			           && essayItem.getAssociatedOrganisations().size()>0) { %>		
			      <br/><br/>	    
			      <strong>Organisations</strong>: <% 
			      for (String organisation: essayItem.getAssociatedOrganisations()) { %> 
			          <a href="/search?keyword=<%=organisation %>"><%=organisation %></a>;&nbsp;			          
			      <%
			      }
			    }
			    
			    if (essayItem.getAssociatedSubjects()!=null 
			           && essayItem.getAssociatedSubjects().size()>0) { %>	
			    <br/><br/>		   
			    <strong>Subjects</strong>:  <% 
			    for (String subject: essayItem.getAssociatedSubjects()) { %> 
			          <a href="/search/advanced/results?subject=<%=subject %>"><%=subject %></a>;&nbsp;		          
			    <% 
			      } 
			    }
			    
			    if (essayItem.getAssociatedPlaces()!=null 
			           && essayItem.getAssociatedPlaces().size()>0) { %>	
                <br/><br/>			           			    
			    <strong>Places</strong>:  <% 
			    for (String place: essayItem.getAssociatedPlaces()) { %> 
			          <a href="/search/advanced/results?location=<%=place %>"><%=place %></a>;&nbsp;		          
			    <% 
			      }
			    }
			    %>  
			</div>
		</div>

		<div class="grid_6">
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
				<div class="left" >
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

</section>

<jsp:include page="footer/footer.jsp" />



