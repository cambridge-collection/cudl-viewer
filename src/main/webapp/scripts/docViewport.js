/**
 * Setup the components required by the viewport.
 */
var viewportComponents = {

	pageTitlePanel : Ext.create('Ext.panel.Panel', {
		xtype : 'panel',
		height : 28,
		border : false,
		title : '<b>Unknown Title</b> &nbsp;<i>by Unknown</i>'
	}),

	/* Setup the toolbar used to change page */
	pagingToolbar : new Ext.Toolbar({
		xtype : 'toolbar',
		height : 28,
		border : 0,
		padding : 0,
		autoShow : true,
		margin : '0 0 0 0',
		items : [ ]
	}),

	/* Setup the tab panel for the document information */
	tabpanel : Ext.create('Ext.tab.Panel', {
		xtype : 'tabpanel',
		activeTab : 0,
		region : 'east',
		split : true, // enable resizing
		width : '50%',
		title : 'More about the document',
		collapsed : false,
		collapsible : true,
		buttonAlign : 'left',
		bodyPadding : 10,
		margins : 0,
		items : [ {
			xtype : 'panel',
			title : 'About',
			el : 'metadata'
		}, {
			xtype : 'panel',
			title : 'Transcription (normalised)',
			el : 'transcription_normal'
		}, {
			xtype : 'panel',
			title : 'Transcription (diplomatic)',
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
			initComponent : function() {
				this.items = [ {
					layout : 'anchor',
					region : 'center',
					border : false,
					width : 100,
					items : [ viewportComponents.pageTitlePanel, 
					          viewportComponents.pagingToolbar , {
						el : 'center',
						height : '90%',
						border : true
					} ]
				}, viewportComponents.tabpanel, {
					xtype : 'tabpanel',
					activeTab : 0,
					width : 250,
					height : 250,
					region : 'west',
					collapsed : true,
					collapsible : true,
					title : 'Document Structure',
					items : [ {
						xtype : 'panel',
						title : 'Content',
						el : 'logical_structure'
					} ]
				}, {
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
						items : [ '->', // right align spacer
						/*
						 * { xtype : 'button', text : 'Example Menu', menu : {
						 * xtype : 'menu', items : [ { xtype : 'menuitem', text :
						 * 'Report Error' }, { xtype : 'menuitem', text :
						 * 'Additional Items' } ] } },
						 */'Terms & Conditions' ]
					} ]
				} ];

				MyViewportUi.superclass.initComponent.call(this);
			}
		});

MyViewport = Ext.extend(MyViewportUi, {
	initComponent : function() {
		MyViewport.superclass.initComponent.call(this);
	}
});
