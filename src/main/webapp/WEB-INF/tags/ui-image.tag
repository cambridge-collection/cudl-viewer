<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="name" required="true" type="java.lang.String" %>
<%@attribute name="class" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${fn:length(name) gt 0}">
    <c:set var = "img" value = "${get_image('logo')}"/>
    <c:choose>
        <c:when test="${fn:length(obj.href) gt 0}">
            <a href="${img.href}" class="${class}">
                <img alt="${img.alt}" src="${img.src}"/>
            </a>
        </c:when>
        <c:otherwise>
            <img alt="${img.alt}" src="${img.src}" class="${class}"/>
        </c:otherwise>
    </c:choose>
</c:if>
