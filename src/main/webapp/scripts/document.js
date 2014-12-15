/*

    We have the following variables set by the Java. 

	cudl.JSONURL
	cudl.JSONTHUMBURL
	cudl.pagenum 
	cudl.docId 
	cudl.docURL 
	cudl.imageServer 
	cudl.proxyURL
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
cudl.currentpage = 1; // stores current page viewed.

// This is used when changing pages from links in the text.
var store = {};
store.loadPage = function(pagenumber) {

	function openDzi(dziPath) {

		// ajax call to fetch .dzi
		$.get('http://found-dom02.lib.cam.ac.uk' + dziPath).success(
				function(xml) {

					// Seadragon AJAX supported being given a DZI as a string
					// and
					// rewriting the tilesource to an external URL
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

	// update current page
	cudl.currentpage = pagenumber;
	$("#pageInput").val(cudl.currentpage);
	return false;

};

// As OpenSeadragon currently has a bug with fullscreen and the youtube video
// panels.
function toggleFullscreen() {
	
	var element = document.getElementById("doc");
	
	if (!document.fullscreenElement && 
	!document.mozFullScreenElement && !document.webkitFullscreenElement
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

		if (cudl.currentpage < cudl.data.pages.length) {
			cudl.currentpage++;
			store.loadPage(cudl.currentpage);
		}
		return false;
	}

	function prevPage() {

		if (cudl.currentpage > 1) {
			cudl.currentpage--;
			store.loadPage(cudl.currentpage);
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
	$("#fullscreen").click(toggleFullscreen);
};

function setupInfoPanel(data) {

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

	aboutFooterHTML = "<br/><div class=\"well\"><h4>Want to know more?</h4><p>Under the 'More' menu you can find <a href='#download'>metadata about the item</a>, any transcription and translation we have of the text and find out about <a href='#download'>downloading or sharing this image</a>.  </p></div>";

	imageRights = "<div id='zoomRights'>"
			+ data.descriptiveMetadata[0].displayImageRights + "</div>";
	try {
		// Setup About panel.
		$('#rightTabs a[href="#abouttab"]').tab('show');
		$('#abouttab').html(
				breadcrumbHTML + "<div id='about-content'><h3>"
						+ cudl.itemTitle + "</h3><div>"
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

	// setup toggle behaviour
	infoPanelExpanded = true;
	$('#right-panel-toggle').click(function() {
		if (infoPanelExpanded) {
			$('#right-panel').css({
				'right' : '0px'
			});
			infoPanelExpanded = false;
			$('#doc').width('60%');
		} else {
			$('#right-panel').css({
				'right' : ($('#right-panel').width() * -1)
			});
			infoPanelExpanded = true;
			$('#doc').width('100%');
		}
	});

};

/**
 * ************* Thumbnails
 * *****************************************************
 */
function setupThumbnails(data) {
	/*
	 * var getPaginationProperties = function (data) {
	 * 
	 * var props = {};
	 * 
	 * var pageHeaderHeight = 120; var thumbnailItemHeight = 165; var
	 * thumbnailItemWidth = 165;
	 * 
	 * var tabWidth = parseInt($("#right-panel").width()); var tabHeight =
	 * parseInt($("#right-panel").height())-pageHeaderHeight; props.numItemInRow =
	 * Math.floor(tabWidth/thumbnailItemWidth); props.numRows =
	 * Math.floor(tabHeight/thumbnailItemHeight);
	 * 
	 * if (props.numItemInRow*props.numRows>0) {
	 * props.pageSize=props.numItemInRow*props.numRows;
	 * 
	 * props.numPages= Math.floor(data.numberOfPages/props.pageSize); }
	 * 
	 * return props; };
	 */
	var props = {};
	props.MAX_THUMBNAIL_ITEMS_ON_PAGE = 42;
	props.MAX_THUMBNAIL_ITEMS_ON_ROW = 3;
	props.NUM_THUMBNAIL_PAGES = 1; // auto generated
	function generatePagination(data) {

		// create the pagination
		NUM_THUMBNAIL_PAGES = 1;
		if (data.numberOfPages > props.MAX_THUMBNAIL_ITEMS_ON_PAGE) {
			props.NUM_THUMBNAIL_PAGES = Math.floor(data.numberOfPages
					/ props.MAX_THUMBNAIL_ITEMS_ON_PAGE);
		}

		var html = "<ul class='pagination'>";
		html = html
				.concat("<li class='thumbnailpaginationstart' onclick='return showThumbnailPage(currentThumbnailPage-1);'><a href='#'>&laquo;</a></li>");

		for (i = 1; i <= props.NUM_THUMBNAIL_PAGES; i++) {
			html = html.concat("<li class='thumbnailpaginationitem" + i
					+ "'><a href='#' onclick='return showThumbnailPage(" + i
					+ ");'>" + i + "</a></li>");
		}

		html = html
				.concat("<li class='thumbnailpaginationend'><a href='#'	onclick='return showThumbnailPage(currentThumbnailPage+1);'>&raquo;</a></li></ul>");

		$('#thumbnailpaginationtop').html(html);
		$('#thumbnailpaginationbottom').html(html);
	}

	generatePagination(data);
	// showThumbnailPageImages(1, data);

	return props;
};

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
						+ ");return false;' class='thumbnail'><img src='http://found-dom02.lib.cam.ac.uk"
						+ data.pages[i].thumbnailImageURL + "' ");

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

var currentThumbnailPage = 1;
function showThumbnailPage(pagenum) {
	if (pagenum > 0 && pagenum <= cudl.thumbnailProps.NUM_THUMBNAIL_PAGES) {
		currentThumbnailPage = pagenum;
		showThumbnailPageImages(cudl.thumbnailProps, currentThumbnailPage,
				cudl.data);

		// Update pagination page selected

		$("#thumbnails-content ul.pagination").find("li").removeClass('active');
		$("#thumbnails-content ul.pagination").find(
				".thumbnailpaginationitem" + pagenum).addClass("active");

	}
};

/**
 * ************** End of Thumbmnails
 * ********************************************
 */

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
		cudl.data = data;
		cudl.currentpage = 1;
		cudl.setupSeaDragon(data);
		setupInfoPanel(data);
		cudl.thumbnailProps = setupThumbnails(data);
		store.loadPage(cudl.currentpage);
		console.debug(data);
		showThumbnailPage(currentThumbnailPage)
	});

});
