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
				document.getElementById("metadata-title").innerHTML = data.title;
				document.getElementById("metadata-author").innerHTML = data.author;
				document.getElementById("metadata-rights").innerHTML = data.rights;
				document.getElementById("metadata-page").innerHTML = data.pages[pagenum - 1].name;
				document.getElementById("metadata-subject").innerHTML = data.subject;
				document.getElementById("metadata-physicalLocation").innerHTML = data.physicalLocation;
				document.getElementById("metadata-shelfLocation").innerHTML = data.shelfLocator;
				document.getElementById("metadata-dateCreatedDisplay").innerHTML = data.dateCreatedDisplay;
				
				// setup logical structure
				var ls = "";
				for ( var i = 0; i < data.logicalStructure.length; i++) {
					var lsItem = data.logicalStructure[i];
					ls += "<li><a href='" + lsItem.startPagePosition + "'>"
							+ lsItem.title + "</a> (image "
							+ lsItem.startPagePosition + ", page "
							+ lsItem.startPage+")</li>";
				}
				document.getElementById("logical_structure").innerHTML = "<div style='height: 100%; overflow:auto;'><ul>"
						+ ls + "</ul></div>";

			}

		}

	};

};
