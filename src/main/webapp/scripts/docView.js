/**
 * Script to configure the ext viewport and data for the document view.
 */
var downloadImageLink;
var isMSIE = /* @cc_on!@ */0;

// Functions to show and hide the right hand panel.
var unmaskRightPanel = function(pos) {
	if (viewportComponents.tabpanel.items.items[pos].el.unmask) {
		viewportComponents.tabpanel.items.items[pos].el.unmask();
	}
};

var maskRightPanel = function(pos) {
	if (viewportComponents.tabpanel.items.items[pos].el.mask) {
		viewportComponents.tabpanel.items.items[pos].el.mask("Loading ...",
				"x-mask-loading");
		;
	}
};

var downloadImage = function(answer) {
	if (answer=='yes') {
	 window.open(downloadImageLink);
	} else {
	 return;
	}
};

var dloadMessage = {
	   title:'Image Licensing',	   
	   buttons: Ext.Msg.YESNO,
	   fn: downloadImage
	};

var downloadImageCheck = function () {
	Ext.Msg.show(dloadMessage);
};

var docView = function() {
	return {

		pageSet : false,

		isNumber : function(n) {
			return !isNaN(parseFloat(n)) && isFinite(n);
		},

		/**
		 * Replaces the innerHTML of the element if it has changed.
		 * 
		 * @param element
		 *            should always exist
		 * @param text
		 *            may be null or blank.
		 * @param overwrite
		 *            boolean. will overwrite existing value even if it is not
		 *            the empty. defaults false. Generally true for items that
		 *            vary with each page/image.
		 * @returns
		 */
		populateElement : function(element, text, overwrite) {

			if (!text) {
				text = "";
			}

			if (element.innerHTML == "" || overwrite) {

				element.innerHTML = text;

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
				downloadImageLink = "/download/image%252Fjpg/document-image"
						+ pagenum + ".jpg?path="
						+ data.pages[pagenum - 1].downloadImageURL;

				// setup transcription
				maskRightPanel(1);
				maskRightPanel(2);

				document.getElementById("transcription_normal_frame").src = "/transcription?url="
						+ encodeURIComponent(data.pages[pagenum - 1].transcriptionNormalisedURL);
				document.getElementById("transcription_diplomatic_frame").src = "/transcription?url="
						+ encodeURIComponent(data.pages[pagenum - 1].transcriptionDiplomaticURL);

				// setup metadata
				view.populateElement(document.getElementById("metadata-title"),
						data.title);
				view
						.populateElement(document
								.getElementById("metadata-author"), data.author);
				view.populateElement(document
						.getElementById("metadata-display-rights"),
						data.displayImageRights);
				dloadMessage.msg='This image has the following copyright: <br/><br/>'+data.downloadImageRights+'<br/><br/> Do you want to download this image?';
				view.populateElement(document.getElementById("metadata-page"),
						data.pages[pagenum - 1].name, true);
				view.populateElement(document
						.getElementById("metadata-page-toolbar"),
						data.pages[pagenum - 1].name, true);
				view.populateElement(document
						.getElementById("metadata-subject"), data.subject);
				view.populateElement(document
						.getElementById("metadata-physicalLocation"),
						data.physicalLocation);
				view.populateElement(document
						.getElementById("metadata-shelfLocation"),
						data.shelfLocator);
				view.populateElement(document
						.getElementById("metadata-dateCreatedDisplay"),
						data.dateCreatedDisplay);

				if (data.abstract) {
					view.populateElement(document
							.getElementById("metadata-abstract"), ""
							+ data.abstract + "<br />");
				}
				if (data.mediaurl) {

					var mediawidth = 220;
					var mediaheight = 175;
					view
							.populateElement(
									document.getElementById("metadata-media"),
									"<object width=\""
											+ mediawidth
											+ "\" height=\""
											+ mediaheight
											+ "\"><param name=\"movie\" value=\""
											+ data.mediaurl
											+ "\"></param><param name=\"allowFullScreen\" value=\"true\"></param><param name=\"allowscriptaccess\" value=\"always\"></param><embed src=\""
											+ data.mediaurl
											+ "\" type=\"application/x-shockwave-flash\" width=\""
											+ mediawidth
											+ "\" height=\""
											+ mediaheight
											+ "\" allowscriptaccess=\"always\" allowfullscreen=\"true\"></embed></object>");
				}

				// setup logical structure
				var ls = "";
				for ( var i = 0; i < data.logicalStructure.length; i++) {
					var lsItem = data.logicalStructure[i];
					ls += "<li><a href='' onclick='store.loadPage("
							+ lsItem.startPagePosition + ");return false;'>"
							+ lsItem.title + "</a> (image "
							+ lsItem.startPagePosition + ", page "
							+ lsItem.startPage + ")</li>";
				}
				view.populateElement(document
						.getElementById("logical_structure"),
						"<div style='height: 100%; overflow-y:auto;'><ul>" + ls
								+ "</ul></div>", true);

			}

		}

	};

};
