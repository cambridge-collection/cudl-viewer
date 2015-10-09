<%@tag description="CUDL head content tags" pageEncoding="UTF-8"%>
<%@attribute name="pagetype" required="true" type="java.lang.String"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<%--  webmaster tools --%>
<meta name="google-site-verification"
	content="asuuZDOIgJTwOZ0EfnrFe1hQ2A7q6uukh5ebEsciiNg" />
<meta name="msvalidate.01" content="EF9291052C3CCD9E1AC12147B69C52C2" />

<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<%-- TODO: Async load UI's js and css --%>

<%-- load Project Light Font. This is not loaded with the webpack build as
	 the fonts are external. Could use this in the future via the UI's js:
	 https://github.com/typekit/webfontloader --%>
<script type="text/javascript" src="//use.typekit.com/hyb5bko.js"></script>
<script type="text/javascript">try { Typekit.load(); } catch (e) {}</script>

<cudl:legacy-ui-resources/>
