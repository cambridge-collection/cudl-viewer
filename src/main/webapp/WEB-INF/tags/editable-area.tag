<%@tag description="Generate an editable area definition. This should be used in the body of <cudl:context-editable-areas>"
       pageEncoding="UTF-8"
       trimDirectiveWhitespaces="true" %>

<%@attribute name="id" required="true" type="java.lang.String" %>
<%@attribute name="filename" required="true" type="java.lang.String" %>

<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>


<json:object>
    <json:property name="id" value="${id}"/>
    <json:property name="filename" value="${filename}"/>
</json:object>
