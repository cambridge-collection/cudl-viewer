function toggleDiv(divid) {
	if (document.getElementById(divid).style.display == 'none') {
		document.getElementById(divid).style.display = 'block';
	} else {
		document.getElementById(divid).style.display = 'none';
	}
}

// Styles expandable trees
this.treestyler = function(){
	var tree = document.getElementById("tree");
	if(tree){

		this.listItem = function(li){
			if(li.getElementsByTagName("ul").length > 0){
				var ul = li.getElementsByTagName("ul")[0];
				ul.style.display = "none";
				var span = document.createElement("span");
				span.className = "collapsed";
				span.onclick = function(){
					ul.style.display = (ul.style.display == "none") ? "block" : "none";
					this.className = (ul.style.display == "none") ? "collapsed" : "expanded";
				};
				li.appendChild(span);
			};
		};
		
		var items = tree.getElementsByTagName("li");
		for(var i=0;i<items.length;i++){
			listItem(items[i]);
		};
		
	};	
};

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
	treestyler();
});

