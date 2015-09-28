<%@tag description="Wrap content in a div with IE version classes" pageEncoding="UTF-8"%>

<!--[if lt IE 7]>
<div class="lt-ie9 lt-ie8 lt-ie7">
<![endif]-->
<!--[if IE 7]>
<div class="lt-ie9 lt-ie8">
<![endif]-->
<!--[if IE 8]>
<div class="lt-ie9">
<![endif]-->

<!-- Pass through body -->
<jsp:doBody/>

<!--[if IE 8]>
</div>
<![endif]-->
