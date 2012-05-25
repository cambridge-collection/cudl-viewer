/**
 * Script to configure the ext viewport and data for the document view.
 */

cudl.downloadImageFunction = function(answer) {
	if (answer == 'yes') {
		window.open(cudl.downloadImageLink);
	} else {
		return;
	}
};

cudl.downloadMessage = {
	title : 'Image Licensing',
	buttons : Ext.Msg.YESNO,
	fn : cudl.downloadImageFunction
};

cudl.downloadImageCheck = function() {
	Ext.Msg.show(cudl.downloadMessage);
};

cudl.bookmarkPage = function(title, url) {
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

cudl.docView = function() {
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

			if (cudl.view.pageSet || !cudl.pagenum
					|| !cudl.view.isNumber(cudl.pagenum)) {
				cudl.pagenum = cudl.store.currentPage;

				// update the URL for this page with pagenum
				// does not change the URL if already correct.
				// supported in Chrome, Safari, FF4+, and IE10pp4+
				try {
					var updatedURL = "/view/" + cudl.docId + "/" + cudl.pagenum;
					if (window.location.pathname != updatedURL
							&& window.history.replaceState) {
						window.history.replaceState(cudl.docId + " page:"
								+ cudl.pagenum, "Cambridge Digital Library",
								updatedURL);
					}
				} catch (err) {
					/* not supported */
				}

			}

			// loadPage will cause this function to be called again as the
			// toolbar
			// has changed.
			if (!cudl.view.pageSet) {
				// default to page 1 if pagenum outside allowed range.
				if (cudl.pagenum < 0 || cudl.pagenum > cudl.store.totalCount) {
					cudl.pagenum = 1;
				}
				cudl.store.loadPage(cudl.pagenum);
				cudl.view.pageSet = true;
				return;
			}

			if (cudl.viewer && cudl.data) {

				// show image
				cudl.viewer.openDzi(cudl.proxyURL
						+ cudl.data.pages[cudl.pagenum - 1].displayImageURL);

				// setup image download link
				cudl.downloadImageLink = "/download/image%252Fjpg/document-image"
						+ cudl.pagenum
						+ ".jpg?path="
						+ cudl.data.pages[cudl.pagenum - 1].downloadImageURL;

				// setup transcription
				cudl.beforeTabShown(cudl.viewportComponents.rightTabPanel.activeTab);

				// Setup metadata
				
				// Get an array of metadata for all the descriptive metadata that 
				// applies to the currently viewed page. 
				var lsArray = cudl.view.getLogicalStructureArray(cudl.pagenum,
						cudl.data.logicalStructures);
				var metadataArray = cudl.view.getDescriptiveMetadataArray(
						cudl.pagenum, lsArray, cudl.data.descriptiveMetadata);
				var descriptiveMetadata = metadataArray[0];

				var metadata = "";

				for ( var i = 0; i < metadataArray.length; i++) {
					metadata += cudl.view
							.getHTMLFromDescriptiveMetadata(metadataArray[i]);

				}

				// Should always have a root element.
				cudl.view.populateElement(document
						.getElementById("metadata-book"), cudl.view
						.getHTMLFromDescriptiveMetadata(metadataArray[0]));

				if (metadataArray.length > 1) {
					cudl.view.populateElement(document
							.getElementById("metadata-chapter"), cudl.view
							.getHTMLFromDescriptiveMetadata(metadataArray[1]),
							true);
					cudl.view.populateElement(document
					.getElementById("metadata-chapter-title"), metadataArray[1].title.displayForm, true);
				} else {
					cudl.view.populateElement(document
							.getElementById("metadata-chapter"), "", true);
					cudl.view.populateElement(document
							.getElementById("metadata-chapter-title"), "", true);					
				}

				if (metadataArray.length > 2) {
					cudl.view.populateElement(document
							.getElementById("metadata-page"), cudl.view
							.getHTMLFromDescriptiveMetadata(metadataArray[2]),
							true);
				} else {
					cudl.view.populateElement(document
							.getElementById("metadata-page"), "", true);
				}
			
				// Set data that is displayed in the page outside the metadata
				// section
				// e.g. title, authors, abstract etc.
				cudl.view.populateElement(document
						.getElementById("metadata-title"), cudl.itemTitle);

				if (cudl.itemAuthors.length > 0) {
					cudl.view.populateElement(document
							.getElementById("metadata-author"),
							" by <span class='document-about-author'>"
									+ cudl.itemAuthors.join(", ") + "</span>");
				}

				cudl.view.populateElement(document
						.getElementById("metadata-display-rights"),
						descriptiveMetadata.displayImageRights.value);
				if (descriptiveMetadata.fundings) {
					cudl.view.populateElement(document
							.getElementById("metadata-funding"),
							descriptiveMetadata.fundings.value);
				}
				cudl.downloadMessage.msg = 'This image has the following copyright: <br/><br/>'
						+ descriptiveMetadata.downloadImageRights.value
						+ '<br/><br/> Do you want to download this image?';

				// update current page number display.
				cudl.view.populateElement(document
						.getElementById("metadata-pagenum"),
						cudl.data.pages[cudl.pagenum - 1].label, true);
				cudl.view.populateElement(document
						.getElementById("metadata-pagenum-toolbar"),
						cudl.data.pages[cudl.pagenum - 1].label, true);

				if (cudl.itemAbstract) {
					cudl.view.populateElement(document
							.getElementById("metadata-abstract"), ""
							+ cudl.itemAbstract + "<br />");
				}

				// setup logical structure

				// only write out contents if the element is currently empty
				// (the first time the page loads).
				if (document.getElementById("logical_structure").children[0].innerHTML == "") {

					var ls;
					if (cudl.data.logicalStructures[0].children) {

						ls = cudl.view.buildLogicalStructures(
								cudl.data.logicalStructures[0].children, 0);

						// If no first level contents use the root element
						// to prevent empty contents list.
					} else {
						ls = cudl.view.buildLogicalStructures(
								cudl.data.logicalStructures, 0);
					}

					cudl.view.populateElement(document
							.getElementById("logical_structure"),
							"<div style='height: 100%; overflow-y:auto;'><ul>"
									+ ls + "</ul></div>", true);

				}

			}

		},

		addSearchLink : function(text) {
			var encodedText = encodeURIComponent(text);
			return "<a class=\"cudlLink\" href='/search?keyword=" + encodedText
					+ "'>" + text + "</a>";
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
		 * @returns
		 */
		getMetadataHTML : function(title, metadataItem, arraySeparator) {

			var item = metadataItem;

			if (metadataItem instanceof Array) {

				var metadataArray = new Array();
				for ( var i = 0; i < metadataItem.length; i++) {

					var singleMetadataItem = metadataItem[i].displayForm;
					var searchLink = (metadataItem[i].linktype == "keyword search");
					if (searchLink) {
						metadataArray[i] = cudl.view
								.addSearchLink(singleMetadataItem);
					} else {
						metadataArray[i] = singleMetadataItem;
					}
				}

				if (metadataArray.length > 0) {
					item = metadataArray.join(arraySeparator);
				}

				// Not an array, a single item.
			} else {
				var searchLink = (metadataItem.linktype == "keyword search");
				if (searchLink) {
					item = cudl.view.addSearchLink(metadataItem);
				} else {
					item = metadataItem;
				}
			}

			if (item != "") {
				return "<li><div><b>" + title + ": </b>" + item
						+ "</div></li>\n";
			}

			return "";
		},

		getHTMLFromDescriptiveMetadata : function(descriptiveMetadata) {
			var metadata = "";

			for ( var key in descriptiveMetadata) {
				if (descriptiveMetadata.hasOwnProperty(key)) {

					var jsonObject = descriptiveMetadata[key];

					if (jsonObject.display == true && jsonObject.label) {

						if (jsonObject.displayForm) {

							metadata += cudl.view.getMetadataHTML(
									jsonObject.label, jsonObject.displayForm);

							// value is an array
						} else if (jsonObject.value
								&& jsonObject.value instanceof Array) {

							metadata += cudl.view.getMetadataHTML(
									jsonObject.label, jsonObject.value, "; ");
						}

					}
				}
			}

			return metadata;
		},

		/**
		 * Returns a 'flat' array of logical structures for that apply to the
		 * page given. Always includes ROOT logical structure.
		 */
		getLogicalStructureArray : function(pageNumber, logicalStructuresArray) {

			var lsArray = new Array();			
			for ( var i = 0; i < logicalStructuresArray.length; i++) {
				var ls = logicalStructuresArray[i];

				if (ls.startPagePosition <= pageNumber
						&& ls.endPagePosition >= pageNumber) {

					cudl.data.descriptiveMetadata[ls.descriptiveMetadataID]
					lsArray.push(ls);
				}
				if (ls.children) {
					lsArray.push.apply(lsArray, cudl.view
							.getLogicalStructureArray(pageNumber, ls.children));
				}
			}
			return lsArray;

		},

		/**
		 * Returns an array of metadata ids for the descriptiveMetadata sections
		 * that apply to the page given. Always includes ROOT
		 * descriptiveMetadata for this item.
		 */
		getDescriptiveMetadataArray : function(pageNumber,
				logicalStructuresArray, descriptiveMetadataArray) {
			
			var metadataArray = new Array(); 	
			for ( var i = 0; i < descriptiveMetadataArray.length; i++) {
				var dm = descriptiveMetadataArray[i];				
				
				for ( var j = 0; j < logicalStructuresArray.length; j++) {
					var ls = logicalStructuresArray[j];

					if (dm.ID == ls.descriptiveMetadataID) {
						metadataArray.push(dm);
					}
				}
			}
			return metadataArray;

		},

		buildLogicalStructures : function(logicalStructureElement, level) {

			// setup logical structure
			var ls = "";

			for ( var i = 0; i < logicalStructureElement.length; i++) {
				var lsItem = logicalStructureElement[i];
				for ( var j = 0; j < level; j++) {
					ls += "<li><ul>";
				}
				ls += "<li><a href='' onclick='cudl.store.loadPage("
						+ lsItem.startPagePosition + ");return false;'>"
						+ lsItem.label + "</a> (image "
						+ lsItem.startPagePosition + ", page "
						+ lsItem.startPage + ")</li>";
				for ( var j = 0; j < level; j++) {
					ls += "</ul></li> ";
				}

				if (lsItem.children && lsItem.children.length > 0) {
					ls += cudl.view.buildLogicalStructures(lsItem.children,
							level + 1);
				}
			}

			return ls;
		}

	};

};
