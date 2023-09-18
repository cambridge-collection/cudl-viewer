<%@tag description="Google Analytics 4 include" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('GA4GoogleAnalyticsId')" var="ga4TrackingId" />

<!-- default to live version of analytics id -->
<c:set var="gid" value="${(empty ga4TrackingId) ? 'G-89DQZCHV21' : ga4TrackingId}" />

<script async src="https://www.googletagmanager.com/gtag/js?id=${gid}"></script>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.1.1/cookieconsent.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.1.1/cookieconsent.min.js" data-cfasync="false"></script>
<script>
    window.addEventListener('load', function(){
        window.cookieconsent.initialise({
            revokeBtn: '<div class="cc-revoke"></div>',
            type: "opt-in",
            position: "top",
            theme: "classic",
            palette: {
                popup: {
                    background: "#ccc",
                    text: "#444"
                },
                button: {
                    background: "#8c6",
                    text: "#fff"
                }
            },
            content: {
                message: "This website uses cookies.",
                link: "Privacy Statement",
                href: "/help#cookies"
            },
            onInitialise: function(status) {
                if(status == cookieconsent.status.allow) site_cookies();
            },
            onStatusChange: function(status) {
                if (this.hasConsented()) site_cookies();
            }
        })
    });

    function site_cookies() {
        window.dataLayer = window.dataLayer || [];
        function gtag() {
            dataLayer.push(arguments);
        }
        gtag('js', new Date());
        gtag('config', '${gid}');
    }
</script>
