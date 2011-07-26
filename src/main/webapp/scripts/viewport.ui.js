var jsonreader = new Ext.data.JsonReader({
        	root:'rows', totalProperty: 'results'
        	}, [ 'name', 'imageURL' ]        	    
        );

var store = Ext.create('Ext.data.Store', {
    id:'pageStore',
    autoLoad: {start: 0, limit: 3},
    fields:[ 'name', 'imageURL' ],
    pageSize: 1, // items per page
    proxy: new Ext.data.HttpProxy({
    	url: 'JSONImageURLGenerator?doc=MSADD-03958-001', method: 'POST',
        reader: jsonreader
    })
});

store.load();

updateCurrentPage = function(){
 if (viewer) {
  viewer.openDzi(jsonreader.rawData.images[store.currentPage-1].imageURL);
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
				title : 'Transcription (normalised)',
				el : 'transcription_normal'
			}, {
				xtype : 'panel',
				title : 'Translation (diplomatic)',
				el: 'transcription_diplomatic'
			}, {
				xtype : 'panel',
				title : 'Metadata'
			}]
		});

var beforetabchange = function (panel, newCard) {
//alert ('change tab: ');
//console.debug(panel);
//console.debug(newCard);
//return true;
};

tabpanel.on('beforetabchange', beforetabchange);


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
				title:'<b>Extract from Untitled Treatise on Revelation</b> &nbsp;<i>by Isaac Newton</i>'
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
					text:'<< Back',
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
