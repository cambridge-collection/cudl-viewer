<%@tag description="Generate the default page context data"
       pageEncoding="UTF-8"
       trimDirectiveWhitespaces="true" %>

<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<%-- Every page carries the unreleased-content flag so the client-side tile and
     result renderers can gate their badges on it. Read as a Boolean and not as a
     String: json:property renders a String as a JSON string, and "false" is
     truthy in Javascript, which would leave the badges permanently on. --%>
<spring:eval var="showUnreleasedContent"
             expression="@environment.getProperty('showUnreleasedContent', T(java.lang.Boolean), false)"/>

<%-- We don't want to escape the output of json:object because it
     goes into the page via cudl:attr which escapes the value. --%>
<json:object escapeXml="false">
    <json:property name="showUnreleasedContent" value="${showUnreleasedContent}"/>
    <jsp:doBody/>
</json:object>
