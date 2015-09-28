<!-- JQuery -->
<script type="text/javascript" src="/scripts/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/scripts/spin.min.js"></script>

<!--  bootstrap -->
<link rel="stylesheet" href="/styles/bootstrap-default.min.css">
<script src="/scripts/bootstrap.min.js"></script>

<!--  Project Light  -->
<link rel="stylesheet" href="/styles/projectlight-full-stylesheet.css" />
<script type="text/javascript"	src="/scripts/projectlight-libs/ios-orientationchange-fix.js"></script>
<script type="text/javascript"	src="/scripts/projectlight-libs/modernizr.js"></script>
<script type="text/javascript"	src="/scripts/projectlight-libs/jquery-min.js"></script>
<script type="text/javascript" src="/scripts/projectlight-custom.js"></script>

<!-- Fancybox -->
<link rel="stylesheet" href="/scripts/fancybox/jquery.fancybox.css" type="text/css" media="screen" />
<script type="text/javascript" src="/scripts/fancybox/jquery.fancybox.pack.js"></script>

<!-- Pagination -->
<script type="text/javascript" src="/scripts/jquery.paging.min.js"></script>

<!-- CUDL -->
<link rel="stylesheet" href="/styles/style.css" type="text/css" media="screen" />
<script type="text/javascript" src="/scripts/cudl.js"></script>

<script type="text/javascript">
	// initalise page if function is available.
	function init() {

		// Show or hide cookie notice and call page specific init statement.
		cudl.displayCookieNotice();
		if (typeof pageinit == 'function') {
			pageinit();
		}
	}
</script>
