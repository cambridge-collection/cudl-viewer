<%@tag description="Base HTML Page" pageEncoding="UTF-8"%>
<%@attribute name="head" fragment="true"%>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@attribute name="bodyClass" required="false"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html>
<head>
    <title><c:out value="${title}"/></title>
    <jsp:invoke fragment="head"/>
</head>
<body class="${fn:escapeXml(bodyClass)}">
  <jsp:doBody/>
</body>
</html>