/**
 * Script to configure the ext viewport and data for the document view.
 */
var downloadImageLink;

var docView = function() {
	return {
		
		pageSet: false,
		
		/* While a tab is loading it is masked. */
		beforetabchange : function(panel, newCard) {
		//	console.debug (viewportComponents.tabpanel.items.items[1]);
		//	viewportComponents.tabpanel.items.items[1].el.mask("Loading ...", "x-mask-loading");
		},

		aftertabchange : function(panel, newCard) {
			//viewportComponents.tabpanel.el.unmask();
		},

		isNumber : function(n) {
			return !isNaN(parseFloat(n)) && isFinite(n);
		},
		
		/**
		 * Replaces the innerHTML of the element if it has changed.
		 * @param element should always exist
		 * @param text may be null or blank.
		 * @param overwrite boolean. will overwrite existing value even 
		 * if it is not the empty. defaults false. Generally true for items
		 * that vary with each page/image.
		 * @returns
		 */
		populateElement: function(element, text, overwrite) {
			
			if (!text) { text =""; }
			
			if (element.innerHTML=="" || overwrite) {
				  
				element.innerHTML=text;
			  
			} 
		},		

		/*
		 * On a page turn the data, images etc for that page need to be
		 * displayed.
		 */
		/* NOTE: ANY changes to the toolbar cause this top be called */		
		updateCurrentPage : function() {
			
			// FIXME validate input properly.
			if (view.pageSet || !pagenum || !view.isNumber(pagenum)) {
				pagenum = store.currentPage;
			}

			// This will cause this function to be called again as the toolbar
			// has changed.
			if (!view.pageSet) {
				store.loadPage(pagenum);
				view.pageSet = true;
				return;
			}

			if (viewer && data) {

				// show image
				viewer.openDzi(data.pages[pagenum - 1].displayImageURL);

				// setup image download link
				downloadImageLink = "/download/image%252Fjpg/document-image"+pagenum+".jpg?path="+data.pages[pagenum - 1].downloadImageURL;
				
				// setup transcription
				//view.beforetabchange();

				//if (data.pages[pagenum - 1].transcriptionNormalisedURL
				//		&& data.pages[pagenum - 1].transcriptionDiplomaticURL) {
					//document.getElementById("transcription_normal_frame").onload = view.aftertabchange;
					document.getElementById("transcription_normal_frame").src = "/transcription?url="
							+ encodeURIComponent(data.pages[pagenum - 1].transcriptionNormalisedURL);
					document.getElementById("transcription_diplomatic_frame").src = "/transcription?url="
							+ encodeURIComponent(data.pages[pagenum - 1].transcriptionDiplomaticURL);
				//} else {
					// there is no transcription to load, unmask the tab.
					//tabpanel.el.unmask();
				//}

				// setup metadata
				view.populateElement(document.getElementById("metadata-title"), data.title);
				view.populateElement(document.getElementById("metadata-author"), data.author);
				view.populateElement(document.getElementById("metadata-rights"), data.rights);
				view.populateElement(document.getElementById("metadata-page"), data.pages[pagenum - 1].name, true);
				view.populateElement(document.getElementById("metadata-subject"), data.subject);
				view.populateElement(document.getElementById("metadata-physicalLocation"), data.physicalLocation);
				view.populateElement(document.getElementById("metadata-shelfLocation"), data.shelfLocator);
				view.populateElement(document.getElementById("metadata-dateCreatedDisplay"), data.dateCreatedDisplay);

				if (data.abstract) {
					view.populateElement(document.getElementById("metadata-abstract"),"<br />"+data.abstract+"<br />");
				}
				if (data.mediaurl) {
					view.populateElement(document.getElementById("metadata-media"),"<iframe style='float: right; padding-left:10px; padding-right:10px' width='320' height='245'	src='"+data.mediaurl+"' frameborder='0' allowfullscreen></iframe>");
				}
				
				// setup logical structure
				var ls = "";
				for ( var i = 0; i < data.logicalStructure.length; i++) {
					var lsItem = data.logicalStructure[i];
					ls += "<li><a href='" + lsItem.startPagePosition + "'>"
							+ lsItem.title + "</a> (image "
							+ lsItem.startPagePosition + ", page "
							+ lsItem.startPage+")</li>";
				}
				view.populateElement(document.getElementById("logical_structure"),"<div style='height: 100%; overflow:auto;'><ul>"
						+ ls + "</ul></div>", true);

			}

		}

	};
	

};
