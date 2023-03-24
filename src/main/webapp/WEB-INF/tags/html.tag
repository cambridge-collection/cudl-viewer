<%@tag description="Base HTML Page" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="title" required="true" type="java.lang.String" %>
<%@attribute name="bodyAttrs" required="false" fragment="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html>
<head itemscope itemtype="http://schema.org/Article">
    <title><c:out value="${title}"/></title>
    <jsp:invoke fragment="head"/>
</head>
<body<jsp:invoke fragment="bodyAttrs"/>>
  <a href="#main_content" class="campl-skipTo">skip to content</a>
  <jsp:doBody/>
</body>
</html>
