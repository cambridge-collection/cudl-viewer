<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!--  Google Analytics -->

<script>
  var googleAnalyticsID = "UA-10976633-3"; // default to live. 

  if ("${globalproperties.GoogleAnalyticsId}" != "") {
	googleAnalyticsID = "${globalproperties.GoogleAnalyticsId}";
  };

  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', googleAnalyticsID, 'auto');
  ga('send', 'pageview');
</script>
