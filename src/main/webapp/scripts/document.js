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
cudl.currentpage = 0; // stores current page viewed.

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
	openDzi(cudl.data.pages[pagenumber].displayImageURL);

	// update metadata

	// update current page
	cudl.currentpage = pagenumber;
	return false;

};

cudl.setupSeaDragon = function(data) {

	OpenSeadragon.setString("Tooltips.Home", "Reset View");
	cudl.viewer = new OpenSeadragon.Viewer({
		id : "doc",
		debugMode : false,
		prefixUrl : "/img/",
		showRotationControl : true,
		maxZoomPixelRatio : 1,
		navigationControlAnchor : OpenSeadragon.ControlAnchor.BOTTOM_LEFT
	});

	// Setup forward and backward buttons
	function fullscreenNextPage() {

		if (cudl.currentpage < cudl.data.pages.length - 1) {
			cudl.currentpage++;
			store.loadPage(cudl.currentpage);
		}

	}

	function fullscreenPrevPage() {

		if (cudl.currentpage >= 1) {
			cudl.currentpage--;
			store.loadPage(cudl.currentpage);
		}
	}

	var buttonNextPage = new OpenSeadragon.Button({
		tooltip : "Next Page",
		srcRest : "/img/nextPage_rest.png",
		srcGroup : "/img/nextPage_grouphover.png",
		srcHover : "/img/nextPage_hover.png",
		srcDown : "/img/nextPage_pressed.png",
		onClick : fullscreenNextPage
	});

	var buttonPrevPage = new OpenSeadragon.Button({
		tooltip : "Previous Page",
		srcRest : "/img/prevPage_rest.png",
		srcGroup : "/img/prevPage_grouphover.png",
		srcHover : "/img/prevPage_hover.png",
		srcDown : "/img/prevPage_pressed.png",
		onClick : fullscreenPrevPage
	});

	var navBar = new OpenSeadragon.ButtonGroup({
		buttons : [ buttonPrevPage, buttonNextPage ],
		config : {}
	});

	cudl.viewer.addControl(navBar.element, {
		anchor : OpenSeadragon.ControlAnchor.TOP_LEFT
	});

};

function setupInfoPanel(data) {

	breadcrumbHTML = "<ol class=\"breadcrumb\"><li><a href='/'>Home</a></li><li><a href=\""
			+ cudl.collectionURL + "\">" + cudl.collectionTitle
			+ "</a></li></ol>";
	if (cudl.parentCollectionTitle) {
		breadcrumbHTML = "<ol class=\"breadcrumb\"><li><a href='/'>Home</a></li><li><a href=\""
				+ cudl.parentCollectionURL + "\">" + cudl.parentCollectionTitle
				+ "</a></li><li><a href=\"" + cudl.collectionURL + "\">"
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
						+ (array[i].startPagePosition - 1)
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
			$('#right-panel').css({'right':'0px'});
			infoPanelExpanded = false;
			$('#doc').width('60%');
		} else {
			$('#right-panel').css({'right':($('#right-panel').width()*-1)});
			infoPanelExpanded = true;
			$('#doc').width('100%');
		}
	});

};

function setupThumbnails(data) {

	var thumbnailhtml = "<div class='row'>";
	for (i = 0; i < data.pages.length; i++) {

		thumbnailhtml = thumbnailhtml
				.concat("<div class='col-md-6 col-md-3'><a href='' onclick='store.loadPage("
						+ (data.pages[i].sequence - 1)
						+ ");return false;' class='thumbnail'><img src='http://found-dom02.lib.cam.ac.uk"
						+ data.pages[i].thumbnailImageURL
						+ "' style='max-width:150px; max-height:130px;'></a></div>");

	}
	console.debug($('#thumbnailimages').html);
	$('#thumbnailimages').html(thumbnailhtml + "</div>");

};

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
		cudl.currentpage = 0;
		cudl.setupSeaDragon(data);
		setupInfoPanel(data);
		setupThumbnails(data);
		store.loadPage(cudl.currentpage);
		console.debug(data);

	});

});
