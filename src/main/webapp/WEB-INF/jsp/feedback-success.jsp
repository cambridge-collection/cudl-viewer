<%@page autoFlush="true" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@environment.getProperty('default.title')" var="defaultTitle" />
<c:set var="defaultTitle" value="${defaultTitle}"/>

<cudl:base-page title="${defaultTitle} - Feedback"
                    pagetype="FEEDBACK">
    <div class="campl-row campl-content">
        <div class="campl-content-container">
            <h3>Feedback</h3>

            <p>Thank you for your feedback.</p>
        </div>
    </div>
</cudl:base-page>
