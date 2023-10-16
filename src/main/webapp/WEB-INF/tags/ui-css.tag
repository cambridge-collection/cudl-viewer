<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${fn:length(name) gt 0}">

    <c:set var = "css_files" value = "${uiThemeBean.getThemeUI().getCss()}"/>
    <c:forEach items="${css_files}" var="file">
        <link href="${file}" rel="stylesheet">
    </c:forEach>
</c:if>
