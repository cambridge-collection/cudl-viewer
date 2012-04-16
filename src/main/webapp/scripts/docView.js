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

var bookmarkPage = function(title, url) {
	Ext.Msg.show({
		title : 'Bookmark Page',
		msg : 'The URL to bookmark for direct access to this page is: <br/>'
				+ url,
		buttons : Ext.Msg.OK,
		fn : function() {
			return;
		}
	});

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
		/* NOTE: ANY changes to the toolbar cause this to be called */
		updateCurrentPage : function(toolbar, pageData, eOpts) {
	
			if (view.pageSet || !pagenum || !view.isNumber(pagenum)) {					
					pagenum = store.currentPage;	
					
					// update the URL for this page.
					// supported in Chrome, Safari, FF4+, and IE10pp4+
					try {
					  window.history.pushState(docId+" page:"+pagenum, "Cambridge Digital Library", "/view/"+docId+"/"+pagenum);
					} catch (err) {
						/* not supported */
					}
					
			}

			// This will cause this function to be called again as the toolbar
			// has changed.
			if (!view.pageSet) {	
				// default to page 1 if pagenum outside allowed range. 
				if (pagenum<0 || pagenum>store.totalCount) {
					pagenum=1;
				}				
				store.loadPage(pagenum);
				view.pageSet = true;
				return;
			}

			if (viewer && data) {

				// show image
				viewer.openDzi(proxyURL
						+ data.pages[pagenum - 1].displayImageURL);

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

				// author and people
				var authors = new Array();
				var authorsfullform = new Array();
				var fowners = new Array();
				var donors = new Array();
				var scribes = new Array();
				var recipients = new Array();
				var otherpeople = new Array();
				if (descriptiveMetadata.names) {
					for ( var i = 0; i < descriptiveMetadata.names.length; i++) {
						var name = descriptiveMetadata.names[i];
						if (name && name.role == "aut") {
							authors.push(name.displayForm);
							authorsfullform.push(name.fullForm);
						} else if (name && name.role == "fmo") {
							fowners.push(name.fullForm);
						} else if (name && name.role == "dnr") {
							donors.push(name.fullForm);
						} else if (name && name.role == "scr") {
							scribes.push(name.fullForm);
						} else if (name && name.role == "rcp") {
							recipients.push(name.fullForm);
						} else if (name && name.role == "pbl") {
							otherpeople.push(name.fullForm);
						} else if (name && name.role == "ann") {
							otherpeople.push(name.fullForm);
						} else if (name && name.role == "oth") {
							otherpeople.push(name.fullForm);
						}
					}
				}
				if (authors.length > 0) {
					view.populateElement(document
							.getElementById("metadata-author"),
							" by <span class='document-about-author'>"
									+ authors.join(", ") + "</span>");
				}
				view.populateElement(document
						.getElementById("metadata-display-rights"),
						descriptiveMetadata.displayImageRights);
				view.populateElement(document
						.getElementById("metadata-funding"),
						descriptiveMetadata.funding);
				dloadMessage.msg = 'This image has the following copyright: <br/><br/>'
						+ descriptiveMetadata.downloadImageRights
						+ '<br/><br/> Do you want to download this image?';

				// update current page number display.
				view.populateElement(document.getElementById("metadata-page"),
						data.pages[pagenum - 1].name, true);
				view.populateElement(document
						.getElementById("metadata-page-toolbar"),
						data.pages[pagenum - 1].name, true);

				// update metadata
				var basicMetadata = "";
				basicMetadata += view.getMetadataHTML("Physical location: ",
						descriptiveMetadata.physicalLocation);
				basicMetadata += view.getMetadataHTML("Classmark: ",
						descriptiveMetadata.shelfLocator);
				basicMetadata += view.getMetadataHTML("Subject: ",
						descriptiveMetadata.subject, "; ", true);
				basicMetadata += view.getMetadataHTML("Date created: ",
						descriptiveMetadata.dateCreatedDisplay, null, true);

				view.populateElement(document.getElementById("metadata-basic"),
						basicMetadata);

				// optional metadata

				var optionalMetadata = "";

				optionalMetadata += view.getMetadataHTML("Language(s): ",
						descriptiveMetadata.languageString);
				optionalMetadata += view.getMetadataHTML("Uniform title: ",
						descriptiveMetadata.uniformTitle);
				optionalMetadata += view.getMetadataHTML(
						"Alternative title(s): ",
						descriptiveMetadata.alternativeTitle, "; ");
				optionalMetadata += view.getMetadataHTML("Author(s): ",
						authorsfullform, "; ", true);
				optionalMetadata += view.getMetadataHTML("Former Owner(s): ",
						fowners, "; ", true);
				optionalMetadata += view.getMetadataHTML("Donor(s): ", donors,
						"; ", true);
				optionalMetadata += view.getMetadataHTML("Scribe(s): ",
						scribes, "; ", true);
				optionalMetadata += view.getMetadataHTML("Recipient(s): ",
						recipients, "; ", true);
				optionalMetadata += view.getMetadataHTML("Associated People: ",
						otherpeople, "; ", true);
				optionalMetadata += view.getMetadataHTML("Publisher: ",
						descriptiveMetadata.publisher, "; ");
				optionalMetadata += view.getMetadataHTML("Origin place: ",
						descriptiveMetadata.originPlace, "; ");
				optionalMetadata += view.getMetadataHTML("Extent: ",
						descriptiveMetadata.extent);
				optionalMetadata += view.getMetadataHTML("Layout: ",
						descriptiveMetadata.layout);
				optionalMetadata += "<p>"
						+ view.getMetadataHTML("Binding: ",
								descriptiveMetadata.binding, "</p><br/><p>")
						+ "</p>";
				optionalMetadata += "<p>"
						+ view.getMetadataHTML("Support: ",
								descriptiveMetadata.support, "</p><br/><p>")
						+ "</p>";
				optionalMetadata += "<p>"
						+ view.getMetadataHTML("Script: ",
								descriptiveMetadata.script, "</p><br/><p>")
						+ "</p>";
				optionalMetadata += "<p>"
						+ view.getMetadataHTML("Decoration: ",
								descriptiveMetadata.decoration, "</p><br/><p>")
						+ "</p>";
				optionalMetadata += "<p>"
						+ view.getMetadataHTML("Notes: ",
								descriptiveMetadata.notes, "</p><br/><p>")
						+ "</p>";
				optionalMetadata += "<p>"
						+ view.getMetadataHTML("Ownership: ",
								descriptiveMetadata.ownership, "</p><br/><p>")
						+ "</p>";

				view.populateElement(document
						.getElementById("metadata-optional"), optionalMetadata);

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

		/**
		 * Converts the metadata item into HTML representation.
		 * 
		 * @param title -
		 *            Text for the label
		 * @param metadataItem -
		 *            Metadata item to display
		 * @param arraySeparator -
		 *            for arrays this is the text that will separate items.
		 * @param searchLink -
		 *            boolean. Makes text a link to the search with the word as
		 *            a keyword.
		 * @returns
		 */
		getMetadataHTML : function(title, metadataItem, arraySeparator,
				searchLink) {

			if (searchLink) {

				if (metadataItem instanceof Array) {
					var metadataItemWithLink = new Array();
					for ( var i = 0; i < metadataItem.length; i++) {
						var singleMetadataItem = metadataItem[i];
						var encodedMetadataItem = encodeURIComponent(singleMetadataItem);
						metadataItemWithLink[i] = "<a class=\"cudlLink\" href='/search?keyword="
								+ encodedMetadataItem
								+ "'>"
								+ singleMetadataItem + "</a>";
					}
					
					return "<div><b>" + title + "</b>" + metadataItemWithLink.join(arraySeparator) + "</div>\n";
				} else {
					var encodedMetadataItem = encodeURIComponent(metadataItem);
					metadataItem = "<a class=\"cudlLink\" href='/search?keyword="
						+ encodedMetadataItem
						+ "'>"
						+ metadataItem + "</a>";
					return "<div><b>" + title + "</b>" + metadataItem + "</div>\n";
				}
				
			}
			
			if (metadataItem instanceof Array && arraySeparator) {
				metadataItem = metadataItem.join(arraySeparator);
			}
			if (metadataItem && metadataItem != "") {
				return "<div><b>" + title + "</b>" + metadataItem + "</div>\n";
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
