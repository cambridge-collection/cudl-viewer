/**
 * Script to configure the ext viewport and data for the document view. 
 */

MyViewport = Ext.extend(MyViewportUi, {
    initComponent: function() {
        MyViewport.superclass.initComponent.call(this);
    }
});

Ext.onReady(function() {
    Ext.QuickTips.init();
    var cmp1 = new MyViewport({
        renderTo: Ext.getBody()
    });
    cmp1.show();
});

/* read json that defines the data for this page */

var jsonreader = new Ext.data.JsonReader({
	root:'rows', totalProperty: 'numberOfPages'
	}, [ 'name', 'displayImageURL', 'downloadImageURL', 'transcriptionNormalisedURL', 'transcriptionDiplomaticURL' ]      	    
);

var store = Ext.create('Ext.data.Store', {
	id:'pageStore',
	autoLoad: {start: 0, limit: 3},
	fields:[ 'name', 'displayImageURL', 'downloadImageURL', 'transcriptionNormalisedURL', 'transcriptionDiplomaticURL' ],
	pageSize: 1, // items per page
	proxy: new Ext.data.HttpProxy({
		url: '/3958.json', method: 'POST',
		reader: jsonreader
	})
});
store.load();

/* While a tab is loading it is masked. */

var beforetabchange = function (panel, newCard) {
	tabpanel.el.mask("Loading ...", "x-mask-loading");
};

var aftertabchange = function (panel, newCard) {
	tabpanel.el.unmask();
};

/* On a page turn the data, images etc for that page need to be displayed. */
updateCurrentPage = function(){
	if (viewer && jsonreader.rawData) {
		console.debug(jsonreader);
		viewer.openDzi(jsonreader.rawData.pages[store.currentPage-1].displayImageURL);

		// setup transcription
		beforetabchange();

		if (jsonreader.rawData.pages[store.currentPage-1].transcriptionNormalisedURL &&
			jsonreader.rawData.pages[store.currentPage-1].transcriptionDiplomaticURL) { 	
			document.getElementById("transcription_normal_frame").onload= aftertabchange;
			document.getElementById("transcription_normal_frame").src="/transcription?url="+encodeURIComponent(jsonreader.rawData.pages[store.currentPage-1].transcriptionNormalisedURL);		
			document.getElementById("transcription_diplomatic_frame").src="/transcription?url="+encodeURIComponent(jsonreader.rawData.pages[store.currentPage-1].transcriptionDiplomaticURL);
		} else {	
			// there is no transcription to load, unmask the tab. 
			tabpanel.el.unmask(); 
		}

		// setup metadata
		document.getElementById("metadata-title").innerHTML=jsonreader.rawData.title;
		document.getElementById("metadata-author").innerHTML=jsonreader.rawData.author;
		document.getElementById("metadata-rights").innerHTML=jsonreader.rawData.rights;
		document.getElementById("metadata-page").innerHTML=jsonreader.rawData.pages[store.currentPage-1].name;
		document.getElementById("metadata-subject").innerHTML=jsonreader.rawData.subject;
		document.getElementById("metadata-physicalLocation").innerHTML=jsonreader.rawData.physicalLocation;
		document.getElementById("metadata-shelfLocation").innerHTML=jsonreader.rawData.shelfLocator;
		document.getElementById("metadata-dateCreatedDisplay").innerHTML=jsonreader.rawData.dateCreatedDisplay;


		// setup logitcal structure
		var ls = "";
		for (var i=0; i<jsonreader.rawData.logicalStructure.length; i++) {
			var lsItem = jsonreader.rawData.logicalStructure[i];
			ls += "<li><a href=''>"+lsItem.title+"</a> [page:"+lsItem.startPagePosition+"]</li>";
		}
		document.getElementById("logical_structure").innerHTML="<ul>"+ls+"</ul>";

	}
};

/* Setup the toolbar used to change page */
var pagingToolbar = Ext.create('Ext.toolbar.Paging',{
	xtype : 'pagingtoolbar',
	store : store,
	padding :0,
	width : 250,
	margin  : '0 0 0 0',
    border: 0
});

pagingToolbar.on('change', updateCurrentPage);

/* Setup the tab panel for the document information */
var tabpanel =  Ext.create('Ext.tab.Panel',{
	xtype : 'tabpanel',
	activeTab : 0,
	region : 'east',
	split : true, // enable resizing
	width : '50%',
	title : 'More about the document',
	collapsed : false,
	collapsible : true,
	buttonAlign : 'left',
	bodyPadding: 10,
	margins : 0,
	items : [ {
		xtype : 'panel',
		title : 'About',
		el : 'metadata'
	},{
		xtype : 'panel',
		title : 'Transcription (normalised)',
		el : 'transcription_normal'
	}, {
		xtype : 'panel',
		title : 'Transcription (diplomatic)',
		el: 'transcription_diplomatic'
	}]
});


tabpanel.on('beforetabchange', beforetabchange);
tabpanel.on('tabchange', aftertabchange);
