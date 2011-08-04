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
    	url: 'sampleJSON', method: 'POST',
        reader: jsonreader
    })
});

store.load();

var beforetabchange = function (panel, newCard) {

	tabpanel.el.mask("Loading ...", "x-mask-loading");
//alert ('change tab: ');
//console.debug(panel);
//console.debug(newCard);
//return true;
};

var aftertabchange = function (panel, newCard) {
	tabpanel.el.unmask();
//alert ('change tab: ');
//console.debug(panel);
//console.debug(newCard);
//return true;
};

updateCurrentPage = function(){
 if (viewer) {
	 console.debug(jsonreader);
  viewer.openDzi(jsonreader.rawData.pages[store.currentPage-1].displayImageURL);

	// setup transcription
	beforetabchange();
 	document.getElementById("transcription_normal_frame").onload= aftertabchange;
	document.getElementById("transcription_normal_frame").src="NewtonTranscriptionViewer?url="+encodeURIComponent(jsonreader.rawData.pages[store.currentPage-1].transcriptionNormalisedURL);
	document.getElementById("transcription_diplomatic_frame").src="NewtonTranscriptionViewer?url="+encodeURIComponent(jsonreader.rawData.pages[store.currentPage-1].transcriptionDiplomaticURL);

	// setup metadata
	document.getElementById("metadata-title").innerHTML=jsonreader.rawData.title;
	document.getElementById("metadata-author").innerHTML=jsonreader.rawData.author;
	document.getElementById("metadata-rights").innerHTML=jsonreader.rawData.rights;
	document.getElementById("metadata-page").innerHTML=jsonreader.rawData.pages[store.currentPage-1].name;
	document.getElementById("metadata-subject").innerHTML=jsonreader.rawData.subject;
	document.getElementById("metadata-physicalLocation").innerHTML=jsonreader.rawData.physicalLocation;
	document.getElementById("metadata-shelfLocation").innerHTML=jsonreader.rawData.shelfLocator;
	document.getElementById("metadata-dateCreatedDisplay").innerHTML=jsonreader.rawData.dateCreatedDisplay;
	
 }
};

var pagingToolbar = Ext.create('Ext.toolbar.Paging',{
		xtype : 'pagingtoolbar',
		store : store,
		padding :0,
		width : 250,
		margin  : '0 0 0 0',
	        border: 0
	});

pagingToolbar.on('change', updateCurrentPage);

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


MyViewportUi = Ext.extend(Ext.Viewport, {
	layout : 'border',
	initComponent : function() {
		this.items = [ {
			layout : 'anchor',
			region : 'center',
  		        border: false,			
			width : 100,
			items : [{
				xtype : 'panel',
				height : 28,
  		        	border: false,			
				title:'<b>'+jsonreader.rawData.title+'</b> &nbsp;<i>by '+jsonreader.rawData.author+'</i>'
			},{
				xtype : 'toolbar',
				height : 28,
				border: 0,
				padding :0,
				autoShow: true,
				margin  : '0 0 0 0',
				items : [  
					{
					xtype : 'button',
					height : 28,
  			        	border: false,			
					text:'<< Collection',
					handler: function() {
					        history.go(-1);
					    }
					},
					{xtype: 'tbspacer', width: 40},
					pagingToolbar				
				]
			},{
			    el: 'center',
			    height: '90%',
			    border: true
			}
			]
		}, tabpanel,/* {
			xtype : 'tabpanel',
			activeTab : 0,
			width : 250,
			height : 250,
			region : 'west',
			collapsed : true,
			collapsible : true,
			title : 'Search',
			items : [ {
				xtype : 'panel',
				title : 'Search'
			}, {
				xtype : 'panel',
				title : 'Advanced'
			}, {
				xtype : 'panel',
				title : 'Bookmarks'
			} ]
		},*/ {
			xtype : 'container',
			region : 'north',
			el : 'north',
			height : 52
		}, {
			xtype : 'container',
			region : 'south',
			height : 28,
			items : [ {
				xtype : 'toolbar',
				height : 28,
				items : [ 
				'->', // right align spacer
				/*{
					xtype : 'button',
					text : 'Example Menu',
					menu : {
						xtype : 'menu',
						items : [ {
							xtype : 'menuitem',
							text : 'Report Error'
						}, {
							xtype : 'menuitem',
							text : 'Additional Items'
						} ]
					}
				},*/ 'Terms & Conditions']
			} ]
		} ];

		MyViewportUi.superclass.initComponent.call(this);
	}
});