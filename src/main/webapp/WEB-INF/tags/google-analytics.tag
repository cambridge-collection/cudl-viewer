<%@tag description="Google Analytics 4 include" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('GA4GoogleAnalyticsId')" var="ga4TrackingId" />

<!-- default to live version of analytics id -->
<c:set var="gid" value="${(empty ga4TrackingId) ? 'G-89DQZCHV21' : ga4TrackingId}" />

<script async src="https://www.googletagmanager.com/gtag/js?id=${gid}"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', '${gid}');
</script>
