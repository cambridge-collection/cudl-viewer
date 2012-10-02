<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.CollectionFactory, java.util.Iterator"%>

<nav id="navSecondary" class="grid_20">
<ul>

	<% 
/*	
    Collection selectedCollection = (Collection) request.getAttribute( "collection" );
	
	Iterator<Collection> collectionIterator = CollectionFactory.getCollections().iterator();

	   while(collectionIterator.hasNext()) {
		   Collection collection = collectionIterator.next();
		   
	      
	      if (selectedCollection.equals(collection)) {
	    	  out.print("<li><a href='"+collection.getURL()+"' title='"+collection.getTitle()+"' ");
	    	  out.print(" class='active' ");
	    	  out.print(">"+collection.getTitle()+"</a></li>\n");	      
	      }
	      
	   } */
    %>

</ul>


</nav>
<!-- end #navSecondary -->