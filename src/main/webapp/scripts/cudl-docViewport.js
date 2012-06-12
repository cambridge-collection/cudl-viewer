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
		split : true, // enable resizing
		width : '50%',
		maxWidth: 550,
		title : 'More about this item',
		collapsed : false,
		collapsible : true,
		bodyPadding : 0,
		items : [  ]
	    }),
	    
	getRightTabPanel : function() {
		return rightTabPanel;
	}
	
	/*
	contentsTree: new Ext.tree.TreePanel({
	    renderTo: Ext.getBody(),
	    title: 'Sections',
	    width: 200,
	    height: 250,
	    userArrows: true,
	    animate: true,
	    autoScroll: true,
	    dataUrl: 'tree-nodes.php',
	    root: {
	        nodeType: 'async',
	        text: 'Root Node'
	    },
	    listeners: {
	        render: function() {
	            this.getRootNode().expand();
	        }
	    }
	   }),
	
	getContentsTree: function () {
		return contentsTree;
	},*/
		/*
	leftTabPanel: new Ext.create('Ext.tab.Panel', {
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
		items : [{
			xtype : 'panel',
			title : 'Sections',
			autoScroll:true,
			el : 'logical_structure'
		}]
	   }),
	   
	getLeftTabPanel: function() {
		return leftTabPanel;
	}*/



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
					height : '100%',
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
