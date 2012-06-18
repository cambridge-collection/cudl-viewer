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
			// toolbar has changed.
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
				
				// TODO only do once. 
				var allLSArray = cudl.view.flatten(cudl.data.logicalStructures);
				var lsArray = cudl.view.getLSArrayForPageViewed(cudl.pagenum, allLSArray);
				
				var metadataArray = cudl.view.getDescriptiveMetadataArray(
						cudl.pagenum, lsArray, cudl.data.descriptiveMetadata);							
				
				var descriptiveMetadata = metadataArray[0];
				
				// Write out first element, as this will always exist and should not change.
				var tree = document.getElementById("tree");
				if (tree.children.length==0) {	

					  var li = document.createElement('li');
					  li.innerHTML="Book Information: <em>"+metadataArray[0].title.displayForm+"</em>"+
					  "<ul><div>"+cudl.view.getHTMLFromDescriptiveMetadata(metadataArray[0])+"</div></ul>";
					  li.id = metadataArray[0].ID;
					  
					  tree.appendChild(li); 
				}
				
				// For the remaining items, replace the text or add/remove list items 
				// as needed. Note this is done to ensure that items already expanded 
				// keep their state.
				for (var i=1; i< metadataArray.length; i++) {
						
					var metadataItem = metadataArray[i];
					var li = document.createElement('li');
					
					li.innerHTML="Section Information: <em>"+metadataArray[i].title.displayForm+"</em>"+
					"<ul><div>"+cudl.view.getHTMLFromDescriptiveMetadata(metadataArray[i])+"</div></ul>";
					li.id = metadataArray[i].ID;					  
					
					if (tree.children.length<=i) {
					  // tree is less than the metadata we need to display
					  // add item to tree. 
					  tree.appendChild(li); 
					} else {
					  // tree has enough (or more than enough) items already.
					  // So change the existing item in tree
				      // Note this is done to ensure that items already expanded 
					  // keep their state.

					  // change section info title <em>
					  tree.children[i].children[0].innerHTML = li.children[0].innerHTML; 
					  // change the body <ul>
					  tree.children[i].children[1].innerHTML = li.children[1].innerHTML; 
					}
				}
				
				// hide any unused metadata sections in tree (from previous page views). 
				for (var i=metadataArray.length; i<tree.children.length; i++) {
				  tree.removeChild(tree.children[i]);					
				}
					 
				// style tree. Add expand/collapse css etc. 
				treestyler();		    
			
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
						.getElementById("metadata-pagenum-toolbar"),
						cudl.data.pages[cudl.pagenum - 1].label, true);

				
				if (descriptiveMetadata.abstract) {
					cudl.view.populateElement(document
							.getElementById("metadata-abstract"), ""
							+ descriptiveMetadata.abstract.displayForm + "<br />");
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
			
			// document has loaded. Display the content and remove the mask. 			
			cudl.loadMask.hide();
			document.getElementById('metadata-about').style.display='inline';
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


		/**
		 * Used to go through each element in a single descriptiveMetadata item and look for 
		 * suitable candidates to display.  These are put into an array for sorting. 
		 * Suitable json objects have - display=true, seq and label fields.  
		 * Returns an array of json objects. 
		 */
		getArrayFromDescriptiveMetadata : function(descriptiveMetadata) {

			var metadataArray = new Array();
			for ( var key in descriptiveMetadata) {
				if (descriptiveMetadata.hasOwnProperty(key) ) {					
					var jsonObject = descriptiveMetadata[key];
					
					// Handle case where there is no label at the top level, but there exists an 
					// array of objects under value that may have labels, display values or arrays of 
					// value strings to display.  
					if (!jsonObject.label && jsonObject.value && jsonObject.value instanceof Array) {
					  for (var i=0; i<jsonObject.value.length; i++) {
					    var value = jsonObject.value[0];
					    metadataArray = metadataArray.concat(cudl.view.getArrayFromDescriptiveMetadata(jsonObject.value[i]));
					  }
					}
					
				
					if (jsonObject.display==true && jsonObject.label && jsonObject.seq) {
						 metadataArray.push(jsonObject);
					}
				}
			}
			return metadataArray;
		},
		
		/**
		 * Generates HTML for the descriptiveMetadata item supplied.  Showing only those 
		 * with display = true and a label.  Ordered by seq. 
		 * Returns a string.
		 */
		getHTMLFromDescriptiveMetadata : function(descriptiveMetadata) {
			var metadata = "";

			var metadataArray = cudl.view.getArrayFromDescriptiveMetadata(descriptiveMetadata);
			metadataArray = metadataArray.sort(function (a,b) {if (a.seq && b.seq) {return a.seq-b.seq;}});
			
			for (var i=0; i<metadataArray.length; i++) {
				var jsonObject = metadataArray[i];
					
					if (jsonObject.display == true && jsonObject.label) {

						// prioritise displayForm at top level. 
						if (jsonObject.displayForm) {

							metadata += cudl.view.getMetadataHTML(
									jsonObject.label, jsonObject.displayForm);

						// then display value (if an array)
						} else if (jsonObject.value
								&& jsonObject.value instanceof Array) {

						    	metadata += cudl.view.getMetadataHTML(
										jsonObject.label, jsonObject.value, "; ");						     
						}

					}
				
			}

			return metadata;
		},

		/** 
		 * Flatten array so that any child elements (.children array) 
		 * appear on the top level of the array. 
		 * Returns a single level array of json objects. 
		 */
		flatten : function (jsonArray) {
			
			var lsArray = new Array();			
			for ( var i = 0; i < jsonArray.length; i++) {
				var ls = jsonArray[i];
                lsArray.push(ls);
			
				if (ls.children) {
					lsArray.push.apply(lsArray, cudl.view
							.flatten(ls.children));
				}
			}
			return lsArray;			
			
		}, 
		
		/**
		 * Returns an array of logical structures for that apply to the
		 * page given. Always includes ROOT logical structure.
		 * Takes in a 'flat' array of logical structures and a page number. 		 
		 */
		getLSArrayForPageViewed : function(pageNumber, logicalStructuresArray) {

			var lsArray = new Array();			
			for ( var i = 0; i < logicalStructuresArray.length; i++) {
				var ls = logicalStructuresArray[i];

				if (ls.startPagePosition <= pageNumber
						&& ls.endPagePosition >= pageNumber) {

					cudl.data.descriptiveMetadata[ls.descriptiveMetadataID]
					lsArray.push(ls);
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
						+ lsItem.startPageLabel + ")</li>";
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
