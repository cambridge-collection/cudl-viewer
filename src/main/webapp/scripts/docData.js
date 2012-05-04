/**
 * 
 * @returns object containing the JSON data
 */
var docData = function() {
	return {

		dataLoaded : false,

		/* read json that defines the data for this page */
		jsonreader : null,

		store : null,

		init : function() {

			/* read json that defines the data for this page */
			this.jsonreader = new Ext.data.JsonReader({
				root : 'pages',
				totalProperty : 'numberOfPages'
			},
					[ 'name', 'displayImageURL', 'downloadImageURL',
							'transcriptionNormalisedURL',
							'transcriptionDiplomaticURL' ]);

			this.store = Ext.create('Ext.data.Store', {
				id : 'pageStore',
				autoLoad : false,
				fields : [ 'name', 'displayImageURL', 'downloadImageURL',
						'transcriptionNormalisedURL',
						'transcriptionDiplomaticURL' ],
				pageSize : 1, // items per page
				proxy : new Ext.data.HttpProxy({
					url : cudl.JSONURL,
					method : 'POST',
					reader : this.jsonreader
				})
			});

		},

		load : function() {
			this.store.load();
		},

		getData : function() {
		
			return this.jsonreader.rawData;
		}

	};

};