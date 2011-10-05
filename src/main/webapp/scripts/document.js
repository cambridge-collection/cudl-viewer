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
	Seadragon.Config.debugMode=true;
	Seadragon.Strings.Tooltips.Home="Full Page View";
	
	viewer = new Seadragon.Viewer("doc");	
	
	// FIXME remove the maximize button as it's causing problems
	viewer.getNavControl().removeChild(viewer.getNavControl().childNodes[3]);
	
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
	viewportComponents.pageTitlePanel.items.items[0].text = '<b>' + data.title
	+ '</b> &nbsp;by ' + data.author + '';
	
	view = new docView();

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

	downloadImage = function(answer) {
		if (answer=='yes') {
		 window.open(downloadImageLink);
		} else {
		 return;
		}
	};
	
	downloadImageCheck = function () {
		Ext.Msg.show({
			   title:'Image Licensing',
			   msg: 'This image has the following copyright: <br/>Copyright &copy; Cambridge University Library. Licensed under a Creative Commons Attribution-NonCommercial 3.0 Unported License (CC BY-NC 3.0).<br/><br/> Do you want to download this image?',
			   buttons: Ext.Msg.YESNO,
			   fn: downloadImage
			});
	};
	
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
	viewportComponents.pagingToolbar.add({tooltip:'Download Image', icon: '/img/icon-download-blue.png', handler: downloadImageCheck});
	
	// viewportComponents.tabpanel.on('beforetabchange', view.beforetabchange);
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

	setupSeaDragon();
	loadData();

});


