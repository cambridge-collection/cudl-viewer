<%@page isErrorPage="true" autoFlush="true" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@uiThemeBean.themeUI.title" var="defaultTitle" />
<c:set var="defaultTitle" value="${defaultTitle}"/>

<cudl:generic-page pagetype="STANDARD">
    <cudl:nav activeMenuIndex="${0}" displaySearch="true" title="Page not found"/>

    <div id="main_content" class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <div class="campl-column9  campl-main-content" id="content">
                <div class="campl-content-container">

                    <div class="panel light">

                        <div class="grid_11">

                            <h4>Page Not Found.</h4>
                            <p>
                                Sorry, the page you requested has not been found. Please <a
                                    href="mailto:dl-feedback@lib.cam.ac.uk"
                                    title="404 Page - Resource Missing">Contact Us</a> so we can
                                resolve the issue or click on the link below to go to our home
                                page. <br /> <br /> <a href="/">${defaultTitle}</a>.
                            </p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</cudl:generic-page>
