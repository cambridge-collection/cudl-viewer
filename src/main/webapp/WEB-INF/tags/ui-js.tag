<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${fn:length(name) gt 0}">
    <c:set var = "js_files" value = "${get_from_json('js')}"/>
    <c:forEach items="${js_files}" var="file">
        <script src="${file}" type="text/javascript"/>
    </c:forEach>
</c:if>
