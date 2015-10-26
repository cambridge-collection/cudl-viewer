<%@tag description="Generate JSON param for Google Analytics tracking code"
	   pageEncoding="UTF-8"
	   trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%-- live's ID --%>
<c:set var="trackingId" value="UA-10976633-3"/>

<c:if test="${fn:length(globalproperties.GoogleAnalyticsId) > 0}">
	<c:set var="trackingId" value="${globalproperties.GoogleAnalyticsId}"/>
</c:if>

<json:property name="gaTrackingId" value="${trackingId}"/>
