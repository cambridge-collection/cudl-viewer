<%@tag description="Theme Image Tag" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:eval expression="@uiThemeBean.themeUI" var="themeUI" />

<c:set var = "css_files" value = "${themeUI.css}"/>
<c:forEach items="${css_files}" var="file">
    <link href="${file}" rel="stylesheet">
</c:forEach>

