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

