<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@uiThemeBean.themeUI" var="themeUI" />
<c:set var = "js_files" value = "${themeUI.getJs()}"/>

<!-- This tag is not currently used -->
<c:forEach items="${js_files}" var="file">
    <script src="${file}" type="text/javascript"/>
</c:forEach>

