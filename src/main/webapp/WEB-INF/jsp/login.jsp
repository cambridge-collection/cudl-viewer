<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


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
                                <form:form class="openid_form" method="POST"
                                    action="/auth/login?type=google">
                                    <a class="btn btn-block btn-social btn-google" href="/auth/oauth2/google"> <i
                                        class="fa fa-google"></i> Sign In with Google
                                    </a>
                                    <button action="submit">Sign in with Google</button>
                                </form:form>
                                <br />
                                <form:form class="openid_form" method="POST"
                                    action="/auth/login?type=facebook">
                                    <a class="btn btn-block btn-social btn-facebook" href="/auth/oauth2/facebook"> <i
                                        class="fa fa-facebook"></i> Sign In with Facebook
                                    </a>
                                    <button action="submit">Sign in with Facebook</button>
                                </form:form>
                                <br />
                                <form:form class="openid_form" method="POST"
                                    action="/auth/login?type=linkedin">
                                    <a class="btn btn-block btn-social btn-linkedin" href="/auth/oauth2/linkedin"> <i
                                        class="fa fa-linkedin"></i> Sign In with LinkedIn
                                    </a>
                                    <button action="submit">Sign in with LinkedIn</button>
                                </form:form>
                                <br />
                                <form:form class="openid_form" method="POST"
                                    action="/auth/login?type=raven">
                                    <a class="btn btn-block btn-social btn-raven" href="/raven/"> <img class="img-raven" src="/img/general/raven.png" /> Sign In with Raven
                                    </a>
                                    <button action="submit">Sign in with Raven</button>
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
