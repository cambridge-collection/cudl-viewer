/**
 * Script to configure the ext viewport and data for the document view.
 */
var downloadImageLink;
var isMSIE = /* @cc_on!@ */0;
var transcriptionNormalisedURL;
var transcriptionDiplomaticURL;

var downloadImage = function(answer) {
	if (answer == 'yes') {
		window.open(downloadImageLink);
	} else {
		return;
	}
};

var dloadMessage = {
	title : 'Image Licensing',
	buttons : Ext.Msg.YESNO,
	fn : downloadImage
};

var downloadImageCheck = function() {
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

		findDescriptiveMetadata : function(id, data) {

			for ( var i = 0; i < data.descriptiveMetadata.length; i++) {
				if (data.descriptiveMetadata[i].ID == id) {
					return data.descriptiveMetadata[i];
				}

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
				viewer.openDzi(proxyURL+data.pages[pagenum - 1].displayImageURL);

				// setup image download link
				downloadImageLink = "/download/image%252Fjpg/document-image"
						+ pagenum + ".jpg?path="
						+ data.pages[pagenum - 1].downloadImageURL;

				// setup transcription
				beforeTabShown(viewportComponents.rightTabPanel.activeTab);

				// setup metadata
				// Find the ROOT descriptiveMetadata object.
				var descriptiveMetadataID = data.logicalStructure[0].descriptiveMetadataID;
				var descriptiveMetadata = view.findDescriptiveMetadata(
						descriptiveMetadataID, data);

				view.populateElement(document.getElementById("metadata-title"),
						descriptiveMetadata.title);
				if (descriptiveMetadata.author
						&& descriptiveMetadata.author != "") {
					view.populateElement(document
							.getElementById("metadata-author"),
							" by <span class='document-about-author'>"
									+ descriptiveMetadata.author + "</span>");
				}
				view.populateElement(document
						.getElementById("metadata-display-rights"),
						descriptiveMetadata.displayImageRights);
				dloadMessage.msg = 'This image has the following copyright: <br/><br/>'
						+ descriptiveMetadata.downloadImageRights
						+ '<br/><br/> Do you want to download this image?';
				view.populateElement(document.getElementById("metadata-page"),
						data.pages[pagenum - 1].name, true);
				view.populateElement(document
						.getElementById("metadata-page-toolbar"),
						data.pages[pagenum - 1].name, true);
				view.populateElement(document
						.getElementById("metadata-subject"),
						descriptiveMetadata.subject);
				view.populateElement(document
						.getElementById("metadata-physicalLocation"),
						descriptiveMetadata.physicalLocation);
				view.populateElement(document
						.getElementById("metadata-shelfLocation"),
						descriptiveMetadata.shelfLocator);
				view.populateElement(document
						.getElementById("metadata-dateCreatedDisplay"),
						descriptiveMetadata.dateCreatedDisplay);

				// optional metadata
				
				var optionalMetadata = "";
				optionalMetadata += view.getMetadataHTML("Uniform title: ", descriptiveMetadata.uniformTitle);
				optionalMetadata += view.getMetadataHTML("Alternative title: ", descriptiveMetadata.alternativeTitle);
				optionalMetadata += view.getMetadataHTML("Publisher: ", descriptiveMetadata.publisher);
				optionalMetadata += view.getMetadataHTML("Publication place: ", descriptiveMetadata.publicationPlace);
				optionalMetadata += view.getMetadataHTML("Extent: ", descriptiveMetadata.extent);
				optionalMetadata += view.getMetadataHTML("Notes: ", descriptiveMetadata.notes);
				optionalMetadata += view.getMetadataHTML("Ownership: ", descriptiveMetadata.ownership);
						
				view.populateElement(document
						.getElementById("metadata-optional"),
						optionalMetadata);

				var abstractText = "";
				if (descriptiveMetadata.mediaUrl) {

					var mediawidth = 280;
					var mediaheight = 157;
					abstractText = "<object style=\"float:left; margin-right:10px;\" width=\""
							+ mediawidth
							+ "\" height=\""
							+ mediaheight
							+ "\"><param name=\"movie\" value=\""
							+ descriptiveMetadata.mediaUrl
							+ "\"></param><param name=\"allowFullScreen\" value=\"true\"></param><param name=\"allowscriptaccess\" value=\"always\"></param><embed src=\""
							+ descriptiveMetadata.mediaUrl
							+ "\" type=\"application/x-shockwave-flash\" width=\""
							+ mediawidth
							+ "\" height=\""
							+ mediaheight
							+ "\" allowscriptaccess=\"always\" allowfullscreen=\"true\"></embed></object>";

				}
				if (descriptiveMetadata.abstract) {
					abstractText = abstractText + descriptiveMetadata.abstract;

					view.populateElement(document
							.getElementById("metadata-abstract"), ""
							+ abstractText + "<br />");
				}

				// setup logical structure

				var ls = view.buildLogicalStructure(
						data.logicalStructure[0].children, 0);

				// If no first level contents use the root element
				// to prevent empty contents list.
				if (ls == "") {
					ls = view.buildLogicalStructure(data.logicalStructure, 0);
				}

				// only write out contents if the element is currently empty
				// (the first time the page loads).
				if (document.getElementById("logical_structure").children[0].innerHTML == "") {

					view.populateElement(document
							.getElementById("logical_structure"),
							"<div style='height: 100%; overflow-y:auto;'><ul>"
									+ ls + "</ul></div>", true);

				}

			}

		},
		
		getMetadataHTML : function (title, metadataItem) {
			
			if (metadataItem && metadataItem!="") {
				return "<div>"+title + "<b>"+metadataItem+"</b></div>\n";
			}
			return "";
		},

		buildLogicalStructure : function(logicalStructureElement, level) {

			// setup logical structure
			var ls = "";

			for ( var i = 0; i < logicalStructureElement.length; i++) {
				var lsItem = logicalStructureElement[i];
				for ( var j = 0; j < level; j++) {
					ls += "<li><ul>";
				}
				ls += "<li><a href='' onclick='store.loadPage("
						+ lsItem.startPagePosition + ");return false;'>"
						+ lsItem.label + "</a> (image "
						+ lsItem.startPagePosition + ", page "
						+ lsItem.startPage + ")</li>";
				for ( var j = 0; j < level; j++) {
					ls += "</ul></li> ";
				}

				if (lsItem.children.length > 0) {
					ls += view
							.buildLogicalStructure(lsItem.children, level + 1);
				}
			}

			return ls;
		}

	};

};
