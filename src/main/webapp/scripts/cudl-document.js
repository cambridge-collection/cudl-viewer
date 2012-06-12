/**
 * Script to setup the document view page.
 */

cudl.setupSeaDragon = function () {
	Seadragon.Config.imagePath = "/img/";
	Seadragon.Config.debugMode = true;
	Seadragon.Config.maxZoomPixelRatio = 1;
	Seadragon.Strings.Tooltips.Home = "Full Page View";

	cudl.viewer = new Seadragon.Viewer("doc");

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

	// Update the URL if this tab contains external resources
	var urlAttribute = thisTab.urlAttribute;
	if (urlAttribute) {

		var url = encodeURIComponent(cudl.data.pages[cudl.pagenum - 1][urlAttribute + '']);
		if (url=="undefined") {
			url="";
		}
		cachedURL = "/externalresource?url="
				+ encodeURIComponent("/transcription?url=" + url) + "&doc="
				+ cudl.docId;

		thisTab.el.dom.children[0].src = cachedURL;
	}

}

cudl.afterTabShown = function () {

	// Unmask tab if required
	if (cudl.viewportComponents.rightTabPanel.activeTab.el
			&& cudl.viewportComponents.rightTabPanel.activeTab.el.unmask) {

		cudl.viewportComponents.rightTabPanel.activeTab.el.unmask();
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
	var docTitle = '<a href="' + cudl.collectionURL + '">' + cudl.collectionTitle
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
		tooltip : 'Download Image',
		icon : '/img/icon-download-blue.gif',
		handler : cudl.downloadImageCheck
	});

	// Add tabs
	var aboutTab = cudl.setupTab('About', 'metadata',
			cudl.viewportComponents.rightTabPanel);
	cudl.setupTab('Contents', 'logical_structure', cudl.viewportComponents.rightTabPanel);

	if (cudl.data.useTranscriptions) {

		cudl.setupTab('Transcription (normalised)', 'transcription_normal',
				cudl.viewportComponents.rightTabPanel, 'transcriptionNormalisedURL',
				true);

		cudl.setupTab('Transcription (diplomatic)', 'transcription_diplomatic',
				cudl.viewportComponents.rightTabPanel, 'transcriptionDiplomaticURL',
				true);
	}

	cudl.viewportComponents.rightTabPanel.setActiveTab(0);

	// Initialise viewport.
	Ext.QuickTips.init();

	var cmp1 = new cudl.MyViewport({
		renderTo : Ext.getBody()
	});

	cmp1.show();

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
		div.setAttribute("id", element);
		div.setAttribute('width', '100%');
		div.setAttribute('height', '100%');
		document.body.appendChild(div);
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
		iframe.setAttribute('width', '100%');
		iframe.setAttribute('height', '100%');
		iframe.setAttribute('src', '/transcription?url='); // needed for HTML

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
