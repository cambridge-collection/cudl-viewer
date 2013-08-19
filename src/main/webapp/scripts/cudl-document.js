/**
 * Script to setup the document view page.
 */

cudl.setupSeaDragon = function () {
	Seadragon.Config.imagePath = "/img/";
	Seadragon.Config.debugMode = true;
	Seadragon.Config.maxZoomPixelRatio = 1;
	Seadragon.Strings.Tooltips.Home = "Reset View";

	cudl.viewer = new Seadragon.Viewer("doc");
	/*		
	cudl.viewer = new OpenSeadragon.Viewer({
		id:"doc",
        debugMode: true,
        prefixUrl: "/img/",
        maxZoomPixelRatio: 1
    });
*/

	// Setup forward and backward buttons
	function fullscreenNextPage() {
		if (cudl.pagenum + 1 <= cudl.store.totalCount) {
			cudl.store.loadPage(cudl.pagenum + 1);
			cudl.view.pageSet = true;
		}
	}

	function fullscreenPrevPage() {
		if (cudl.pagenum - 1 > 0) {
			cudl.store.loadPage(cudl.pagenum - 1);
			cudl.view.pageSet = true;
		}
	}

	var buttonNextPage = new Seadragon.Button("Next Page",
			"/img/nextPage_rest.png", "/img/nextPage_grouphover.png",
			"/img/nextPage_hover.png", "/img/nextPage_pressed.png", null, // do
																			// nothing
																			// on
																			// initialpress
			fullscreenNextPage, // go home on release
			null, // no need to use clickthresholds
			null, // do nothing on enter
			null // do nothing on exit
	);

	var buttonPrevPage = new Seadragon.Button("Previous Page",
			"/img/prevPage_rest.png", "/img/prevPage_grouphover.png",
			"/img/prevPage_hover.png", "/img/prevPage_pressed.png", null, // do
																			// nothing
																			// on
																			// initialpress
			fullscreenPrevPage, // go home on release
			null, // no need to use clickthresholds
			null, // do nothing on enter
			null // do nothing on exit
	);

	var navBar = new Seadragon.ButtonGroup([ buttonPrevPage, buttonNextPage ]);

	cudl.viewer.addControl(navBar.elmt, Seadragon.ControlAnchor.BOTTOM_LEFT);

};

cudl.loadData = function() {

	var setupDone = false;
	var docDataLoader = new cudl.docData();
	docDataLoader.init();
	cudl.store = docDataLoader.store;

	docDataLoader.store.on('load', function(store, records, successful,
			operation, eOpts) {

		if (!setupDone) {
			docDataLoader.dataLoaded = true;
			cudl.data = docDataLoader.getData();

			// placing it here means the viewport should only be initalised once
			// the data is loaded.

			cudl.setupViewport();
			setupDone = true;

		}
	});

	docDataLoader.load(); // loads in the JSON
}

/**
 * Load content into the current tab that is being viewed. Uses data as setting
 * SRC depends on currently viewed page.
 * 
 * @param tabs
 * @param thisTab
 */
cudl.beforeTabShown = function(thisTab) {

	// mask this tab if required
	if (thisTab.displayLoadingMask) {
		thisTab.el.mask("Loading...", "x-mask-loading");
	}
	
	// show tab if hidden
	if (thisTab.el.dom.style.display=='none') {
		thisTab.el.dom.style.display='inline';
	}

	// Update the URL if this tab contains external resources
	var urlAttribute = thisTab.urlAttribute;
	if (urlAttribute) {

		var url = encodeURIComponent(cudl.data.pages[cudl.pagenum - 1][urlAttribute + '']);
		if (url=="undefined") {
			url="";
		}
		//cachedURL = "/externalresource?url="
		//		+ encodeURIComponent("/transcription?url=" + url) + "&doc="
		//		+ cudl.docId;
		cachedURL = "/transcription?url=" + url +"&doc="	+ cudl.docId;
			
		thisTab.el.dom.children[0].src = cachedURL;
	}
}

cudl.afterTabShown = function () {

    var activeTab = cudl.viewportComponents.rightTabPanel.activeTab.el;
	
	// Horrible Hack for Chrome bug Issue 236043: iframe in a div has incorrect height
	// https://code.google.com/p/chromium/issues/detail?id=236043	
	if (activeTab.id == "transcription_normal") {
		document.getElementById("transcription_normal_frame").style.height = activeTab.dom.style.height;	
	} else 
	if (activeTab.id == "transcription_diplomatic") {
		document.getElementById("transcription_diplomatic_frame").style.height = activeTab.dom.style.height;	
	}	
	// end of hack. 
	
	// Unmask tab if required
	if (activeTab && activeTab.unmask) {
		
		activeTab.unmask();
	}
	
}

/**
 * Should run only once.
 */
cudl.setupViewport = function () {

	cudl.view = new cudl.docView();

	// Find the ROOT descriptiveMetadata object.
	var descriptiveMetadataID = cudl.data.logicalStructures[0].descriptiveMetadataID;
	var descriptiveMetadata = cudl.view.findDescriptiveMetadata(
			descriptiveMetadataID, cudl.data);

	// set title
	// limit length here, as display width is limited.
	var tbTitle = cudl.itemTitle;
	var titleLimit = 35;
	if (tbTitle.length > titleLimit) {
		tbTitle = tbTitle.substring(0, titleLimit) + "...";
	}
	var docTitle = '';
	if (cudl.parentCollectionTitle && cudl.parentCollectionURL) {
		docTitle += '<a href="' + cudl.parentCollectionURL + '">'+ cudl.parentCollectionTitle+ '</a> > '; 
	}
	docTitle += '<a href="' + cudl.collectionURL + '">' + cudl.collectionTitle
			+ '</a> > <b>' + tbTitle + '</b>';


	tbAuthor = cudl.itemAuthors;
	
	// limit length here, as display width is limited.
	if (tbAuthor.length > 0) {
		var authorLimit = 20;
		tbAuthor = tbAuthor + ""; // converts to String
		if (tbAuthor.length > authorLimit) {
			tbAuthor = tbAuthor.substring(0, authorLimit) + "...";
		}
		docTitle += ' &nbsp;by ' + tbAuthor;
	}

	cudl.viewportComponents.pageTitlePanel.items.items[0].text = docTitle;

	// We now have data in the store so we can setup the pageing toolbar.
	var pagingTool = Ext.create('Ext.toolbar.Paging', {
		xtype : 'pagingtoolbar',
		padding : 0,
		width : 275,
		inputItemWidth : 40,
		beforePageText : 'Image',
		store : cudl.store,
		margin : '0 0 0 0',
		border : 0
	});

	// Setup component behaviour
	pagingTool.on('change', cudl.view.updateCurrentPage);
	cudl.viewportComponents.pagingToolbar.add(pagingTool);
	cudl.viewportComponents.pagingToolbar
			.add('Page: <span id="metadata-pagenum-toolbar">&nbsp;</span>');
	cudl.viewportComponents.pagingToolbar.add('->');
	cudl.viewportComponents.pagingToolbar.add({
		tooltip : 'Page Link',
		icon : '/img/icon-link.png',		
		handler : cudl.linkImageCheck
	});		
	cudl.viewportComponents.pagingToolbar.add({
		tooltip : 'Bookmark Image',
		icon : '/img/icon-bookmark-blue.png',
		handler : cudl.bookmarkImageCheck
	});	
	cudl.viewportComponents.pagingToolbar.add({
		tooltip : 'Download Image',
		icon : '/img/icon-download-blue.gif',
		handler : cudl.downloadImageCheck
	});

	// Add tabs
	var aboutTab = cudl.setupTab('About', 'metadata',
			cudl.viewportComponents.rightTabPanel);
	
	// Show Contents tab only if there are any children defined 
	if (cudl.data.logicalStructures[0].children) {
	   cudl.setupTab('Contents', 'logical_structure', cudl.viewportComponents.rightTabPanel);
	}
	
	// Setup diplomatic transcription tab
	if (cudl.data.useDiplomaticTranscriptions) {

	    // if this attribute is present, the transcription does not change on page change.  
		if (cudl.data.allTranscriptionDiplomaticURL) {
			
			cudl.setupTab('Transcription (diplomatic)', 'transcription_diplomatic',
					cudl.viewportComponents.rightTabPanel);
			
			// Create iframe and set target to be the transcription URL. 
			var iframe = document.createElement("iframe");
			iframe.setAttribute('id', 'transcription_diplomatic_frame');
			iframe.setAttribute('src', cudl.data.allTranscriptionDiplomaticURL); 
            document.getElementById('transcription_diplomatic').appendChild(iframe);
					
			
		} else {

			// This tab changes src when the page changes. 
			cudl.setupTab('Transcription (diplomatic)', 'transcription_diplomatic',
					cudl.viewportComponents.rightTabPanel, 'transcriptionDiplomaticURL',
					true);
		}
	}		
	
	// Setup normalised transcription tab	
	if (cudl.data.useNormalisedTranscriptions) {
	
	    // if this attribute is present, the transcription does not change on page change.  
		if (cudl.data.allTranscriptionNormalisedURL) {
			
			cudl.setupTab('Transcription (normalised)', 'transcription_normal',
					cudl.viewportComponents.rightTabPanel);
			
			// Create iframe and set target to be the transcription URL. 
			var iframe = document.createElement("iframe");
			iframe.setAttribute('id', 'transcription_normal_frame');
			iframe.setAttribute('src', cudl.data.allTranscriptionNormalisedURL); 
            document.getElementById('transcription_normal').appendChild(iframe);
								
		} else {
		
		  cudl.setupTab('Transcription (normalised)', 'transcription_normal',
				cudl.viewportComponents.rightTabPanel, 'transcriptionNormalisedURL',
				true);
		}
	}

	// Setup Thumbnail Tab.
	var thumbnailTab = cudl.setupTab('Thumbnails', 'thumbnailTab', cudl.viewportComponents.rightTabPanel);	
	
	Ext.Loader.setConfig({enabled: true});

	Ext.Loader.setPath('Ext.ux.DataView', '/scripts/extjs/ux/DataView/');
	
	// Setup Thumbnails
	Ext.require([
	             'Ext.data.*',
	             'Ext.util.*',
	             'Ext.view.View',
	             'Ext.ux.DataView.DragSelector',
	             'Ext.ux.DataView.LabelEditor'
	         ]);

	cudl.thumbnailsSetup = false;
	cudl.setupThumbnailTab = function() { 
		
	  if (!cudl.thumbnailsSetup) {
		  
		var setNumberItems = function () {
			
		  var pageHeaderHeight = 120;
		  var thumbnailItemHeight = 175;
		  var thumbnailItemWidth = 130;
			
		  var tabWidth = parseInt(document.getElementById("rightTabPanel").style.width);
		  var tabHeight = parseInt(document.getElementById("rightTabPanel").style.height)-pageHeaderHeight; 
		  var numItemInRow =  Math.floor(tabWidth/thumbnailItemWidth);
		  var numRows =  Math.floor(tabHeight/thumbnailItemHeight);
      			  		  
		  Ext.data.StoreManager.lookup('thumbStore').pageSize=numItemInRow*numRows;

		};
		
		setNumberItems();
		
	 	var thumbnailPanel =  new Ext.Panel({
		    id: 'thumbnailPanel',
		    layout: 'absolute',
		    x: 0, 
		    y: 0,
			border : 0, 	 		    
 			padding : '0 0 0 0',
		    html: "<div id=\"images-view\"><div id=\"thumbnails\"></div></div>",
		    items: [new Ext.Toolbar({
	 			xtype : 'toolbar',
	 			id : 'thumbnailToolbar',
	 			height : 26,
				width: '100%', 			
	 			padding : 0,				
				border : 0, 	 			
	 			autoShow : true,
	 			margin : '5 5 5 5', 
	 			items: [ Ext.create('Ext.toolbar.Paging', {
					xtype : 'pagingtoolbar',
					padding : 0,
					inputItemWidth : 40,
					width: 450,
					beforePageText : 'Page',
					store : Ext.data.StoreManager.lookup('thumbStore'),
					margin : '0 0 0 0',
					border : 0, 
					displayInfo: true, 
					listeners: {
						beforeChange: function(tb, params) { setNumberItems() }
					}
	 			})]	    
	 	 	   })]
	 	 	 });		
		  
	 	// Note height=150px for portrait, width=120px for landscape. 
		var imageTpl = new Ext.XTemplate(
			    '<tpl for=".">',
			        '<div class="thumb-wrap">',
			          '<img src="/imageproxy{downloadImageURL}" height="150px"/>',
			          '<span>{label}</span>',
			        '</div>',
			    '</tpl>'
			);
		 
		  thumbnailTab.add( thumbnailPanel);
			
		  var thumbnailView = Ext.create('Ext.view.View', {
		    store: Ext.data.StoreManager.lookup('thumbStore'),
		    tpl: imageTpl,
		    itemSelector: 'div.thumb-wrap',
		    emptyText: 'No images available',
		    renderTo: 'thumbnails',
	        listeners: {
	                selectionchange: function(dv, nodes ){
	   
	                   if (nodes.length>0) {

	                     cudl.pagenum = nodes[0].data.sequence; 
	                     cudl.store.loadPage(cudl.pagenum);
	
	                   }
	                }
	            }
		  });
			  
		  cudl.thumbnailsSetup = true;
		 
		}
	
	}
	
	thumbnailTab.addListener('beforeshow', cudl.setupThumbnailTab);
	 
	cudl.viewportComponents.rightTabPanel.setActiveTab(0);
	
	// Initialise viewport.

	Ext.QuickTips.init();

	var cmp1 = new cudl.MyViewport({
		renderTo : Ext.getBody()
	});

	cmp1.show();
	
	// setup logical structure

	cudl.buildLogicalStructures = function(logicalStructureElement, level) {
		
		var ls = "";

		for ( var i = 0; i < logicalStructureElement.length; i++) {
			var lsItem = logicalStructureElement[i];
			for ( var j = 0; j < level; j++) {
				ls += "<li><ul>";
			}
			ls += "<li><a href='' onclick='cudl.store.loadPage("
					+ lsItem.startPagePosition + ");return false;'>"
					+ lsItem.label + "</a> (image "
					+ lsItem.startPagePosition + ", page "
					+ lsItem.startPageLabel + ")</li>";
			for ( var j = 0; j < level; j++) {
				ls += "</ul></li> ";
			}

			if (lsItem.children && lsItem.children.length > 0) {
				ls += cudl.buildLogicalStructures(lsItem.children,
						level + 1);
			}
		}

		return ls;
	}	

	
	if (document.getElementById("logical_structure")) {
		
		var ls;
		if (cudl.data.logicalStructures[0].children) {

			ls = cudl.buildLogicalStructures(
					cudl.data.logicalStructures[0].children, 0);
		} 

		var lsDiv = document.getElementById("logical_structure");
		
		lsDiv.innerHTML="<div style='height: 100%; overflow-y:auto;'><ul>"
						+ ls + "</ul></div>";

	}

}

/**
 * Note that the URL is only set when the tab is shown, not when it is created.
 * 
 * @param title
 * @param element -
 *            specifies the element that contains the content for this tab. Will
 *            be created if it does not exist already.
 * @param parent
 *            the TabPanel object which this tab will be added to.
 * @param urlAttribute -
 *            page level attribute specifying URL.
 * @param displayLoadingMask -
 *            boolean, only works with urlAttribute (when it's an external
 *            resource tab)
 */
cudl.setupTab = function(title, element, parent, urlAttribute, displayLoadingMask) {

	if (!document.getElementById(element)) {
		var div = document.createElement("div");
		div.style.display='none';
		div.setAttribute("id", element);	
		document.body.appendChild(div);
	} else {
		document.getElementById(element).style.display='none';
	}

	var tab = Ext.create('Ext.panel.Panel', {
		xtype : 'panel',
		title : title,
		el : element
	});

	// This assumes we are loading in content from an external url
	// specified in an attribute from the JSON.
	// This content is displayed in an iframe we create.
	if (urlAttribute) {

		// Add iframe for content. Src will be set when content is loaded.
		var iframe = document.createElement("iframe");
		iframe.setAttribute('id', element + '_frame');
		iframe.setAttribute('onload', 'cudl.afterTabShown()');
		iframe.setAttribute('src', '/transcription?url=&doc='); // needed for validation of HTML

		// validation.
		div.appendChild(iframe);
		document.body.appendChild(div);

		tab.urlAttribute = urlAttribute;
		tab.displayLoadingMask = displayLoadingMask;
		tab.addListener('beforeshow', cudl.beforeTabShown);

	}

	parent.add(tab);
	return tab;
}

var store;

// This runs when the page has viewport has loaded.
Ext.onReady(function() {

	// Display Loading mask. 
	cudl.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"Please wait..."});
	cudl.loadMask.show();
	
	cudl.loadData();
	cudl.setupSeaDragon();
	
	// required for abstract
	store = cudl.store; 

});
