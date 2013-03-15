/*
 * Functions for the genizah bibliography interface.
 */

/* 
 * Datatables column sorting function for classmarks, 
 * based roughly on the one for sorting IP addresses.
 * Simply splits on the "." and pads the numbers. 
 */
jQuery.extend(jQuery.fn.dataTableExt.oSort, {
	"classmark-pre" : function(c) {
		var re = /[\s\.]/;
		var splitClassmark = c.split(".");
		var convertedForm = "";
		for (var i = 0; i < splitClassmark.length; i++) {
			var item = splitClassmark[i]; 
			if (item.length == 1) {
				convertedForm += "00" + item;  
			} else if (item.length == 2) {
				convertedForm += "0" + item;
			} else {
				convertedForm += item;
			}
		}
		return convertedForm;
	},
	
	"classmark-asc": function(a, b) {
		return ((a < b) ? -1 : ((a > b)? 1 : 0));
	},
	
	"classmark-desc": function(a, b) {
		return ((a < b) ? 1 : ((a > b)? -1 : 0));
	}
	
});