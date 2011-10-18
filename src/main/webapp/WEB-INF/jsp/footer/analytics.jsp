<!--  Google Analytics -->

<script type="text/javascript">

	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl."
			: "http://www.");
	document
			.write(unescape("%3Cscript src='"
					+ gaJsHost
					+ "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">

	var googleAnalyticsID = UA-10976633-3; // default to live. 
	
	if ("${globalproperties.GoogleAnalyticsId}" != "") {
		googleAnalyticsID = "${globalproperties.GoogleAnalyticsId}";
	};

	try {
		var pageTracker = _gat._getTracker(googleAnalyticsID);
		pageTracker._trackPageview();
	} catch (err) {
	}
</script>
<script src="http://www.lib.cam.ac.uk/global/js/gaAddons.js" type="text/javascript"></script>