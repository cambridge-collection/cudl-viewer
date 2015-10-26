<%@tag description="Generate the default page context data"
	   pageEncoding="UTF-8"
	   trimDirectiveWhitespaces="true" %>

<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<json:object>
	<cudl:context-ga-tracking-code/>
	<jsp:doBody/>
</json:object>
