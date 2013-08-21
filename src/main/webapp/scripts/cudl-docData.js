/**
 * 
 * @returns object containing the JSON data
 */
cudl.docData = function() {
	return {

		dataLoaded : false,

		/* read json that defines the data for this page */
		jsonreader : null,

		store : null,
		
		thumbnailStore : null,		

		init : function() {

			/* read json that defines the data for this page */
			this.jsonreader = new Ext.data.JsonReader({
				root : 'pages',
				totalProperty : 'numberOfPages'
			},
					[ 'label', 'sequence', 'displayImageURL', 'downloadImageURL',
							'transcriptionNormalisedURL',
							'transcriptionDiplomaticURL' ]);
		    
			this.store = Ext.create('Ext.data.Store', {
				id : 'pageStore',
				autoLoad : false,
				fields : [ 'label', 'sequence', 'displayImageURL', 'downloadImageURL',
						'transcriptionNormalisedURL',
						'transcriptionDiplomaticURL' ],
				pageSize : 1, // items per page
				proxy : new Ext.data.HttpProxy({
					url : cudl.JSONURL,
					method : 'POST',
					reader : this.jsonreader
				})
			});
			
			this.thumbnailStore = Ext.create('Ext.data.Store', {
				id : 'thumbStore',
				autoLoad : false,
				fields : [ 'label', 'sequence', 'displayImageURL', 'downloadImageURL',
						'transcriptionNormalisedURL',
						'transcriptionDiplomaticURL', 'thumbnailImageURL', 'thumbnailImageOrientation' ],
				pageSize : 8, // items per page
				proxy : new Ext.data.HttpProxy({
					url : cudl.JSONTHUMBURL,
					method : 'POST',
					reader : this.jsonreader
				})
			});			

		},

		load : function() {
			this.store.load();
			this.thumbnailStore.load();
		},

		getData : function() {
		
			return this.jsonreader.rawData;
		}

	};

};