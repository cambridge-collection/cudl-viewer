<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<cudl:base-page title="Manchester Digital Library - Feedback"
                    pagetype="FEEDBACK">

    <div class="campl-row campl-content">
        <div class="campl-content-container">
            <form:form method="post" commandName="feedbackForm">
                <fieldset>
                    <legend>Feedback</legend>

                    <p>
                        We welcome all comments, feedback and suggestions. Please
                        tell us what you liked, disliked, would like to see improved, or if
                        you think something is not right.
                    </p>

                    <div class="campl-control-group <form:errors path="name">campl-error</form:errors>">
                        <form:label path="name">Name</form:label>
                        <div class="campl-controls">
                            <form:input path="name" type="text" />
                            <form:errors path="name" cssClass="campl-help-block" />
                        </div>
                    </div>

                    <div class="campl-control-group <form:errors path="email">campl-error</form:errors>">
                        <form:label path="name">Email</form:label>
                        <div class="campl-controls">
                            <form:input path="email" type="text" />
                            <form:errors path="email" cssClass="campl-help-block" />
                        </div>
                    </div>

                    <div class="campl-control-group <form:errors path="comment">campl-error</form:errors>">
                        <form:label path="comment">Your comment</form:label>
                        <div class="campl-controls">
                            <form:textarea path="comment" rows="3"/>
                            <form:errors path="comment" cssClass="campl-help-block"/>
                        </div>
                    </div>
                </fieldset>

                <fieldset>
                    <legend>Are you human?</legend>
                    <div class="campl-control-group">
                        <c:out value="${captchaHtml}" escapeXml="false"/>
                    </div>

                    <%-- This is effectively the recaptcha error area as the
                         only object errors generated are for recaptcha --%>
                    <form:errors>
                        <div class="campl-control-group campl-error">
                            <label><form:errors/></label>
                        </div>
                    </form:errors>
                </fieldset>

                <c:if test="${submissionFailed}">
                    <div class="campl-notifications-panel campl-warning-panel campl-notifications-container clearfix">
                        <div class="campl-column4">
                            <p class="campl-notifications-icon campl-warning-icon">Error:</p>
                        </div>
                        <div class="campl-column8">
                            <p>
                                Sorry there was a problem submitting your feedback, we'll be looking into it as soon as we can.
                                Please email your comments directly to the team at <a href="mailto:dl-feedback@lib.cam.ac.uk?body=${cudlfn:uriEnc(feedbackForm.comment)}">dl-feedback@lib.cam.ac.uk</a>.
                            </p>
                        </div>
                    </div>
                </c:if>

                <div class="campl-form-actions">
                    <button class="campl-btn campl-primary-cta" type="submit">Send Feedback</button>
                </div>
            </form:form>
        </div>
    </div>
</cudl:base-page>
