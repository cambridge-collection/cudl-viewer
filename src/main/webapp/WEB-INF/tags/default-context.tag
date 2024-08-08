<%@tag description="Generate the default page context data"
       pageEncoding="UTF-8"
       trimDirectiveWhitespaces="true" %>

<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<%-- We don't want to escape the output of json:object because it
     goes into the page via cudl:attr which escapes the value. --%>
<json:object escapeXml="false">
    <jsp:doBody/>
</json:object>
