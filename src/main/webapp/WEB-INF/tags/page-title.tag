<%@tag description="Add page title" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="additionalClasses" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="defaultTitle" value="Cambridge Digital Library"/>

<div class="pagetitle ${additionalClasses}">
    <h2>${(not empty title) ? title : defaultTitle}</h2>
</div>
