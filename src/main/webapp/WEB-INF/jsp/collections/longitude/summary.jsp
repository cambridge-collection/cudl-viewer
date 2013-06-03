<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List"%>
<%
	List<Collection> subCollections = (List<Collection>) request.getAttribute("subCollections");
%>
<div class="grid_20">
	<h3 style="margin-left: 8px">Board of Longitude</h3>

	<div class="grid_8">
		<br />
		<p>The archives of the Royal Greenwich Observatory, held in
			Cambridge University Library, include the complete run of the papers
			of the Board of Longitude through the eighteenth century until its
			abolition in 1828. These papers throw a vivid light on the role of
			the British state in encouraging invention and discovery, on the
			energetic culture of technical ingenuity in the long eighteenth
			century, and on many aspects of exploration and maritime travel in
			the Pacific Ocean and the Arctic.</p>

		<p>In July 1714, an act of parliament established a large prize
			for discovery of longitude, the determination of position at sea east
			or west from a fixed meridian line. The prize varied according to the
			accuracy of the method proposed: as much as £20,000 would be awarded
			for a method accurate to within 30 nautical miles, provided a ship
			could show the method worked to this accuracy. Further sums could be
			allotted for related inventions and experiments.</p>
			
	</div>
	<div class="grid_11 right">
         <iframe width="510" height="315" src="http://www.youtube.com/embed/videoseries?list=PL49D9939209E5BAEC&showinfo=1&modestbranding=1" frameborder="0" allowfullscreen></iframe>
    </div>  
    <div class="grid_19"> 
    <h4>Board of Longitude Collections</h4>                    

		<%
			for (int i=0; i<subCollections.size(); i++) { 
				       Collection c = subCollections.get(i);
		%>

		<div class="featuredcollection grid_3">
			<a href="<%=c.getURL()%>"><img
				src="/images/collectionsView/collection-<%=c.getId()%>.jpg"
				height="128px" width="128px" /> <span
				class="featuredcollectionlabellong"><%=c.getTitle()%></span></a>
		</div>
		<%
			}
		%>         

</div>