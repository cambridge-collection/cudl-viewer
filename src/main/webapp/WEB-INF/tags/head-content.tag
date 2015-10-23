<%@tag description="CUDL head content tags" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="pagetype" required="true" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudl-frontend" uri="/WEB-INF/cudl-frontend.tld" %>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<%--  webmaster tools --%>
<meta name="google-site-verification"
	content="asuuZDOIgJTwOZ0EfnrFe1hQ2A7q6uukh5ebEsciiNg" />
<meta name="msvalidate.01" content="EF9291052C3CCD9E1AC12147B69C52C2" />

<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<%-- load Project Light Font. This is not loaded with the webpack build as
	 the fonts are external. Could do async load in future. --%>
<script type="text/javascript" src="//use.typekit.com/hyb5bko.js"></script>
<script type="text/javascript">try { Typekit.load(); } catch (e) {}</script>

<%-- Tell our webpack bundle about the path we serve it from. This is required
     as we don't hard-code this in the webpack build. --%>
<script type="text/javascript">__cudl_webpack_public_path__ = '/ui/';</script>

<cudl-frontend:fontend-resources pagetype="${pagetype}"/>
