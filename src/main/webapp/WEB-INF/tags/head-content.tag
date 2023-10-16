<%@tag description="CUDL head content tags" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="pagetype" required="true" type="java.lang.String" %>
<%@attribute name="metaTags" required="false" fragment="true" %>
<%@attribute name="viewport" required="false" type="java.lang.String" %>
<%@attribute name="metaDescription" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudl-frontend" uri="/WEB-INF/cudl-frontend.tld" %>


<cudl:meta httpEquiv="Content-Type" content="text/html; charset=utf-8" />
<cudl:meta httpEquiv="X-UA-Compatible" content="IE=edge"/>

<c:if test="${not empty metaDescription}">
    <cudl:meta name="description" content="${metaDescription}"/>
</c:if>

<%--  webmaster tools --%>
<cudl:meta name="google-site-verification"
    content="asuuZDOIgJTwOZ0EfnrFe1hQ2A7q6uukh5ebEsciiNg" />
<cudl:meta name="msvalidate.01" content="EF9291052C3CCD9E1AC12147B69C52C2"/>

<cudl:meta name="viewport" content="${(empty viewport) ? \"width=device-width, initial-scale=1.0, minimum-scale=1\" : viewport}"/>

<jsp:invoke fragment="metaTags"/>

<cudl:google-analytics />

<%-- load Project Light Font. This is not loaded with the webpack build as
     the fonts are external. Could do async load in future. --%>
<%--<script type="text/javascript" src="//use.typekit.com/hyb5bko.js"></script>--%>
<%--<script type="text/javascript">try { Typekit.load(); } catch (e) {}</script>--%>

<%-- Tell our webpack bundle about the path we serve it from. This is required
     as we don't hard-code this in the webpack build. --%>
<script type="text/javascript">__cudl_webpack_public_path__ = '/ui/';</script>

<cudl-frontend:fontend-resources pagetype="${pagetype}"/>

<%--<cudl:ui-js/>--%>
<cudl:ui-css/>

<jsp:doBody/>
