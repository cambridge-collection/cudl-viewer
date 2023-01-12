<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="LOGIN">
    <cudl:nav activeMenuIndex="${3}" displaySearch="true" title="Login"/>

    <div class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <div class="campl-column12  campl-main-content" id="content">
                <div class="campl-content-container">

                    <c:if test="${not empty error}">
                        <div id="login_error">
                            <c:out value="${error}"/>
                        </div>
                    </c:if>

                    <div class="campl-column12">Login to create or view your
                        collection of bookmarks.</div>
                    <div class="campl-column12">&nbsp;</div>
                    <div class="campl-column12">
                        <div class="campl-column4 campl-login-form">
                            <div class="campl-content-container">
                                <c:set var="nextUrlQuerySegment"
                                       value="${not empty nextUrl ? '&next='.concat(cudlfn:uriEnc(nextUrl)) : ''}"/>
                                <form:form method="POST"
                                    action="/auth/login?type=google${nextUrlQuerySegment}">
                                    <button class="btn btn-block btn-social btn-google" action="submit">
                                        <i class="fa fa-google"></i> Sign in with Google
                                    </button>
                                </form:form>
                                <br />
                                <form:form method="POST"
                                    action="/auth/login?type=linkedin${nextUrlQuerySegment}">
                                    <button class="btn btn-block btn-social btn-linkedin" action="submit">
                                        <i class="fa fa-linkedin"></i> Sign in with LinkedIn
                                    </button>
                                </form:form>
                                <br />
                                <form:form method="POST"
                                    action="/auth/login?type=raven${nextUrlQuerySegment}">
                                    <button class="btn btn-block btn-social btn-raven" action="submit">
                                        <img class="img-raven" src="/img/general/raven.png"/> Sign in with Raven
                                    </button>
                                </form:form>
                            </div>
                        </div>
                    </div>

                    <div class="campl-column12">
                        <br /> <br />
                    </div>
                    <div class="campl-column12 center">

                    </div>
                </div>

                    <div class="campl-column12">
                        <br /> <br />
                    </div>
                    <div class="campl-column12 center">

            </div>
        </div>
    </div>
</cudl:generic-page>
