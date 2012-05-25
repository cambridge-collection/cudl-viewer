<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.Iterator"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>
<jsp:include page="header/nav-browse-submenu.jsp" />

<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>
<script type="text/javascript">

var viewPage = function(pageNum) {
	 if (window.history.replaceState) {
		 window.history.replaceState(pageNum, "Cambridge Digital Library",
				 "#"+pageNum);
	 } else if (window.location){
		 window.location.hash = pageNum;
	 }
	 return false;
};

function pageinit() {
 
  var pageLimit = 8;
  var numResults = <%=collection.getItems().size()%>;
	  
  var Paging = $(".pagination").paging(
	numResults,
	{

		format : "[< (q-) ncnnnn (-p) >]",
		perpage : pageLimit,
		lapping : 0,
		page : 1,
		onSelect : function(page) {

	        $.ajax({
                "url": '<%=collection.getURL()%>/itemJSON?start=' + this.slice[0] + '&end=' + this.slice[1],
                "success": function(data) {
                       // Spinner.stop();
                      
                      // hide content
                      //cont.slice(prev[0], prev[1]).css('display','none');

                      // content replace					                   
			          //    var data = this.slice;
				      var container = document.getElementById("collections_carousel");
				      
				      // Remove all children
				      container.innerHTML = '';

				      // add in the results
				      for (var i=0; i<data.length; i++) {
				    	  var item = data[i];
				    	  var imageDimensions = "";
						  if (item.thumbnailOrientation=="portrait") {
							imageDimensions = " style='height:100%' ";
						  } else if (item.thumbnailOrientation=="landscape") {
							imageDimensions = " style='width:100%' ";
						  }
							
				    	  var itemDiv = document.createElement('div');
				    	  itemDiv.setAttribute("class", "collections_carousel_item");
				    	  itemDiv.innerHTML= "<div class='collections_carousel_image_box'>"+
				        "<div class='collections_carousel_image'>"+
				        "<a href='/view/" +item.id+ "'><img src='" +item.thumbnailURL+ "' alt='" +item.id+ "' "+
				        imageDimensions+ " > </a></div></div> "+
				        "<div class='collections_carousel_text'><h5>" +item.title+ " (" +item.shelfLocator+ ")</h5> "+item.abstractShort+
				        " </div><div class='clear'></div>";
	           	        container.appendChild(itemDiv);
			 
				      
				      }
				      
				      // show items									      
						//cont.slice(data[0], data[1]).fadeIn("fast");
						//prev = data;					                        
                }
            });
		

			return false; 
		},

		onFormat : function(type) {

			switch (type) {

			case 'block':

				if (!this.active)
					return '<span class="disabled">'
							+ this.value + '</span>';
				else if (this.value != this.page)
					return '<em><a href="" onclick="viewPage('+ this.value + '); return false;">'
							+ this.value + '</a></em>';
				return '<span class="current">'
						+ this.value + '</span>';
						
			case 'right':
			case 'left':

				if (!this.active) {
					return '';
				}
				return '<a href="" onclick="viewPage('+ this.value + '); return false;">' + this.value + '</a>';
				
			case 'next':

				if (this.active)
					return '<a href="" onclick="viewPage('+ this.value + '); return false;" class="next">Next ></a>';
				return '<span class="disabled">Next ></span>';

			case 'prev':

				if (this.active)
					return '<a href="" onclick="viewPage('+ this.value + '); return false;" class="prev">< Prev</a>';
				return '<span class="disabled">< Prev</span>';

			case 'first':

				if (this.active)
					return '<a href="" onclick="viewPage('+ this.value + '); return false;" class="first">|<</a>';
				return '<span class="disabled">|<</span>';

			case 'last':

				if (this.active)
					return '<a href="" onclick="viewPage('+ this.value + '); return false;" class="last">>|</a>';
				return '<span class="disabled">>|</span>';

			case "leap":

				if (this.active)
					return "...";
				return "";

			case 'fill':

				if (this.active)
					return "...";
				return "";
			}
		}
	});

    // Handle updating the Page selected from the hash part of the URL
	$(window).hashchange(function() {

		if (window.location.hash)
			Paging.setPage(window.location.hash.substr(1));
		else
			Paging.setPage(1); // we dropped the initial page selection and need to run it manually
	});

	$(window).hashchange();

	// Show the pagination toolbars if enough elements are present
	if ((numResults/pageLimit)>1) {
		$(".toppagination")[0].style.display="block";
		$(".toppagination")[1].style.display="block";
	} else {
		$(".toppagination")[0].style.display="none";
		$(".toppagination")[1].style.display="none";		
	}
}
</script>

<div class="clear"></div>

<section id="content" class="grid_20 content">

	<jsp:include page="<%=collection.getSummary()%>" />

	<div class="grid_9 container" id="pagination_container">

		<div class="pagination toppagination"></div>
		<!-- start of list -->
		<div id="collections_carousel" class="collections_carousel">
		</div>
		<!-- end of list -->
		<div class="pagination toppagination"></div>

	</div>

	<jsp:include page="<%=collection.getSponsors()%>" />
</section>

<jsp:include page="footer/footer.jsp" />



