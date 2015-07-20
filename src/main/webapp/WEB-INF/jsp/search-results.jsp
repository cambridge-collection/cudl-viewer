<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page
	import="java.util.*,java.net.URLEncoder,
	ulcambridge.foundations.viewer.search.*,
	ulcambridge.foundations.viewer.model.Item,
	ulcambridge.foundations.viewer.ItemFactory,
	ulcambridge.foundations.viewer.forms.SearchForm,
	org.owasp.encoder.Encode,
	java.text.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Search" />
</jsp:include>

<!-- result css, query duration and sticky menu -->
<script type="text/javascript" src="/scripts/moment.min.js"></script>
<!-- <script type="text/javascript" src="/scripts/jquery.sticky.js"></script> -->
<link rel="stylesheet" href="/styles/advancedsearch.css">

<%
	SearchResultSet resultSet = ((SearchResultSet) request.getAttribute("results"));
	SearchForm form = ((SearchForm) request.getAttribute("form"));
%>

<!--  script for ajax pagination -->
<script type="text/javascript">

	var viewPage = function(pageNum) {
		if (window.history.replaceState) {
			window.history.replaceState(pageNum, "Cambridge Digital Library", "#" + pageNum);
		} else if (window.location) {
			window.location.hash = pageNum;
		}
		return false;
	};

	// query duration
	var requestStartTime = 0, requestTime = 0;
	$(document).ajaxStart(function() {
		requestStartTime = new Date().getTime();
	});

	// list item number
	//var listNo = 0, currentPage = 0, page_Limit = 20;

	function pageinit() {

		var pageLimit = 20;
		var numResults = <%=resultSet.getNumberOfResults()%>;
  
	  	// Setup spinner. 
	  	var opts = {
				length: 7, // The length of each line
				width: 4, // The line thickness
				radius: 10, // The radius of the inner circle
		};
		
	  	var target = document.getElementById('content');
		
	  	var spinner = new Spinner(opts);
		  
		// Setup pagination
		var Paging = $(".pagination").paging(
						numResults,
						{
							format : "< (q-) ncnnnnnn (-p) >", //[< (q-) ncnnnnnn (-p) >]
							perpage : pageLimit,
							lapping : 0,
							page : 1,
							onSelect : function(page) {
								
								spinner.spin(target);

								$.ajax({
									"url" : '/search/JSON?start=' + this.slice[0] + '&end=' + this.slice[1] + '&<%=request.getQueryString()%>',
									"success" : function(data) {

										// query duration
										requestTime = new Date().getTime() - requestStartTime;
										$("#reqtime").text(moment.duration(parseInt(requestTime)).asSeconds() + ' seconds');
										
										// current page
										//currentPage = page - 1;
										
										spinner.stop();

										// content replace					                   
										var container = document.getElementById("collections_carousel");

										// Remove all children
										container.innerHTML = '';

										// add in the results
										for (var i = 0; i < data.length; i++) {
											var result = data[i];
											var item = result.item;
											var imageDimensions = "";
											if (item.thumbnailOrientation == "portrait") {
												imageDimensions = " style='height:100%' ";
											} else if (item.thumbnailOrientation == "landscape") {
												imageDimensions = " style='width:100%' ";
											}
											var title = item.title;
											if (result.itemType == "essay") {
												title = "Essay: " + title;
											}

											var itemDiv = document.createElement('div');
											itemDiv.setAttribute("class", "collections_carousel_item campl-column12");
											var itemText = "<div class='collections_carousel_image_box campl-column4'>"
													+ "<div class='collections_carousel_image'>"
													+ "<a href='/view/" +item.id+ "/"+result.startPage+"'><img src='" +result.pageThumbnailURL+ "' alt='" +item.id+ "' "+ imageDimensions+ " > </a></div></div> "
													//+ "<div class='collections_carousel_text campl-column8'><h5>"
													+ "<div class='collections_carousel_text campl-column8'><h3>"
													//+ "# " + (currentPage * pageLimit + i + 1) + " "
													//+ title
													+ "<a href='/view/" +item.id+ "/"+result.startPage+"'>" + title + "</a>" //+ " </h3>"
													//+ " <font style='color:#999'>(" + item.shelfLocator + " Page: " + result.startPageLabel
													+ " <font style='color:#999;font-weight:normal;font-size:14px;'>(" + "<span title='Shelf locator'>" +item.shelfLocator + "</span>" + (item.shelfLocator[0].length <= 0 ? "" : " ") + "Page: " + result.startPageLabel + ")</font>"
													//+ ")</font></h5> "
													+ "</h3> "
													+ item.abstractShort
													+ " ... <br/><br/><ul>";

											for (var j = 0; j < result.snippets.length; j++) {
												var snippet = result.snippets[j];
												if (snippet != "" && snippet != "undefined") {
													var snippetLabel = "";
													//itemText += "<li><a href='/view/" +item.id+ "/"+result.startPage+"'>" + snippet + "</a> </li>";
													//itemText += "<li><span><a href='/view/" +item.id+ "/"+result.startPage+"'>" + styleSnippet(snippet) + "</a></span></li>";
													itemText += "<li><span>" + styleSnippet(snippet) + "</span></li>";
												}
											}

											itemText += "</ul></div><div class='clear'></div>";
											itemDiv.innerHTML = itemText;
											container.appendChild(itemDiv);
										}

										/* 
										  $('.collections_carousel_text').truncate({  
										    max_length: 260,  
										      more: "view more",  
										      less: "hide"
										  }); 
										 */
										 
										// update height of left panel
										/* $(".campl-column4.campl-secondary-content").height(
												$(".campl-column8.campl-content").height()
										); */
									}
								});

								return false;
							},
							onFormat : function(type) {

								switch (type) {

									case 'block':
										if (!this.active)
											return '<span class="disabled">' + this.value + '</span>';
										else if (this.value != this.page)
											return '<em><a href="" onclick="viewPage(' + this.value + '); return false;">' + this.value + '</a></em>';
										return '<span class="current">' + this.value + '</span>';
	
									case 'right':
										
									case 'left':
										if (!this.active) {
											return '';
										}
										return '<a href="" onclick="viewPage(' + this.value + '); return false;">' + this.value + '</a>';
	
									case 'next':
										if (this.active)
											return '<a href="" onclick="viewPage(' + this.value
													+ '); return false;" class="next"><img src="/images/interface/icon-fwd-btn-larger.png" class="pagination-fwd"/></a>';
										return '<span class="disabled"><img src="/images/interface/icon-fwd-btn-larger.png" class="pagination-fwd"/></span>';
	
									case 'prev':
										if (this.active)
											return '<a href="" onclick="viewPage(' + this.value
													+ '); return false;" class="prev"><img src="/images/interface/icon-back-btn-larger.png" class="pagination-back"/></a>';
										return '<span class="disabled"><img src="/images/interface/icon-back-btn-larger.png" class="pagination-back"/></span>';
	
									case 'first':
										if (this.active)
											return '<a href="" onclick="viewPage(' + this.value + '); return false;" class="first">|<</a>';
										return '<span class="disabled">|<</span>';
	
									case 'last':
										if (this.active)
											return '<a href="" onclick="viewPage(' + this.value + '); return false;" class="last">>|</a>';
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
		var hashChange = function() {
			if (window.location.hash)
				Paging.setPage(window.location.hash.substr(1));
			else
				Paging.setPage(1); // we dropped the initial page selection and need to run it manually
		};

		$(window).bind('hashchange', hashChange);
		hashChange();

		// Show the pagination toolbars if enough elements are present
		if ((numResults / pageLimit) > 1) {
			$(".toppagination")[0].style.display = "block";
			$(".toppagination")[1].style.display = "block";
		} else {
			$(".toppagination")[0].style.display = "none";
			$(".toppagination")[1].style.display = "none";
		}
		
	}
	
</script>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-main-content" id="content">
			<!-- <div> -->
				<div class="campl-column4 campl-secondary-content">
				
					<div class="searchform box ">

						<div class="campl-content-container">
							<form:form commandName="searchForm" class="grid_5" action="/search" method="GET">

								<form:input path="keyword" class="search" type="text" value="<%=Encode.forHtmlAttribute(form.getKeyword())%>" name="keyword" placeholder="Search" />
								<input class="campl-search-submit " src="/images/interface/btn-search-header.png" type="image">
								<%
									Iterator<String> facetsUsedHidden = form.getFacets().keySet().iterator();
									while (facetsUsedHidden.hasNext()) {
										String facetName = facetsUsedHidden.next();
										String facetValue = form.getFacets().get(facetName);
								%>
								<input path="<%=Encode.forHtmlAttribute(facetName)%>" type="hidden" name="facet-<%=Encode.forHtmlAttribute(facetName)%>" value="<%=Encode.forHtmlAttribute(facetValue)%>" />
								<%
									}
								%>
								<form:input path="fileID" type="hidden" name="fileID" value="<%=Encode.forHtmlAttribute(form.getFileID())%>" />

							</form:form>
							<div class="altsearchlink grid_5">
								<form:form commandName="searchForm" action="/search/advanced/query" method="GET">
									<input type="hidden" value="<%=Encode.forHtmlAttribute(form.getKeyword())%>" name="keyword" />
									<input class="altsearchlink" type="submit" value="Advanced Search" />
								</form:form>
							</div>

							<%
								Iterator<String> facetsUsed = form.getFacets().keySet().iterator();
								while (facetsUsed.hasNext()) {
									String facetName = facetsUsed.next();
									String facetValue = form.getFacets().get(facetName);
							%>
							<div class="search-facet-selected">
								<%-- <a class="search-close" href="?<%=SearchUtil.getURLParametersWithoutFacet(form, Encode.forHtmlAttribute(facetName))%>&amp;">X</a>
								<%
									out.print("in " + Encode.forHtml(facetValue));
								%> --%>
								<a class="search-close" href="?<%=SearchUtil.getURLParametersWithoutFacet(form, facetName)%>&amp;" title="Remove"> 
									in <b><% out.print("<span>" + Encode.forHtml(facetValue) + "</span>"); %></b> 
									<% out.println(" (" + Encode.forHtml(facetName) + ")"); %> &Cross; 
								</a>
							</div>
							<%
								}
								if (form.getFileID() != null && form.getFileID().trim().length() > 0) {
							%>
							<div class="search-facet-selected">
								<a class="search-close" href="?<%=SearchUtil.getURLParameters(form).replace("fileID=" + form.getFileID(), "fileID=")%>&amp;">X</a>
								<%
									out.print("CUDL ID: " + Encode.forHtml(form.getFileID()));
								%>
							</div>
							<%
								}
								if (resultSet.getSpellingSuggestedTerm() != null && !resultSet.getSpellingSuggestedTerm().equals("")) {
									out.println("Did you mean <a href=\"/search?keyword=" + resultSet.getSpellingSuggestedTerm() + "\">" + resultSet.getSpellingSuggestedTerm() + "</a> ?");
								}

								if (form.getKeyword() != null && !form.getKeyword().equals("")) {
									out.println("<div class=\"_mib\"></div>");
									out.println("<div class=\"campl-column12 resultcount\"> About " +
											NumberFormat.getNumberInstance(Locale.US).format(resultSet.getNumberOfResults()) +
											" results (<span id=\"reqtime\"></span></span>)</div>");
								}
							%>
						</div>
					</div>
					
					<%
						if (resultSet.getNumberOfResults() > 0) {
					%>
					<div class="campl-content-container">
						<h5>Refine by:</h5>
						<ol id="tree" class="campl-unstyled-list">
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
										out.println("<li><strong><span>&#9662; </span>" + fieldLabel + "</strong><ul class='campl-unstyled-list'>");

										for (int j = 0; j < facets.size(); j++) {
											Facet facet = facets.get(j);
											
											out.print("<li><a href='?" + SearchUtil.getURLParametersWithExtraFacet(form, field, facet.getBand()) + "'>");
											out.print(facet.getBand() + "</a> (" + facet.getOccurences() + ")</li>");
										}
										out.println("</ul></li>");
									}
								}
							}
						%>
						</ol>
					</div>
					<%
						}
					%>
					
				</div>
				<!-- <div class="campl-column8" id="pagination_container"> -->
				<div class="campl-column8 campl-content">

					<%
						List<SearchResult> results = resultSet.getResults();

						// No results were returned. So print out some help.
						if (resultSet.getNumberOfResults() == 0) {
							out.println("<div class=\"searchexample campl-content-container\">");
							
							if (form.getKeyword() != null && !form.getKeyword().equals("")) {
								out.println("<p class=\"box\">We couldn't find any items matching <b>" + Encode.forHtml(form.getKeyword()) + "</b></p>");
							}
							
							out.println("<h5>Example Searches</h5><br/><p>");
							out.println("Searching for <span class=\"search\">newton</span> Searches the metadata for 'newton'<br/>");
							out.println("Searching for <span class=\"search\">isaac newton</span> Searches for 'isaac' AND 'newton'<br/>");
							out.println("Searching for <span class=\"search\">\"isaac newton\"</span> Searches for the phrase 'isaac newton'<br/>");
							out.println("The characters <b>?</b> and <b>*</b> can be used as wildcards in your search.<br />");
							out.println("Use <b>?</b> to represent one unknown character and <b>*</b> to represent any number of unknown characters.<br/>");
							out.println("</p></div>");
						} else {
					%>

							<!-- <div class="pagination toppagination"></div> -->
							<!-- start of list -->
							<div id="collections_carousel" class="collections_carousel">
							</div>
							<!-- end of list -->
							<div class="pagination toppagination"></div>
					
					<%	
						} 
					%>

				</div>
			<!-- </div> -->
		</div>
	</div>
</div>

<script type="text/javascript">
	//sticky menu
	$(document).ready(function(){
		/* var bs = $('.campl-local-footer').outerHeight() + $('.campl-global-footer').outerHeight();
		$('.campl-secondary-content').sticky({
			topSpacing:0,
			bottomSpacing:bs
		}); */
	});
	
	// collapsible list
	$("#tree > li > strong").on("click", function(e) {
		if($(this).parent().find("span").html().match("^\u25be")) $(this).parent().find("span").html("\u25b8 ")
		else $(this).parent().find("span").html("\u25be ")
		$(this).parent().find("ul").toggleClass("hide");
	});
	
	//style snippet
	function styleSnippet(s) {
		s = s.replace(/<b>/g, "<span class=\"campl-search-term\">");
		s = s.replace(/<\/b>/g, "</span>");
		return s;
	}
</script>

<jsp:include page="header/footer-full.jsp" />
