/**
 * Script to setup the document view page.
 */
var viewer; // seadragon view
var data;
var store;
var view; // viewport
var currentPage;
var currentTab = 0;

function setupSeaDragon() {
	Seadragon.Config.imagePath = "/img/";
	Seadragon.Config.debugMode=true;
	Seadragon.Strings.Tooltips.Home="Full Page View";
	
	viewer = new Seadragon.Viewer("doc");	
	//console.debug(viewer);
	// FIXME remove the maximize button as it's causing problems
	//viewer.getNavControl().removeChild(viewer.getNavControl().childNodes[3]);
	
};

function loadData() {

	var setupDone = false;
	var docDataLoader = new docData();
	docDataLoader.init();
	store = docDataLoader.store;

	docDataLoader.store.on('load', function(store, records, successful,
			operation, eOpts) {

		if (!setupDone) {
			docDataLoader.dataLoaded = true;
			data = docDataLoader.getData();

			// placing it here means the viewport should only be initalised once
			// the
			// data is loaded.

			setupViewport();
			setupDone = true;
		}
	});

	docDataLoader.load(); // loads in the JSON
}

/**
 * Load content into the current tab that is being viewed. 
 * 
 * @param tabs
 * @param thisTab
 */
function beforeTranscriptionTabChange(tabs, thisTab) {

	for (var i=0; i<tabTitles.length; i++) {
		if (tabTitles[i]==thisTab.title) {
			currentTab=i;
		};
	}

	if (currentTab==1) {
		  document.getElementById("transcription_normal_frame").src = transcriptionNormalisedURL;
		  document.getElementById("transcription_normal_frame").style.display="inline";
	} else 
	if (currentTab==2) {
		  document.getElementById("transcription_diplomatic_frame").src = transcriptionDiplomaticURL;
		  document.getElementById("transcription_diplomatic_frame").style.display="inline";
	}
}

/**
 * Should run only once.
 */
function setupViewport() {
	
	view = new docView();
	
	// Find the ROOT descriptiveMetadata object. 
	var descriptiveMetadataID = data.logicalStructure[0].descriptiveMetadataID;
	var descriptiveMetadata = view.findDescriptiveMetadata(descriptiveMetadataID, data);
	
	// set title
	viewportComponents.pageTitlePanel.items.items[0].text = '<b>' + descriptiveMetadata.title
	+ '</b> &nbsp;by ' + descriptiveMetadata.author + '';

	// We now have data in the store so we can setup the pageing toolbar.
	var pagingTool = Ext.create('Ext.toolbar.Paging', {
		xtype : 'pagingtoolbar',
		padding : 0,
		width : 275,
		inputItemWidth:40,
		beforePageText:'Image',
		store : store,
		margin : '0 0 0 0',
		border : 0
	});

	
	bookmarkPage = function(title, url) {

		Ext.Msg.show({
			   title:'Bookmark Page',
			   msg: 'The URL to bookmark for direct access to this page is: <br/>'+url,
			   buttons: Ext.Msg.OK,
			   fn: function() {return;}
			});

	};
	// Setup component behaviour
	pagingTool.on('change', view.updateCurrentPage);
	viewportComponents.pagingToolbar.add(pagingTool);
	viewportComponents.pagingToolbar.add('Page: <span id="metadata-page-toolbar">&nbsp;</span>');
	viewportComponents.pagingToolbar.add('->');
	viewportComponents.pagingToolbar.add({tooltip:'Download Image', icon: '/img/icon-download-blue.gif', handler: downloadImageCheck});
	
	viewportComponents.rightTabPanel.on('beforetabchange', beforeTranscriptionTabChange);
	//viewportComponents.leftTabPanel.on('beforetabchange', beforeContentsTabChange);
	// viewportComponents.tabpanel.on('tabchange', this.aftertabchange);

	// initialise viewport.
	Ext.QuickTips.init();

	var cmp1 = new MyViewport({
		renderTo : Ext.getBody()
	});

	cmp1.show();

}

// This runs when the page has viewport has loaded.
Ext.onReady(function() {

	loadData();
	setupSeaDragon();
	

});


