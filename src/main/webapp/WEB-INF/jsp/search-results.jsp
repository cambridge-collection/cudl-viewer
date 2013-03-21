<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,ulcambridge.foundations.viewer.search.*,ulcambridge.foundations.viewer.model.Item,ulcambridge.foundations.viewer.ItemFactory,ulcambridge.foundations.viewer.forms.SearchForm"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<%
	SearchResultSet resultSet = ((SearchResultSet) request
			.getAttribute("results"));
	SearchForm form = ((SearchForm) request.getAttribute("form"));	
%>

<!--  script for ajax pagination -->
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
	
  var pageLimit = 20;
  var numResults = <%=resultSet.getNumberOfResults()%>;
  
  // Setup spinner. 
  var opts = {
		  lines: 13, // The number of lines to draw
		  length: 7, // The length of each line
		  width: 4, // The line thickness
		  radius: 10, // The radius of the inner circle
		  rotate: 0, // The rotation offset
		  color: '#000', // #rgb or #rrggbb
		  speed: 1, // Rounds per second
		  trail: 60, // Afterglow percentage
		  shadow: false, // Whether to render a shadow
		  hwaccel: false, // Whether to use hardware acceleration
		  className: 'spinner', // The CSS class to assign to the spinner
		  zIndex: 2e9, // The z-index (defaults to 2000000000)
		  top: 'auto', // Top position relative to parent in px
		  left: 'auto' // Left position relative to parent in px
		};
		var target = document.getElementById('content');
		var spinner = new Spinner(opts);
		
  // Setup pagination
  var Paging = $(".pagination").paging(
	numResults,
	{

		format : "[< (q-) ncnnnnnn (-p) >]",
		perpage : pageLimit,
		lapping : 0,
		page : 1,
		onSelect : function(page) {

			spinner.spin(target);				   	
			
	        $.ajax({
                "url": '/search/JSON?start=' + this.slice[0] + '&end=' + this.slice[1] +'&<%=request.getQueryString()%>',
                "success": function(data) {
                	
                	  spinner.stop();

                      // content replace					                   
				      var container = document.getElementById("collections_carousel");
				      
				      // Remove all children
				      container.innerHTML = '';

				      // add in the results
				      for (var i=0; i<data.length; i++) {
				    	  var result = data[i];
				    	  var item = result.item;
				    	  var imageDimensions = "";
						  if (item.thumbnailOrientation=="portrait") {
							imageDimensions = " style='height:100%' ";
						  } else if (item.thumbnailOrientation=="landscape") {
							imageDimensions = " style='width:100%' ";
						  }
							
				    	  var itemDiv = document.createElement('div');
				    	  itemDiv.setAttribute("class", "collections_carousel_item");
				    	  var itemText = "<div class='collections_carousel_image_box'>"+
				        "<div class='collections_carousel_image'>"+
				        "<a href='/view/" +item.id+ "'><img src='" +item.thumbnailURL+ "' alt='" +item.id+ "' "+
				        imageDimensions+ " > </a></div></div> "+
				        "<div class='collections_carousel_text grid_8'><h5>" +item.title+ " (" +item.shelfLocator+ ")</h5> "+item.abstractShort+
				        " ... <br/><br/><ul>";
				        
					    for (var j=0; j<result.snippets.length; j++) {		
					    	  
					    	var snippet = result.snippets[j];
					    	
					    	// Strip tags. 
					    	var snippetValue = snippet.snippetStrings[0];
					    						    	
					    	if (snippetValue!="" && snippetValue!="undefined") {
					          var snippetLabel = "";
					          // FIXME Temporary hack to hide page label if the match 
					          // is on the first page as this will be a match for the whole item. 
					          if (snippet.startPage!=1) { snippetLabel = snippet.startPageLabel; }
					    	  itemText += "<li><a href='/view/" +item.id+ "/"+snippet.startPage+"'>"+snippetValue
					    	  +"</a> <font style='color:#999999'>"+snippetLabel+"</font></li>"; 
					    	}
					    	
					    }
					      
					    itemText+="</ul></div><div class='clear'></div>";
					    itemDiv.innerHTML = itemText;
	           	        container.appendChild(itemDiv);
			 
				      }	     
				      
	           	       $('.collections_carousel_text').truncate({  
	           	    	    max_length: 260,  
		           	        more: "view more",  
		           	        less: "hide"
		           	    }); 
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

<section id="content" class="grid_20 content"> <!-- <h3 style="margin-left: 8px">Search</h3>  -->

<div class="grid_6 ">
	<div class="searchform box">

		<form:form commandName="searchForm" class="grid_5" action="/search" method="GET">
		
			<form:input path="keyword" class="search" type="text" value="<%=form.getKeyword()%>" name="keyword"
				placeholder="Search" autocomplete="off" /> 
			<input id="submit" type="submit" value="Search" />
			
            <a href="/search/advanced/query" class="altsearchlink">advanced</a>
            <br/><br/>
			<%
				Iterator<String> facetsUsedHidden = form.getFacets().keySet()
						.iterator();
				while (facetsUsedHidden.hasNext()) {
					String facetName = facetsUsedHidden.next();
					String facetValue =form.getFacets().get(facetName);
			%>
			<input path="<%=facetName%>" type="hidden" name="facet-<%=facetName%>"
				value="<%=facetValue%>" />
			<%
				}
			%>
			<form:input path="fileID" type="hidden" name="fileID" value="<%=form.getFileID() %>"/>

		</form:form>
		<br/><br/>

		<%
			Iterator<String> facetsUsed = form.getFacets().keySet().iterator();
			while (facetsUsed.hasNext()) {
				String facetName = facetsUsed.next();
				String facetValue = form.getFacets().get(facetName);
		%>
		<div class="search-facet-selected">
			<a class="search-close"
				href="?<%=SearchUtil.getURLParametersWithoutFacet(form,facetName)%>&amp;"></a>
			<%
				out.print(facetValue);
			%>
		</div>
		<%
			}
			if (form.getFileID()!=null && form.getFileID().trim().length()>0 ) {
			%>
			<div class="search-facet-selected"><a class="search-close"
				href="?<%=SearchUtil.getURLParameters(form).replace("fileID="+form.getFileID(), "fileID=")%>&amp;"></a>
			<%
				out.print("File ID: "+form.getFileID());
			%>	</div>		
           <%
			}
			if (resultSet.getSpellingSuggestedTerm() != null
					&& !resultSet.getSpellingSuggestedTerm().equals("")) {
				out.println("Did you mean <a href=\"/search?keyword="
						+ resultSet.getSpellingSuggestedTerm() + "\">"
						+ resultSet.getSpellingSuggestedTerm() + "</a> ?");
			}
			
			if (form.getKeyword()!=null && !form.getKeyword().equals("")) {
			  out.println("<div class=\"grid_5\"><br /><b>" + resultSet.getNumberOfResults() + "</b>"
					+ " results were returned.<br/><br/></div>");
			}
		%>
		<%
			if (resultSet.getNumberOfResults() > 0) {
		%>
		<h5>Refine by:</h5>

		<ul id="tree" style="margin-left: 0px;">
			<%
				List<FacetGroup> facetGroups = resultSet.getFacets();

					if (facetGroups != null) {

						for (int i = 0; i < facetGroups.size(); i++) {
							FacetGroup facetGroup = (FacetGroup) facetGroups.get(i);
							String fieldLabel = facetGroup.getFieldLabel();
							String field = facetGroup.getField();
							List<Facet> facets = facetGroup.getFacets();

							// Do not print out the facet for a field already faceting on
							if (!form.getFacets().containsKey(field)) {

								out.println("<li><strong>" + fieldLabel + "</strong><ul  style=\"margin-left: 0px;\">");

								for (int j = 0; j < facets.size(); j++) {
									Facet facet = facets.get(j);

									out.print("<li><a href='?"
											+ SearchUtil.getURLParametersWithExtraFacet(
													form, field, facet.getBand()) + "'>");
									out.print(facet.getBand() + "</a> ("
											+ facet.getOccurences() + ")</li>");

								}
								out.println("</ul></li>");
							}
						}
					}
			%>
		</ul>
		<%
			}
		%>
	</div>
</div>

	<div class="grid_13 container" id="pagination_container">

		<div class="pagination toppagination"></div>
		<!-- start of list -->
		<div id="collections_carousel" class="collections_carousel">
		</div>
		<!-- end of list -->
		<div class="pagination toppagination"></div>


		<%
			List<SearchResult> results = resultSet.getResults();

			// No results were returned. So print out some help.
			if (resultSet.getNumberOfResults() == 0) {
				if (form.getKeyword()!=null && !form.getKeyword().equals("")) {
					out.println("<p class=\"box\">We couldn't find any items matching <b>"				
						+ form.getKeyword() + "</b></p>");
				}
				
				out.println("<div class=\"searchexample\">");
				out.println("<h5>Example Searches</h5><br/><p>");
				out.println("Searching for <span class=\"search\">newton</span> Searches the metadata for 'newton'<br/>");
				out.println("Searching for <span class=\"search\">isaac newton</span> Searches for 'isaac' AND 'newton'<br/>");
				out.println("Searching for <span class=\"search\">\"isaac newton\"</span> Searches for the phrase 'isaac newton'<br/>");
				out.println("The characters <b>?</b> and <b>*</b> can be used as wildcards in your search.<br />");
				out.println("Use <b>?</b> to represent one unknown character and <b>*</b> to represent any number of unknown characters.<br/>");
				out.println("</p></div>");
			}
		%>
</div>
 


</section>


<jsp:include page="footer/footer.jsp" />



