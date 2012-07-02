var cudl = {};

cudl.toggleDiv = function (divid) {
	if (document.getElementById(divid).style.display == 'none') {
		document.getElementById(divid).style.display = 'block';
	} else {
		document.getElementById(divid).style.display = 'none';
	}
}

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
				span.title = "treespan";
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

/* Fancybox setup */
$(document).ready(function() {
	$(".iframe").fancybox({
		'width' : '75%',
		'height' : '75%',
		'autoScale' : false,
		'transitionIn' : 'none',
		'transitionOut' : 'none',
		'type' : 'iframe',
		'titleShow' : false
	});
	
	$("a#inline").fancybox({
		'width' : '75%',
		'height' : '75%',
		'autoScale' : false,
		'transitionIn' : 'none',
		'transitionOut' : 'none'
	});	
	
	// setup any expandable trees
	cudl.treestyler();
});

cudl.setCookie = function (c_name,value)
{
var c_value=escape(value);
document.cookie=c_name + "=" + c_value;
}

cudl.getCookie = function (c_name)
{
var i,x,y,ARRcookies=document.cookie.split(";");
for (i=0;i<ARRcookies.length;i++)
{
  x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
  y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
  x=x.replace(/^\s+|\s+$/g,"");
  if (x==c_name)
    {
    return unescape(y);
    }
  }
}
