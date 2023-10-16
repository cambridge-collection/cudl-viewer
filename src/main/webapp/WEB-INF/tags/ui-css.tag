<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="name" required="true" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${fn:length(name) gt 0}">
    <c:set var = "css_files" value = "${get_from_json('css')}"/>
    <c:forEach items="${css_files}" var="file">
        <link href="${file}" rel="stylesheet">
    </c:forEach>
</c:if>
