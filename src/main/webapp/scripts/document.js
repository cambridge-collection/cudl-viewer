/*

    We have the following variables set by the Java. 

	cudl.JSONURL
	cudl.JSONTHUMBURL
	cudl.pagenum 
	cudl.docId 
	cudl.docURL 
	cudl.imageServer 
	cudl.services 
	
	// Read in Attributes
	cudl.collectionURL 
	cudl.collectionTitle 
	cudl.parentCollectionURL 
	cudl.parentCollectionTitle 
	cudl.itemTitle 
	cudl.itemAuthors
	cudl.itemAuthorsFullForm 

 */

cudl.data; // stores the JSON data for this book.
var store = {}; // This is used when changing pages from links in the text.
cudl.currentThumbnailPage = 1;

cudl.thumbnailProps = {}; // stores thumbnail properties
cudl.thumbnailProps.MAX_THUMBNAIL_ITEMS_ON_PAGE = 42;
cudl.thumbnailProps.MAX_THUMBNAIL_ITEMS_ON_ROW = 3;

store.loadPage = function(pagenumber) {

	// validation
	if (isNaN(pagenumber)) { alert ("Please enter a number."); return; }
	else if (pagenumber < 0 ) {	pagenumber = 0;	} 
	else if (pagenumber > cudl.data.numberOfPages ) {
		pagenumber = cudl.data.numberOfPages;
	}
	
	// test for images
	var imageavailable = true;	
	if (typeof(cudl.data.pages[pagenumber-1].displayImageURL) == "undefined") {		
		cudl.viewer._showMessage("No image available for page: "+cudl.data.pages[pagenumber-1].label);
		imageavailable = false;
	}
		
	function openDzi(dziPath) {

		// ajax call to fetch .dzi
		$.get(cudl.imageServer + dziPath).success(function(xml) {

			// Seadragon AJAX supported being given a DZI as a string
			// and rewriting the tilesource to an external URL
			// openseadragon won't accept an external DZI so we build an
			// inline tilesource with a modified URL

			xmlDoc = $.parseXML(xml);
			$xml = $(xmlDoc);			
			
			if(navigator.appVersion.indexOf("MSIE 9.")!=-1) {
			  // If using IE 9
			  $image = $(xml).find('Image');
			  $size = $(xml).find('Size');
			  
			} else { 
		      // Any other browser
			  $image = $xml.find('Image');
			  $size = $xml.find('Size');
			}
			
			var path = dziPath;
			path = path.substring(0, path.length - 4);
			var dzi = {
				Image : {
					xmlns : $image.attr('xmlns'),
					Url : cudl.imageServer + path + '_files/',
					Format : $image.attr('Format'),
					Overlap : $image.attr('Overlap'),
					TileSize : $image.attr('TileSize'),
					Size : {
						Height : $size.attr('Height'),
						Width : $size.attr('Width')
					}
				}
			};
			
			cudl.viewer.open(dzi);
		}).error(function(jqXHR, textStatus, errorThrown) {			
			cudl.viewer._showMessage("Image server temporarily unavailable");
		});
	}
	;
	

	// open Image
	if (imageavailable) { openDzi(cudl.data.pages[pagenumber - 1].displayImageURL); }

	// update current page
	cudl.pagenum = pagenumber;
	$("#pageInput").val(cudl.pagenum);
	$("#maxPage").html(cudl.data.numberOfPages);
	
	// update transcription data
	cudl.setTranscriptionPage(cudl.data, pagenumber);	

	// update metadata
	cudl.updatePageMetadata(cudl.data, pagenumber);
	
	// Google analytics
	ga('create', googleAnalyticsID, 'auto');
	ga('send', 'pageview');
	// end of analytics

};

// Update the metadata that changes on page change
cudl.updatePageMetadata = function (data, pagenumber) {

	try {
       var newURL = "/view/" + cudl.docId + "/" + pagenumber;
       $('#downloadCopyright').html(data.descriptiveMetadata[0].downloadImageRights);
       $('#currentURL').val("http://cudl.lib.cam.ac.uk"+newURL);
       $('#embedCode').val("<div style='position: relative; width: 100%; padding-bottom: 80%;'><iframe type='text/html' width='600' height='410' style='position: absolute; width: 100%; height: 100%;' src='http://cudl.lib.cam.ac.uk/embed/#item="+cudl.docId+"&page="+pagenumber+"&hide-info=true' frameborder='0' allowfullscreen='' onmousewheel=''></iframe></div>")
       cudl.highlightMetadataForPageViewed(pagenumber, data.logicalStructures);
       $('#pageLabel').html("Page: "+data.pages[pagenumber-1].label);

       // update URL bar, does not work in ie9. 	
	   window.history.replaceState(cudl.docId + " page:"+ pagenumber, "Cambridge Digital Library",newURL);
	   
	} catch (e) { /* the above does not work in fullscreen mode*/ }
		
};


cudl.setupSeaDragon = function(data) {

	OpenSeadragon.setString("Tooltips.Home", "Reset View");
	cudl.viewer = new OpenSeadragon.Viewer({
		id : "doc",
		debugMode : false,
		prefixUrl : "/img/",
		showRotationControl : true,
		zoomInButton : "zoomIn",
		zoomOutButton : "zoomOut",
		rotateLeftButton : "rotateLeft",
		rotateRightButton : "rotateRight",
		fullPageButton: "fullscreen"
	});
	cudl.viewer.clearControls(); // hides controls.

	// Setup forward and backward buttons
	function nextPage() {

		if (cudl.pagenum < cudl.data.pages.length) {
			cudl.pagenum++;
			store.loadPage(cudl.pagenum);			
		}
		return false;
	}

	function prevPage() {

		if (cudl.pagenum > 1) {
			cudl.pagenum--;
			store.loadPage(cudl.pagenum);
		}
		return false;
	}

	$("#nextPage").click(function() {
		return nextPage();
	});
	$("#prevPage").click(function() {
		return prevPage();
	});
	$("#pageInput").change(function(input) {
		store.loadPage(input.target.value);
	});
	
	// move the pagination controls to the image when going fullscreen
	// then back to the header bar when full screen is exited. 
	// Also update transcriptions / metadata etc elements when 
	// going back to normal view as page may have changed. 
	cudl.viewer.addHandler("pre-full-screen", function (data) {
	    if (data.fullScreen) {
	      $(".cudl-viewer-buttons-pagination").appendTo("#doc");
	    }
	});	
	cudl.viewer.addHandler("full-screen", function (data) {
	    if (data.fullScreen) {
	      $('#doc').css("top", "0px");
	    } else {	    	
		  $(".cudl-viewer-buttons-pagination").appendTo(".navbar-header");
		  cudl.setTranscriptionPage(cudl.data, cudl.pagenum);	
		  cudl.updatePageMetadata(cudl.data, cudl.pagenum);
		  $('#doc').css("top", "68px");
	    }
	});	
	
	// setup keyboard shortcuts.  Same as the embedded viewer. 
	$(window).keypress(function(e) {

		    switch (e.charCode) {
		        case 118:  // v 
					cudl.pagenum++;
					store.loadPage(cudl.pagenum);	
					return false;
		        case 99: // c
		        	cudl.pagenum--;
					store.loadPage(cudl.pagenum);	
		            return false;       
		        case 113: case 43: // q or + zoom in
		        	cudl.viewer.viewport.zoomBy(2); 
		            return false;
		        case 101: case 45: // e or - zoom out 
		        	cudl.viewer.viewport.zoomBy(0.5); 
		            return false;	 
		        case 122: // z rotate left 90	        	
	        		cudl.viewer.viewport.setRotation(cudl.viewer.viewport.getRotation()-90);
		            return false;	
		        case 120: // x rotate right 90
		        	cudl.viewer.viewport.setRotation(cudl.viewer.viewport.getRotation()+90);
		            return false;	 
		        case 90: // Z rotate left		        	
	        		cudl.viewer.viewport.setRotation(cudl.viewer.viewport.getRotation()-10);
		            return false;	
		        case 88: // X rotate right
		        	cudl.viewer.viewport.setRotation(cudl.viewer.viewport.getRotation()+10);
		            return false;	 f
		        case 102: // f fullscreen toggle
		        	if (cudl.viewer.isFullPage()) {
		        	  cudl.viewer.setFullScreen(false);
		        	} else {
		        	  cudl.viewer.setFullScreen(true);	
		        	}  
		            return false;		
		        case 114: // r toggle right panel
		        	cudl.toggleRightPanel();
		            return false;			            
		            
		    }

	});	
	
	$('.openseadragon-canvas').focus();	

};

cudl.setupInfoPanel = function(data) {

	breadcrumbHTML = "<ol class=\"breadcrumb\"><li><a href='/'>Home</a></li><li><a href=\""
			+ cudl.collectionURL
			+ "\">"
			+ cudl.collectionTitle
			+ "</a></li></ol>";
	if (cudl.parentCollectionTitle) {
		breadcrumbHTML = "<ol class=\"breadcrumb\"><li><a href='/'>Home</a></li><li><a href=\""
				+ cudl.parentCollectionURL
				+ "\">"
				+ cudl.parentCollectionTitle
				+ "</a></li><li><a href=\""
				+ cudl.collectionURL
				+ "\">"
				+ cudl.collectionTitle + "</a></li></ol>";
	}
	$('#doc-breadcrumb').html(breadcrumbHTML);
	
	$('#about-header').html(cudl.itemTitle+ " ("+data.descriptiveMetadata[0].shelfLocator.displayForm+")");	
	$('#about-imagerights').html(data.descriptiveMetadata[0].displayImageRights);
	try {
		$('#about-abstract').html(data.descriptiveMetadata[0].abstract.displayForm);
	} catch (ex) { /* ignore, not all items have value */} 
	if (data.completeness) {   
		$('#about-completeness').html("<p>"+data.completeness+"</p>");
	} 

	try {
		// Set contents panel
		function addTreeNodes(array, edgeSpacing) {
			list = "";
			for (var i = 0; i < array.length; i++) {
				list = list.concat("<a href='' onclick='store.loadPage("
						+ (array[i].startPagePosition)
						+ ");return false;' class='list-group-item'>"+edgeSpacing
						+ array[i].label
						+" <span class='about-content-page'>"
						+"(image "+ array[i].startPagePosition
						+", page "+ array[i].startPageLabel+")</span>"
						+" </a> ");				
				if (array[i].children) {
					list = list.concat(addTreeNodes(array[i].children, edgeSpacing.concat(" &nbsp;&nbsp;&nbsp; ")));
				}
			}
			return list;
		}
		var contents = "<div class=\"list-group\">"
				+ addTreeNodes(data.logicalStructures[0].children, "") + "</div>";
		$('#contentstab').html(contents);
	} catch (ex) { /* ignore, no contents list */
	}
	
	// Enable / disable menus
	// Note not all pages may have a transcription/translation however
	// We are enabling the menu if any are available.
	
    if (typeof data.useNormalisedTranscriptions == 'undefined' || !data.useNormalisedTranscriptions) {
    	$('#transcriptionnormtab').parent().addClass("disabled");
    	$('#transcriptionnormtab').click(function(e){return false;}); // disable link;
    }
    if (typeof data.useDiplomaticTranscriptions == 'undefined' || !data.useDiplomaticTranscriptions) {
    	$('#transcriptiondiplotab').parent().addClass("disabled");
    	$('#transcriptiondiplotab').click(function(e){return false;}); // disable link;
    }
    
    if (typeof data.useTranslations == 'undefined' || !data.useTranslations) {	
    	$('#translationtab').parent().addClass("disabled");
    	$('#translationtab').click(function(e){return false;}); // disable link;
    }
    
    if (!data.logicalStructures[0].children) {    
    	$('#rightTabs a[href="#contentstab"]').parent().addClass("disabled");
    	$('#rightTabs a[href="#contentstab"]').click(function(e){return false;}); // disable link;
    }
    
    // NB: This will disable thumbnails if the first page has no image. This assumes that 
    // the there are documents either with a complete set of thumbnails or no thumbnails.  
    if (typeof data.pages[0].thumbnailImageURL == 'undefined') {    
    	$('#rightTabs a[href="#thumbnailstab"]').parent().addClass("disabled");
    	$('#rightTabs a[href="#thumbnailstab"]').click(function(e){return false;}); // disable link;
    }    

	// setup toggle behaviour
	var infoPanelExpanded = true;
	
	cudl.toggleRightPanel = function() {

		if (infoPanelExpanded) {
			$('#right-panel').css({
				'right' : ($('#right-panel').width() * -1)
			});
			infoPanelExpanded = false;
			$('#doc').width('100%');
			

			// update icon
			$('#right-panel-toggle i').removeClass('fa-angle-right');
			$('#right-panel-toggle i').addClass('fa-angle-left');
			
		} else {
			$('#right-panel').css({
				'right' : '0px'
			});
			infoPanelExpanded = true;
			$('#doc').css( {'width':($(window).width() - $('#right-panel').width())} );
			
			// update icon
			$('#right-panel-toggle i').removeClass('fa-angle-left');
			$('#right-panel-toggle i').addClass('fa-angle-right');
						
		}
				
	}
	$('#right-panel-toggle').click(cudl.toggleRightPanel);
	if ($(window).width()<550) { cudl.toggleRightPanel(); }
	
	//update panel positon on resize
	$(window).resize(function () { 
		if (!infoPanelExpanded) {
		  $('#right-panel').css( {'right':($('#right-panel').width() * -1)} );
		}
	});

};

/* Allows you to link to a tab panel */
cudl.showPanel = function (panelHREF) {
	
	$('a[href="' + panelHREF + '"]').tab('show');

};

cudl.addBookmark = function () {	
	
	// Generate bookmarkPath 	
	var thumbnailURL = cudl.imageServer+cudl.data.pages[cudl.pagenum-1].thumbnailImageURL;
	var bookmarkPath = "/mylibrary/addbookmark/?itemId="+cudl.docId+"&page="+cudl.pagenum+"&thumbnailURL="+encodeURIComponent(thumbnailURL);
	
	// ajax call to make the bookmark:	
	$.get(bookmarkPath).success(function(xml) {
		
		// parse JSON response.
		if (xml.bookmarkcreated==true) {			
			
			//created bookmark successfully.				
			$('#bookmarkConfirmation').hide();		
			window.confirm('Added Bookmark Successfully.');			
			return true; 
		} else {
			//failed to create bookmark so manually redirect to login page
			$('#bookmarkConfirmation').hide();
			window.location.href = bookmarkPath+"&redirect=true";			
		}
		
	});
	
	// failed to create bookmark.	
	return false;
	
}

cudl.downloadImage = function () {
	
  $('#downloadConfirmation').hide();
  var downloadImageURL = cudl.data.pages[cudl.pagenum-1].downloadImageURL;
  if (typeof downloadImageURL != "undefined") {
        window.open(cudl.imageServer+downloadImageURL);    
  } else {
	  alert ("No image available to download.");
  }
    
}

cudl.setupThumbnails = function (data) {

	var props = cudl.thumbnailProps;
	props.NUM_THUMBNAIL_PAGES = 1; // set below
	function generatePagination(data) {

		// create the pagination
		NUM_THUMBNAIL_PAGES = 1;
		if (data.numberOfPages > props.MAX_THUMBNAIL_ITEMS_ON_PAGE) {
			props.NUM_THUMBNAIL_PAGES = Math.ceil(data.numberOfPages
					/ props.MAX_THUMBNAIL_ITEMS_ON_PAGE);
		}

		var html = "<ul class='pagination'>";
		html = html
				.concat("<li class='thumbnailpaginationstart' onclick='return cudl.showThumbnailPage(cudl.currentThumbnailPage-1);'><a href='#'>&laquo;</a></li>");

		for (i = 1; i <= props.NUM_THUMBNAIL_PAGES; i++) {
			html = html.concat("<li class='thumbnailpaginationitem" + i
					+ "'><a href='#' onclick='return cudl.showThumbnailPage(" + i
					+ ");'>" + i + "</a></li>");
		}

		html = html
				.concat("<li class='thumbnailpaginationend'><a href='#'	onclick='return cudl.showThumbnailPage(cudl.currentThumbnailPage+1);'>&raquo;</a></li></ul>");

		$('#thumbnailpaginationtop').html(html);
		$('#thumbnailpaginationbottom').html(html);
	}

	generatePagination(data);

};


cudl.showThumbnailPage = function (pagenum) {
		
	/**
	 * PageNum should be between 1 and NUM_THUMBNAIL_PAGES
	 */
	function showThumbnailPageImages(props, pageNum, data) {

		// find the startIndex and endIndex for the data items we want to display
		// thumbnails of.
		var startIndex = props.MAX_THUMBNAIL_ITEMS_ON_PAGE * (pageNum - 1);
		var endIndex = Math.min((props.MAX_THUMBNAIL_ITEMS_ON_PAGE * pageNum) - 1,
				data.pages.length - 1);
		var thumbnailhtml = "";

		for (i = startIndex; i <= endIndex; i++) {

			if (i == startIndex) {
				thumbnailhtml = thumbnailhtml
						.concat("<div class='thumbnail-pane' id='thumbnail"
								+ pageNum + "'>");
			}
			if (i == startIndex || ((i) % props.MAX_THUMBNAIL_ITEMS_ON_ROW) == 0) {

				thumbnailhtml = thumbnailhtml.concat("<div class='row'>");
			}

			thumbnailhtml = thumbnailhtml
					.concat("<div class='col-md-4'><a href='' onclick='store.loadPage("
							+ (data.pages[i].sequence)
							+ ");return false;' class='thumbnail'><img src='"
							+ cudl.imageServer
							+ data.pages[i].thumbnailImageURL
							+ "' ");

			if (data.pages[i].thumbnailImageOrientation == "portrait") {
				thumbnailhtml = thumbnailhtml
						.concat("style='height:150px;'><div class='caption'>"
								+ data.pages[i].label + "</div></a></div>");
			} else {
				thumbnailhtml = thumbnailhtml
						.concat("style='width:130px;'><div class='caption'>"
								+ data.pages[i].label + "</div></a></div>");
			}

			if (i == endIndex
					|| ((i) % props.MAX_THUMBNAIL_ITEMS_ON_ROW) == props.MAX_THUMBNAIL_ITEMS_ON_ROW - 1) {
				thumbnailhtml = thumbnailhtml.concat("</div>");
			}
			if (i == endIndex) {
				thumbnailhtml = thumbnailhtml.concat("</div>");
			}

		}

		$('#thumbnailimages').html(thumbnailhtml);
	};
	
	
	if (pagenum > 0 && pagenum <= cudl.thumbnailProps.NUM_THUMBNAIL_PAGES) {
		cudl.currentThumbnailPage = pagenum;
		showThumbnailPageImages(cudl.thumbnailProps, cudl.currentThumbnailPage,
				cudl.data);

		// Update pagination page selected

		$("#thumbnails-content ul.pagination").find("li").removeClass('active');
		$("#thumbnails-content ul.pagination").find(
				".thumbnailpaginationitem" + pagenum).addClass("active");

	}
};

cudl.setupMetadata = function (data) {
	
	// set content of more info tab. 
	$('#metadatacontent').html("<ul>"+ getHTMLForLogicalStructure(data.logicalStructures, 0, Number.MAX_VALUE)+"</ul>");
	
	// set metadata section of about tab. 
	$('#about-metadata').html("<br/>"+getHTMLForLogicalStructure([data.logicalStructures[0]], 0, 0));
	
	
  function getHTMLForLogicalStructure(array, level, maxlevel) {
			
	var html = "";
		
	for (var i=0; i<array.length; i++) {
	    			  
	  var meta = findDescriptiveMetadata(array[i].descriptiveMetadataID, data);
	  html = html.concat("<div class=\"panel-group\" id=\"accordion\"><div class=\"panel panel-default panel"+array[i].startPagePosition+"-"+array[i].endPagePosition+"\">");
	  html = html.concat("<div class=\"panel-heading\"><h4 class=\"panel-title\">");
	  //html = html.concat("<a data-toggle=\"collapse\" data-target=\"#collapse"+array[i].descriptiveMetadataID+"\" href=\"#collapse"+array[i].descriptiveMetadataID+"\">");
	  if (level==0) { 
		  html = html.concat("Information about this document"); // title for full document metadata
	  } else {
		  html = html.concat("Section shown in images "+array[i].startPagePosition+" to "+array[i].endPagePosition);
	  }
	  //html = html.concat("</a>");
	  html = html.concat("</h4></div><div id=\"collapse"+array[i].descriptiveMetadataID+"\" class=\"panel-collapse collapse in\"><div class=\"panel-body\">");
	  html = html.concat("<ul>"+getHTMLForDescriptiveMetadata(meta)+"</ul>");
	  html = html.concat("</div></div></div>");
	   	
	  if (array[i].children && level<maxlevel) {		
		level = level+1;	
	   	html = html.concat(getHTMLForLogicalStructure(array[i].children, level, maxlevel));
	  }
	}
    return html;
  }
	
	function findDescriptiveMetadata (id, data) {

		for ( var i = 0; i < data.descriptiveMetadata.length; i++) {
			if (data.descriptiveMetadata[i].ID == id) {
				return data.descriptiveMetadata[i];
			}
		}
	};
	
	function getHTMLForDescriptiveMetadata(descriptiveMetadata) {
		var metadata = "";

		var metadataArray = getArrayFromDescriptiveMetadata(descriptiveMetadata);
		metadataArray = metadataArray.sort(function (a,b) {if (a.seq && b.seq) {return a.seq-b.seq;}});
		
		for (var i=0; i<metadataArray.length; i++) {
			var jsonObject = metadataArray[i];
				
				if (jsonObject.display == true && jsonObject.label) {

					// prioritise displayForm at top level. 
					if (jsonObject.displayForm) {

						metadata += getMetadataHTML(jsonObject.label, jsonObject.displayForm);

					// then display value (if an array)
					} else if (jsonObject.value
							&& jsonObject.value instanceof Array) {

					    metadata += getMetadataHTML(jsonObject.label, jsonObject.value, "; ");						     
					}

				}			
		}

		return metadata;
		//return "<li>"+descriptiveMetadata.title.displayForm+"</li>";
	}
	
	/**
	 * Converts the metadata item into HTML representation.
	 * 
	 * @param title -
	 *            Text for the label
	 * @param metadataItem -
	 *            Metadata item to display
	 * @param arraySeparator -
	 *            for arrays this is the text that will separate items.
	 * @returns
	 */
	function getMetadataHTML(title, metadataItem, arraySeparator) {

		var item = metadataItem;

		if (metadataItem instanceof Array) {

			var metadataArray = new Array();
			for ( var i = 0; i < metadataItem.length; i++) {

				var singleMetadataItem = metadataItem[i].displayForm;
				var searchLink = (metadataItem[i].linktype == "keyword search");
				if (searchLink) {
					metadataArray[i] = addSearchLink(singleMetadataItem);
				} else {
					metadataArray[i] = singleMetadataItem;
				}
			}

			if (metadataArray.length > 0) {
				item = metadataArray.join(arraySeparator);
			}

			// Not an array, a single item.
		} else {
			var searchLink = (metadataItem.linktype == "keyword search");
			if (searchLink) {
				item = addSearchLink(metadataItem);
			} else {
				item = metadataItem;
			}
		}

		if (item != "") {
			return "<li><div><b>" + title + ": </b>" + item
					+ "</div></li>\n";
		}

		return "";
	}

	function addSearchLink (text) {
		
		var linkText = text;
		
		// manually escape the special search characters ? and *.
		if (linkText.indexOf("?")!=-1){
			linkText = linkText.replace("?", "\\?");
			}
        if (linkText.indexOf("*")!=-1){
			linkText = linkText.replace("*", "\\*");
			}				
		
		linkText = encodeURIComponent(linkText);
		
		return "<a class=\"cudlLink\" href='/search?keyword=" + linkText
				+ "'>" + text + "</a>";
	}
	
	
	/**
	 * Used to go through each element in a single descriptiveMetadata item and look for 
	 * suitable candidates to display.  These are put into an array for sorting. 
	 * Suitable json objects have - display=true, seq and label fields.  
	 * Returns an array of json objects. 
	 */
	function getArrayFromDescriptiveMetadata(descriptiveMetadata) {

		var metadataArray = new Array();
		for ( var key in descriptiveMetadata) {
			if (descriptiveMetadata.hasOwnProperty(key) ) {					
				var jsonObject = descriptiveMetadata[key];
				
				// Handle case where there is no label at the top level, but there exists an 
				// array of objects under value that may have labels, display values or arrays of 
				// value strings to display.  
				if (!jsonObject.label && jsonObject.value && jsonObject.value instanceof Array) {
				  for (var i=0; i<jsonObject.value.length; i++) {
				    var value = jsonObject.value[0];
				    metadataArray = metadataArray.concat(getArrayFromDescriptiveMetadata(jsonObject.value[i]));
				  }
				}
				
			
				if (jsonObject.display==true && jsonObject.label && jsonObject.seq) {
					 metadataArray.push(jsonObject);
				}
			}
		}
		return metadataArray;
	}	
}

/**
 * Returns an array of logical structures for that apply to the
 * page given. Always includes ROOT logical structure.
 * Takes in an array of logical structures and a page number. 		 
 */
cudl.highlightMetadataForPageViewed = function(pageNumber, logicalStructures) {

	var lsArray = new Array();			
	for ( var i = 0; i < logicalStructures.length; i++) {
		var ls = logicalStructures[i];		
		
		if (ls.startPagePosition <= pageNumber
				&& ls.endPagePosition >= pageNumber) {
					
			$('.panel'+ls.startPagePosition+"-"+ls.endPagePosition).addClass("panel-info");			
			
		} else {

			if ($('.panel'+ls.startPagePosition+"-"+ls.endPagePosition).hasClass("panel-info")) {
			  $('.panel'+ls.startPagePosition+"-"+ls.endPagePosition).removeClass("panel-info");
			}
		}
		
		if (ls.children) {
			cudl.highlightMetadataForPageViewed(pageNumber, ls.children);
		}
		
	}

}

cudl.setTranscriptionPage = function (data, pagenum) {
	
	// check for existance of transcription frame, if not, 
	// then in fullscreen mode so do nothing. 
	if (!document.getElementById('transcriptionnormframe')) {
		return;
	}	
	
	// normalised transcriptions
	var url = data.pages[pagenum-1].transcriptionNormalisedURL;
	if (typeof url != 'undefined' && typeof data.allTranscriptionNormalisedURL == 'undefined') {		
		document.getElementById('transcriptionnormframe').src = cudl.services + url;		
	} else {
		document.getElementById('transcriptionnormframe').src = 'data:text/html;charset=utf-8,%3Chtml%3E%3Chead%3E%3Clink href%3D%22styles%2Fstyle-transcription.css%22 rel%3D%22stylesheet%22 type%3D%22text%2Fcss%22%2F%3E%0A%3C%2Fhead%3E%3Cbody%3E%3Cdiv class%3D%22transcription%22%3ENo transcription available for this image.%3C%2Fdiv%3E%3C%2Fbody%3E%3C%2Fhtml%3E';
	}
	
	// diplomatic transcriptions
	var url = data.pages[pagenum-1].transcriptionDiplomaticURL;
	if (typeof url != 'undefined' && typeof data.allTranscriptionDiplomaticURL == 'undefined') {
		document.getElementById('transcriptiondiploframe').src = cudl.services + url;		
	} else {
		document.getElementById('transcriptiondiploframe').src = 'data:text/html;charset=utf-8,%3Chtml%3E%3Chead%3E%3Clink href%3D%22styles%2Fstyle-transcription.css%22 rel%3D%22stylesheet%22 type%3D%22text%2Fcss%22%2F%3E%0A%3C%2Fhead%3E%3Cbody%3E%3Cdiv class%3D%22transcription%22%3ENo transcription available for this image.%3C%2Fdiv%3E%3C%2Fbody%3E%3C%2Fhtml%3E';
	}
	
	// translation
	var url = data.pages[pagenum-1].translationURL;
	if (typeof url != 'undefined') {
		document.getElementById('translationframe').src = cudl.services + url;		
	} else {
		document.getElementById('translationframe').src = 'data:text/html;charset=utf-8,%3Chtml%3E%3Chead%3E%3Clink href%3D%22styles%2Fstyle-transcription.css%22 rel%3D%22stylesheet%22 type%3D%22text%2Fcss%22%2F%3E%0A%3C%2Fhead%3E%3Cbody%3E%3Cdiv class%3D%22transcription%22%3ENo translation available for this image.%3C%2Fdiv%3E%3C%2Fbody%3E%3C%2Fhtml%3E';
	}

	// set all normalised transcriptions (all transcriptions on one page)
	var url = data.allTranscriptionNormalisedURL;
	if (typeof url != 'undefined') {
		document.getElementById('transcriptionnormframe').src = cudl.services + url;		
	} 
	
	// set all diplomatic transcriptions (all transcriptions on one page)
	var url = data.allTranscriptionDiplomaticURL;
	if (typeof url != 'undefined') {
		document.getElementById('transcriptiondiploframe').src = cudl.services + url;		
	} 
	
}


$(document).ready(function() {
	// Read in the JSON
	$.getJSON(cudl.JSONURL).done(function(data) {

		// tab content needs fixed height for scrolling
		$('#tab-content').height($(window).height() - 68 - 42);
		//$('#doc').height($(window).height() - 68);
		$(window).resize(function() {			
			if (!$('.fullpage').length) {
			//	$('#doc').height($(window).height() - 68);
				$('#tab-content').height($(window).height() - 68 - 42);
			}
		});

		// set seadragon options and load in dzi.
		if (cudl.pagenum==0) { cudl.pagenum=1; } // page 0 returns item level metadata.		
		cudl.data = data;
		cudl.setupSeaDragon(data);
		cudl.setupInfoPanel(data);
		cudl.setupThumbnails(data);
		cudl.setupMetadata(data);
		store.loadPage(cudl.pagenum);		
		cudl.showThumbnailPage(cudl.currentThumbnailPage);		
	});

});
