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
					/*{
					xtype : 'button',
					height : 28,
  			        	border: false,			
					text:'<< Collection',
					handler: function() {
					        history.go(-1);
					    }
					},
					{xtype: 'tbspacer', width: 40},*/
					pagingToolbar				
				]
			},{
			    el: 'center',
			    height: '90%',
			    border: true
			}
			]
		}, tabpanel, {
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
				el:'logical_structure'
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