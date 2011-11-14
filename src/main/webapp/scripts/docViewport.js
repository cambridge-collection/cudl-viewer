/**
 * Setup the components required by the viewport.
 */
var tabTitles = ["About", "Transcription (normalised)", "Transcription (diplomatic)"];
var viewportComponents = {

	pageTitlePanel: new Ext.Toolbar({
		xtype : 'toolbar',
		height : 26,
		margin : '0 0 0 0',					
		border : 0,
		padding : 0,
		items:[ '<b>Unknown Title</b> &nbsp;by Unknown' ]
	}),

	/* Setup the toolbar used to change page */
	pagingToolbar : new Ext.Toolbar({
		xtype : 'toolbar',
		height : 26,
		border : 0,
		padding : 0,
		autoShow : true,
		margin : '0 0 0 0',
		items : [  ]
	}),

	/* Setup the tab panel for the document information */
	tabpanel : Ext.create('Ext.tab.Panel', {
		xtype : 'tabpanel',
		activeTab : 0,
		region : 'east',
		split : true, // enable resizing
		width : '50%',
		title : 'More about this item',
		collapsed : false,
		collapsible : true,
		bodyPadding : 0,
		items : [ {
			xtype : 'panel',
			title : tabTitles[0],			
			el : 'metadata'				
		}, {
			xtype : 'panel',
			title : tabTitles[1],
			el : 'transcription_normal'
		}, {
			xtype : 'panel',
			title : tabTitles[2],
			el : 'transcription_diplomatic'
		} ]
	})

};

/**
 * Setup the viewport itself
 */
MyViewportUi = Ext.extend(Ext.Viewport,
		{
			layout : 'border',
			margin : '0 0 0 0',					
			border : 0,
			padding : 0,			
			initComponent : function() {
				this.items = [ {
					layout : 'anchor',
					region : 'center',
					border : false,
					margin : '0 0 0 0',					
					border : 0,
					padding : 0,
					width : 100,
					items : [ viewportComponents.pageTitlePanel,
					          viewportComponents.pagingToolbar , {
						el : 'doc',
						height : '90%',
						border : true
					} ]
				}, viewportComponents.tabpanel, {
					xtype : 'tabpanel',
					activeTab : 0,
					width : 250,
					height : 250,
					defaults:{ autoScroll:true },
					region : 'west',
					collapsed : true,
					collapsible : true,
					title : 'Contents',
					split:true,
					items : [ {
						xtype : 'panel',
						title : 'Sections',
						autoScroll:true,
						el : 'logical_structure'
					} ]
				}, {
					xtype : 'container',
					region : 'north',
					el : 'north',
					height : 81
				}/*, {
					xtype : 'container',
					region : 'south',
					height : 28,
					items : [ {
						xtype : 'toolbar',
						height : 28,
						items : [ '->', // right align spacer
						]
					} ]
				} */];

				MyViewportUi.superclass.initComponent.call(this);
			}
		});

MyViewport = Ext.extend(MyViewportUi, {
	initComponent : function() {
		MyViewport.superclass.initComponent.call(this);
	}
});
