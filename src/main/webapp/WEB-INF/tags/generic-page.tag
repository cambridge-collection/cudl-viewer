<%@tag description="The base for the majority of CUDL pages" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="pagetype" required="false" type="java.lang.String" %>
<%@attribute name="head" required="false" fragment="true" %>
<%@attribute name="bodyAttrs" required="false" fragment="true" %>

<%-- A string containing JSON encoded data. It should start with '{' or '['. --%>
<%@attribute name="pageData" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:base-page title="${title}" pagetype="${pagetype}" bodyAttrs="${bodyAttrs}"
                pageData="${pageData}" head="${head}">
    <cudl:ie-div>

        <%-- Pass through body --%>
        <jsp:doBody/>

        <cudl:cookie-notice/>
        <cudl:footer/>
    </cudl:ie-div>
</cudl:base-page>
