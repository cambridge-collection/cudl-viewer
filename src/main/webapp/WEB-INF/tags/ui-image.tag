<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="name" required="true" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${fn:length(name) gt 0}">
    <c:set var = "img" value = "${get_from_json('images', 'logo')}"/>
    <a href="${img.href}" class="campl-main-logo">
        <img alt="${img.alt}" src="${img.src}" />
    </a>
</c:if>
