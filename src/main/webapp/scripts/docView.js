/**
 * Script to configure the ext viewport and data for the document view.
 */
var downloadImageLink;
var isMSIE = /*@cc_on!@*/0;

var docView = function() {
	return {
		
		pageSet: false,
		
		/* While a tab is loading it is masked. */
		beforetabchange : function(panel) {	
			//alert ("before");
			if (isMSIE) {
			   /* do not show loading mask in ie */
			} else {
				panel.mask("Loading ...", "x-mask-loading");
			}						
		},

		aftertabchange : function(panel, newCard) {
			//alert ("after");
			if (isMSIE) {
				/* do not show loading mask in ie */
			} else {
				panel.unmask();
			}
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
		updateCurrentPage : function(toolbar, pageData, eOpts) {									  	

			// FIXME validate input 
			
			// Input page from next/back button etc. 
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
				view.beforetabchange(viewportComponents.tabpanel.items.items[1].el);
				view.beforetabchange(viewportComponents.tabpanel.items.items[2].el);

				//if (data.pages[pagenum - 1].transcriptionNormalisedURL
				//		&& data.pages[pagenum - 1].transcriptionDiplomaticURL) {
					document.getElementById("transcription_normal_frame").onload = function(){view.aftertabchange(viewportComponents.tabpanel.items.items[1].el);};
					document.getElementById("transcription_diplomatic_frame").onload = function(){view.aftertabchange(viewportComponents.tabpanel.items.items[2].el);};
					
					document.getElementById("transcription_normal_frame").src = "/transcription?url="
							+ encodeURIComponent(data.pages[pagenum - 1].transcriptionNormalisedURL);
					document.getElementById("transcription_diplomatic_frame").src = "/transcription?url="
							+ encodeURIComponent(data.pages[pagenum - 1].transcriptionDiplomaticURL);
				//} else {
					// there is no transcription to load, unmask the tab.
				//	viewportComponents.tabpanel.items.items[1].body.unmask();
				//}

				// setup metadata
				view.populateElement(document.getElementById("metadata-title"), data.title);
				view.populateElement(document.getElementById("metadata-author"), data.author);
				view.populateElement(document.getElementById("metadata-rights"), data.rights);
				view.populateElement(document.getElementById("metadata-page"), data.pages[pagenum - 1].name, true);
				view.populateElement(document.getElementById("metadata-page-toolbar"), data.pages[pagenum - 1].name, true);				
				view.populateElement(document.getElementById("metadata-subject"), data.subject);
				view.populateElement(document.getElementById("metadata-physicalLocation"), data.physicalLocation);
				view.populateElement(document.getElementById("metadata-shelfLocation"), data.shelfLocator);
				view.populateElement(document.getElementById("metadata-dateCreatedDisplay"), data.dateCreatedDisplay);

				if (data.abstract) {
					view.populateElement(document.getElementById("metadata-abstract"),""+data.abstract+"<br />");
				}
				if (data.mediaurl) {
					//view.populateElement(document.getElementById("metadata-media"),"<iframe style='float: right; padding-left:10px; padding-right:10px' width='320' height='245'	src='"+data.mediaurl+"' frameborder='0' allowfullscreen></iframe>");
					var mediawidth = 220;
					var mediaheight = 175;
					view.populateElement(document.getElementById("metadata-media"),"<object width=\""+mediawidth+"\" height=\""+mediaheight+"\"><param name=\"movie\" value=\""+data.mediaurl+"\"></param><param name=\"allowFullScreen\" value=\"true\"></param><param name=\"allowscriptaccess\" value=\"always\"></param><embed src=\""+data.mediaurl+"\" type=\"application/x-shockwave-flash\" width=\""+mediawidth+"\" height=\""+mediaheight+"\" allowscriptaccess=\"always\" allowfullscreen=\"true\"></embed></object>");
				}
				
				// setup logical structure
				var ls = "";
				for ( var i = 0; i < data.logicalStructure.length; i++) {
					var lsItem = data.logicalStructure[i];
					ls += "<li><a href='' onclick='store.loadPage(" + lsItem.startPagePosition + ");return false;'>"
							+ lsItem.title + "</a> (image "
							+ lsItem.startPagePosition + ", page "
							+ lsItem.startPage+")</li>";
				}
				view.populateElement(document.getElementById("logical_structure"),"<div style='height: 100%; overflow-y:auto;'><ul>"
						+ ls + "</ul></div>", true);

			}

		}

	};
	

};
