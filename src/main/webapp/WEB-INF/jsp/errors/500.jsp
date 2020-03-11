<%@page isErrorPage="true" autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="errorEmailSubject" value="500 Page - Error"/>

<cudl:generic-page pagetype="ERROR_500" title="Error">
    <cudl:nav activeMenuIndex="${0}" displaySearch="true" title="Error"/>

    <div class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <div class="campl-column9  campl-main-content" id="content">
                <div class="campl-content-container">
                    <div class="panel light">
                        <div class="grid_11">
                            <h2>Error occurred</h2>
                            <p>
                                Sorry, there has been an error displaying the page you requested.
                                Please <a href="mailto:dl-support@lib.cam.ac.uk?subject=${cudlfn:uriEnc(errorEmailSubject)}"
                                    title="Email us">Contact Us</a> so we can resolve the
                                issue.
                            </p>

                            <p>
                                Use the navigation above or or <a href="/">return to the home page</a>.
                            </p>

                            <h3>Details</h3>
                            <p>
                                <dl>
                                    <dt>Message</dt>
                                    <dd>${exception.message}</dd>
                                </dl>
                            </p>

                            <p>
                                <a href="#stacktrace" data-toggle="collapse"
                                    class="toggle-error-traceback campl-primary-cta"
                                    aria-expanded="false"
                                    aria-controls="#stacktrace">More</a>
                            </p>

                            <p>
                                <pre id="stacktrace" class="stacktrace collapse">${pageContext.out.flush();exception.printStackTrace(pageContext.response.writer)}</pre>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</cudl:generic-page>
