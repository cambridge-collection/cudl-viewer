/**
 * Script to setup the document view page.
 */
var viewer; // seadragon view
var data;
var store;
var view; // viewport
var currentPage;

function setupSeaDragon() {
	Seadragon.Config.imagePath = "/img/";
	viewer = new Seadragon.Viewer("center");
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
 * Should run only once.
 */
function setupViewport() {

	// Set the title before we make the viewport. 
	viewportComponents.pageTitlePanel.title = '<b>' + data.title
	+ '</b> &nbsp;<i>by ' + data.author + '</i>';
	
	view = new docView();

	// We now have data in the store so we can setup the pageing toolbar.
	var pagingTool = Ext.create('Ext.toolbar.Paging', {
		xtype : 'pagingtoolbar',
		padding : 0,
		width : 250,
		store : store,
		margin : '0 0 0 0',
		border : 0
	});

	// Setup component behaviour
	pagingTool.on('change', view.updateCurrentPage);
	viewportComponents.pagingToolbar.add(pagingTool);
	viewportComponents.tabpanel.on('beforetabchange', view.beforetabchange);
	viewportComponents.tabpanel.on('tabchange', this.aftertabchange);

	// initialise viewport.
	Ext.QuickTips.init();

	var cmp1 = new MyViewport({
		renderTo : Ext.getBody()
	});

	cmp1.show();

}

// This runs when the page has viewport has loaded.
Ext.onReady(function() {

	setupSeaDragon();
	loadData();

});
