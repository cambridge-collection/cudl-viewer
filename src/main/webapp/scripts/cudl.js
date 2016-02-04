var cudl = {};

cudl.toggleDiv = function (divid) {
	if (document.getElementById(divid).style.display == 'none') {
		document.getElementById(divid).style.display = 'block';
	} else {
		document.getElementById(divid).style.display = 'none';
	}
}
/*
// Styles expandable trees
cudl.treestyler = function(){
	var tree = document.getElementById("tree");
	if(tree){

		cudl.treestyler.listItem = function(li){
			if(li.getElementsByTagName("ul").length > 0){
				var ul = li.getElementsByTagName("ul")[0];
				if (!ul.style.display) {
					ul.style.display = "none";
				}
				var span = document.createElement("span");
				span.title = "Click to expand/collapse";
				span.className = (ul.style.display == "none") ? "collapsed" : "expanded";
				span.onclick = function(){
					ul.style.display = (ul.style.display == "none") ? "block" : "none";
					this.className = (ul.style.display == "none") ? "collapsed" : "expanded";
					cudl.treestyler.expand[ul.id] = (ul.style.display != "none");
				};

				if (li.getElementsByTagName("span").length==0) {
				  li.appendChild(span);
				}
			};
		};
		
		var items = tree.getElementsByTagName("li");
		for(var i=0;i<items.length;i++){
			cudl.treestyler.listItem(items[i]);
		};
		
	};	
};

// Stores state of expand / collapse on tree
cudl.treestyler.expand = {};
*/

/* Fancybox setup */

$(document).ready(function() {

	try {	
 	  $(".iframe").fancybox({
		'width' : '75%',
		'height' : '75%',
		'autoSize' : false,
		'transitionIn' : 'none',
		'transitionOut' : 'none',
		'type' : 'iframe',
		'title': null
	  });
	
	  $(".fancybox-inline").fancybox({
		'width' : '75%',
		'height' : '75%',
		'autoSize' : false,
		'transitionIn' : 'none',
		'transitionOut' : 'none'
	  });	
	} catch (exception) {
		/* ignore, fancybox not always present */
	}
	// setup any expandable trees
	//cudl.treestyler();
});

cudl.setCookie = function (name,value,days)
{
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
};

cudl.getCookie = function (name)
{
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
};

cudl.displayCookieNotice = function () {

	  if (!cudl.getCookie("cudlcookies")) {	
		document.getElementById("cookienotice").style.display="inline";
	  } 
	};
	
cudl.acceptCookies = function () {
	cudl.setCookie("cudlcookies", "true", 183);
	document.getElementById("cookienotice").style.display="none";
	return false;
};


/**
 * Create a quick search box, optionally with the tagging variable-recall
 * slider, and filtered to a specific collection.
 *
 * Example markup:
 * <div class="quick-search quick-search-tagging"
 *      data-collection-facet="Cairo Genizah">
 *     <h3>Search the collection</h3>
 *     <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur urna velit, scelerisque nec nibh sit amet, interdum consectetur leo. Suspendisse et nibh elit.</p>
 *     <div class="search-placeholder"></div>
 * </div>
 *
 * Omit the quick-search-tagging class to remove the slider. Remove the
 * data-collection-facet attr to not pre-filter the search.
 */
cudl.quickSearch = function quickSearch(placeholderEl) {
    var $this = $(placeholderEl);

    if(!($this.is('.search-placeholder') &&
         $this.closest('.quick-search').length > 0)) {
        throw new Error('The element passed to quickSearch() must be a ' +
                        '.search-placeholder with an ancestor .quick-search')
    }

    var enableTagging = $this.closest('.quick-search')
                               .is('.quick-search-tagging');

    var recallSlider =
        '<div class="recall-slider">' +
            '<input class="recall-slider-input" type="text" name="recallScale"' +
                    'data-slider-value="0.1"' +
                    'data-slider-min="0"' +
                    'data-slider-max="1"' +
                    'data-slider-step="0.1"' +
                    'data-slider-ticks="[0, 0.5, 1]"' +
                    'data-slider-ticks-labels=\'["Curated<br>metadata", "Secondary<br>literature", "Crowd-<br>sourced"]\'' +
                    'data-slider-tooltip="hide">' +
            '<input type="hidden" name="tagging" value="1">' +
        '</div>';

    var keywordInput = $(
        '<div class="campl-column9">' +
            '<div class="campl-control-group">' +
                '<div class="campl-controls">' +
                    '<input placeholder="Keywords" class="campl-input-block-level" type="text" value="" name="keyword">' +
                '</div>' +
                (enableTagging ? recallSlider : '') +
            '</div>' +
        '</div>' +
        '<div class="campl-column2">' +
            '<div class="campl-controls">' +
                '<button type="submit" class="campl-btn campl-primary-cta">Submit</button>' +
            '</div>' +
        '</div>'
    );

    var form = $('<form action="/search/advanced/results" class="clearfix">')
        .append(keywordInput);

    // Allow pre-selecting a collection facet
    var collectionFacet = $this.closest('.quick-search').data('collection-facet');
    if(collectionFacet) {
        form.append(
            $('<input>')
                .attr({
                    type: 'hidden',
                    name: 'facetCollection',
                    value: collectionFacet
                })
        );
    }

    // Replace the placeholder with the search form
    form.replaceAll(placeholderEl);

    // Enable the bootstrap slider
    form.find('.recall-slider-input').slider();
}

// Automatically setup quick search boxes in page markup
$(function() {
    $('.quick-search .search-placeholder').each(function() {
        cudl.quickSearch(this);
    });
});
