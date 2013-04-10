/**
 * Setup the components required by the viewport.
 */

cudl.viewportComponents = {
	
	pageTitlePanel: new Ext.Toolbar({
		xtype : 'toolbar',
		height : 26,
		margin : '0 0 0 0',					
		border : 0,
		padding : 0,
		items:[ '<b>Unknown Title</b> &nbsp;by Unknown' ]
	   }),
		
	getPageTitlePanel: function() {
		return this.pageTitlePanel;
	},

	/* Setup the toolbar used to change page */
	pagingToolbar: new Ext.Toolbar({
		xtype : 'toolbar',
		height : 26,
		border : 0,
		padding : 0,
		autoShow : true,
		margin : '0 0 0 0',
		items : [  ]
 	    }), 
	

	getPagingToolbar : function() {
		return pagingToolbar;
	},

	/* Setup the tab panel for the document information */
	rightTabPanel: Ext.create('Ext.tab.Panel', {
		xtype : 'tabpanel',
		activeTab : 0,
		region : 'east',
//		split : true, // enable resizing
		width : '50%',
		title : 'More about this item',
		collapsed : false,
		collapsible : true,
		bodyPadding : 0,
		items : [  ]
	    }),
	    
	getRightTabPanel : function() {
		return rightTabPanel;
	}
	
};

/**
 * Setup the viewport itself
 */
cudl.MyViewportUi = Ext.extend(Ext.Viewport,
		{
			layout : 'border',
			margin : '0 0 0 0',					
			border : 0,
			padding : 0,			
			initComponent : function() {
				this.items = [ {
					layout: {
			            type: 'vbox',
			            align: 'stretch'
			        },					
					region : 'center',					
					border : false,
					margin : '0 0 0 0',					
					border : 0,
					padding : 0,					
					width : '100%',
					items : [ cudl.viewportComponents.pageTitlePanel,
					          cudl.viewportComponents.pagingToolbar , {
						el : 'seadragonImage',
						flex:1,
						border : true
					} ]
				}, cudl.viewportComponents.rightTabPanel, 
				{
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

				cudl.MyViewportUi.superclass.initComponent.call(this);
			}
		});

cudl.MyViewport = Ext.extend(cudl.MyViewportUi, {
	initComponent : function() {
		cudl.MyViewport.superclass.initComponent.call(this);
	}
});
