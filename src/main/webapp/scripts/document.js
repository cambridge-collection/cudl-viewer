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

	function openDzi(dziPath) {

		// ajax call to fetch .dzi
		$.get(cudl.imageServer + dziPath).success(function(xml) {

			// Seadragon AJAX supported being given a DZI as a string
			// and rewriting the tilesource to an external URL
			// openseadragon won't accept an external DZI so we build an
			// inline tilesource with a modified URL

			xmlDoc = $.parseXML(xml);
			$xml = $(xmlDoc);

			$image = $xml.find('Image');
			$size = $xml.find('Size');

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
	openDzi(cudl.data.pages[pagenumber - 1].displayImageURL);

	// update metadata
	var newURL = "/view/" + cudl.docId + "/" + cudl.pagenum;
	$('#downloadCopyright').html(cudl.data.descriptiveMetadata[0].downloadImageRights);
	$('#currentURL').html("http://cudl.lib.cam.ac.uk"+newURL);
	
	// update URL bar
	window.history.replaceState(cudl.docId + " page:"+ cudl.pagenum, "Cambridge Digital Library",newURL);
	
	// update transcription data
	cudl.setTranscriptionPage(cudl.data, pagenumber);	

	// update current page
	cudl.pagenum = pagenumber;
	$("#pageInput").val(cudl.pagenum);
	$("#maxPage").html(cudl.data.numberOfPages);
	return false;

};

// As OpenSeadragon currently has a bug with fullscreen and the youtube video
// panels.
cudl.toggleFullscreen = function () {

	var element = document.getElementById("doc");

	if (!document.fullscreenElement && !document.mozFullScreenElement
			&& !document.webkitFullscreenElement
			&& !document.msFullscreenElement) {
		if (element.requestFullscreen) {
			element.requestFullscreen();
		} else if (element.msRequestFullscreen) {
			element.msRequestFullscreen();
		} else if (element.mozRequestFullScreen) {
			element.mozRequestFullScreen();
		} else if (element.webkitRequestFullscreen) {
			element.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
		}

		// update icon
		$("#fullscreen").removeClass("fa-expand");
		$("#fullscreen").addClass("fa-compress"); 
		
	} else {
		if (document.exitFullscreen) {
			document.exitFullscreen();
		} else if (document.msExitFullscreen) {
			document.msExitFullscreen();
		} else if (document.mozCancelFullScreen) {
			document.mozCancelFullScreen();
		} else if (document.webkitExitFullscreen) {
			document.webkitExitFullscreen();
		}
		
		// update icon
		$("#fullscreen").removeClass("fa-compress");
		$("#fullscreen").addClass("fa-expand");
		
	}
}

cudl.setupSeaDragon = function(data) {

	OpenSeadragon.setString("Tooltips.Home", "Reset View");
	cudl.viewer = new OpenSeadragon.Viewer({
		id : "doc",
		debugMode : false,
		prefixUrl : "/img/",
		showRotationControl : true,
		maxZoomPixelRatio : 1,
		zoomInButton : "zoomIn",
		zoomOutButton : "zoomOut",
		rotateLeftButton : "rotateLeft",
		rotateRightButton : "rotateRight"
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
	$("#fullscreen").click(cudl.toggleFullscreen);
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

	aboutFooterHTML = "<br/><div class=\"well\"><h4>Want to know more?</h4><p>Under the 'More' menu you can find <a href='#' onclick='cudl.showPanel(\"#metadata\")'>metadata about the item</a>, any transcription and translation we have of the text and find out about <a href='#' onclick='cudl.showPanel(\"#download\")'>downloading or sharing this image</a>.  </p></div>";

	imageRights = "<div id='zoomRights'>"
			+ data.descriptiveMetadata[0].displayImageRights + "</div>";
	try {
		// Setup About panel.
		$('#rightTabs a[href="#abouttab"]').tab('show');
		$('#abouttab').html(
				breadcrumbHTML + "<div id='about-content'><h3>"
						+ cudl.itemTitle + " ("+data.descriptiveMetadata[0].shelfLocator.displayForm+")</h3><div>"
						+ data.descriptiveMetadata[0].abstract.displayForm
						+ "</div>" + aboutFooterHTML + imageRights);
	} catch (ex) { /* ignore, no contents list */
		// Should always be a title.
		$('#abouttab').html(
				breadcrumbHTML + "<div id='about-content'><h3>"
						+ cudl.itemTitle + "</h3><div>" + aboutFooterHTML
						+ imageRights);
	}

	try {
		// Set contents panel
		function addTreeNodes(array) {
			list = "";
			for (var i = 0; i < array.length; i++) {
				list = list.concat("<a href='' onclick='store.loadPage("
						+ (array[i].startPagePosition)
						+ ");return false;' class='list-group-item'>Page: "
						+ array[i].startPageLabel + ": &nbsp; "
						+ array[i].label + " </a> ");
				if (array[i].children) {
					list = list.concat("<div class=\"list-group\">"
							+ addTreeNodes(array[i].children) + "</div>");
				}
			}
			return list;
		}
		var contents = "<div class=\"list-group\">"
				+ addTreeNodes(data.logicalStructures[0].children) + "</div>";
		$('#contentstab').html(contents);
	} catch (ex) { /* ignore, no contents list */
	}
	
	// Enable / disable menus
	// Note not all pages may have a transcription/translation however
	// We are enabling the menu if any are available.
	
	console.debug(data);
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
    
    // setup fancybox for popups
    $("#inline").fancybox();	

	// setup toggle behaviour
	infoPanelExpanded = true;
	$('#right-panel-toggle').click(function() {
		if (infoPanelExpanded) {
			$('#right-panel').css({
				'right' : '0px'
			});
			infoPanelExpanded = false;
			$('#doc').width('60%');
			
			// update icon
			$('#right-panel-toggle i').removeClass('fa-angle-left');
			$('#right-panel-toggle i').addClass('fa-angle-right');
			
		} else {
			$('#right-panel').css({
				'right' : ($('#right-panel').width() * -1)
			});
			infoPanelExpanded = true;
			$('#doc').width('100%');
			
			// update icon
			$('#right-panel-toggle i').removeClass('fa-angle-right');
			$('#right-panel-toggle i').addClass('fa-angle-left');
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
	
	console.debug("thumbnailURL: "+thumbnailURL);
	console.debug("bookmarkPath: "+bookmarkPath);
	
	// ajax call to make the bookmark:	
	$.get(bookmarkPath).success(function(xml) {
		
		// parse JSON response.
		if (xml.bookmarkcreated==true) {			
			
			//created bookmark successfully.
			console.debug("added bookmark");				
			$.fancybox.close();			
			window.confirm('Added Bookmark Successfully.');			
			return true; 
		} else {
			//failed to create bookmark so manually redirect to login page
			$.fancybox.close();
			window.location.href = bookmarkPath+"&redirect=true";			
		}
		
	});
	
	// failed to create bookmark.	
	return false;
	
}

cudl.downloadImage = function () {

  function SaveToDisk(fileURL, fileName) {
    // for non-IE
    if (!window.ActiveXObject) {    	
        var save = document.createElement('a');
        save.href = fileURL;
        save.target = '_blank';
        save.download = fileName || 'unknown';

        var event = document.createEvent('Event');
        event.initEvent('click', true, true);
        save.dispatchEvent(event);
        (window.URL || window.webkitURL).revokeObjectURL(save.href);
    }

    // for IE
    else if ( !! window.ActiveXObject && document.execCommand)     {
        var _window = window.open(fileURL, '_blank');
        _window.document.close();
        _window.document.execCommand('SaveAs', true, fileName || fileURL)
        _window.close();
    }
    
  }
	
  var downloadImageURL = cudl.data.pages[cudl.pagenum-1].downloadImageURL;  
  window.open(cudl.imageServer+downloadImageURL);
    
    
}

cudl.setupThumbnails = function (data) {

	var props = cudl.thumbnailProps;
	props.NUM_THUMBNAIL_PAGES = 1; // set below
	function generatePagination(data) {

		// create the pagination
		NUM_THUMBNAIL_PAGES = 1;
		if (data.numberOfPages > props.MAX_THUMBNAIL_ITEMS_ON_PAGE) {
			props.NUM_THUMBNAIL_PAGES = Math.floor(data.numberOfPages
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
	
	$('#metadatacontent').html("<ul>"+ getHTMLForLogicalStructure(data.logicalStructures, 0)+"</ul>");
		
	function getHTMLForLogicalStructure(array, level) {
			
		var html = "";
		
	    for (var i=0; i<array.length; i++) {
	    			  
		  var meta = findDescriptiveMetadata(array[i].descriptiveMetadataID, data);
		  html = html.concat(getHTMLForDescriptiveMetadata(meta));	
	    	
	      if (array[i].children) {		
			level = level+1;	
		   	html = html.concat("<div class='well'><ul>"+getHTMLForLogicalStructure(array[i].children, level)+"</ul></div>");
		  }
		}
		return html;
	};
	
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


cudl.setTranscriptionPage = function (data, pagenum) {
	
	// normalised transcriptions
	var url = data.pages[pagenum-1].transcriptionNormalisedURL;
	if (typeof url != 'undefined') {
		document.getElementById('transcriptionnormframe').src = cudl.services + url;		
	} else {
		document.getElementById('transcriptionnormframe').src = 'data:text/html;charset=utf-8,%3Chtml%3E%3Chead%3E%3Clink href%3D%22styles%2Fstyle-transcription.css%22 rel%3D%22stylesheet%22 type%3D%22text%2Fcss%22%2F%3E%0A%3C%2Fhead%3E%3Cbody%3E%3Cdiv class%3D%22transcription%22%3ENo transcription available for this image.%3C%2Fdiv%3E%3C%2Fbody%3E%3C%2Fhtml%3E';
	}
	
	// diplomatic transcriptions
	var url = data.pages[pagenum-1].transcriptionDiplomaticURL;
	if (typeof url != 'undefined') {
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
}

$(document).ready(function() {
	// Read in the JSON
	$.getJSON(cudl.JSONURL).done(function(data) {

		// set element height to place the openseadragon image.
		// and ensure this value is updated on window resize.
		$('#doc').height($(window).height() - 50);
		$('#tab-content').height($(window).height() - 100);
		$(window).resize(function() {
			$('#doc').height($(window).height() - 50);
			$('#tab-content').height($(window).height() - 100);
		});

		// set seadragon options and load in dzi.
		if (cudl.pagenum==0) { cudl.pagenum=1; } // page 0 returns item level metadata. 
		cudl.data = data;
		cudl.setupSeaDragon(data);
		cudl.setupInfoPanel(data);
		cudl.setupThumbnails(data);
		cudl.setupMetadata(data);
		store.loadPage(cudl.pagenum);		
		cudl.showThumbnailPage(cudl.currentThumbnailPage)
	});

});
