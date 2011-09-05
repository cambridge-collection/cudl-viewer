/**
 * Script to configure the ext viewport and data for the document view.
 */

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

			var jsonData = data;

			if (viewer && data) {

				viewer.openDzi(jsonData.pages[pagenum - 1].displayImageURL);

				// setup transcription
				view.beforetabchange();

				if (jsonData.pages[pagenum - 1].transcriptionNormalisedURL
						&& jsonData.pages[pagenum - 1].transcriptionDiplomaticURL) {
					document.getElementById("transcription_normal_frame").onload = view.aftertabchange;
					document.getElementById("transcription_normal_frame").src = "/transcription?url="
							+ encodeURIComponent(jsonData.pages[pagenum - 1].transcriptionNormalisedURL);
					document.getElementById("transcription_diplomatic_frame").src = "/transcription?url="
							+ encodeURIComponent(jsonData.pages[pagenum - 1].transcriptionDiplomaticURL);
				} else {
					// there is no transcription to load, unmask the tab.
					//tabpanel.el.unmask();
				}

				// setup metadata
				document.getElementById("metadata-title").innerHTML = jsonData.title;
				document.getElementById("metadata-author").innerHTML = jsonData.author;
				document.getElementById("metadata-rights").innerHTML = jsonData.rights;
				document.getElementById("metadata-page").innerHTML = jsonData.pages[pagenum - 1].name;
				document.getElementById("metadata-subject").innerHTML = jsonData.subject;
				document.getElementById("metadata-physicalLocation").innerHTML = jsonData.physicalLocation;
				document.getElementById("metadata-shelfLocation").innerHTML = jsonData.shelfLocator;
				document.getElementById("metadata-dateCreatedDisplay").innerHTML = jsonData.dateCreatedDisplay;

				// setup logical structure
				var ls = "";
				for ( var i = 0; i < jsonData.logicalStructure.length; i++) {
					var lsItem = jsonData.logicalStructure[i];
					ls += "<li><a href='" + lsItem.startPagePosition + "'>"
							+ lsItem.title + "</a> [page:"
							+ lsItem.startPagePosition + "]</li>";
				}
				document.getElementById("logical_structure").innerHTML = "<ul>"
						+ ls + "</ul>";

			}

		}

	};

};
