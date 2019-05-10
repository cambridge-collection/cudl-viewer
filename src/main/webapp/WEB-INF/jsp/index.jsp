<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="pagetype" value="STANDARD"/>
<c:set var="metaDescription">
    A home for the discovery of digitised material and research
    outputs from the University of Cambridge and beyond
</c:set>

<cudl:generic-page pagetype="${pagetype}" title="${collection.title}">

    <jsp:attribute name="head">
        <cudl:head-content pagetype="${pagetype}" metaDescription="${metaDescription}"/>
    </jsp:attribute>

    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="latestNewsDiv" filename="index-latest-news.html"/>
                <cudl:editable-area id="index-carousel-1" filename="index-carousel-1.html"/>
                <cudl:editable-area id="index-carousel-2" filename="index-carousel-2.html"/>
                <cudl:editable-area id="index-carousel-3" filename="index-carousel-3.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${0}" displaySearch="true"/>

        <c:if test="${not empty downtimeWarning}">
            <c:out value="${downtimeWarning}" escapeXml="false"/>
        </c:if>

        <div class="campl-row campl-page-header" id="content">
            <div class="campl-wrap clearfix">
                <div class="campl-column12">
                    <div class="campl-recessed-carousel">
                        <div class="section-carousel campl-carousel clearfix campl-banner">
                            <div class="campl-carousel-container">

                                <ul class="campl-unstyled-list campl-slides">
                                    <li class="campl-slide campl-column12">
                                        <div id="index-carousel-1" style="position:relative">
                                            <c:import charEncoding="UTF-8"
                                                url="${contentHTMLURL}/index-carousel-1.html" />
                                        </div>
                                    </li>
                                    <li class="campl-slide campl-column12">
                                        <div id="index-carousel-2" style="position:relative">
                                            <c:import charEncoding="UTF-8"
                                                url="${contentHTMLURL}/index-carousel-2.html" />
                                        </div>
                                    </li>
                                    <li class="campl-slide campl-column12">
                                        <div id="index-carousel-3" style="position:relative">
                                            <c:import charEncoding="UTF-8"
                                                url="${contentHTMLURL}/index-carousel-3.html" />
                                        </div>
                                    </li>
                                </ul>

                            </div>
                        </div>
                    </div>

                    <div class="campl-column4">&nbsp;</div>
                </div>
            </div>
        </div>
        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">

                <div class="campl-column12">
                    <div class="campl-side-padding ">
                        <h2>Collections</h2>
                    </div>
                </div>
                <div class="campl-column12 campl-main-content">

                    <c:forEach items="${rootCollections}" var="c">
                        <div class="campl-column4">
                            <div class="campl-content-container campl-side-padding">
                                <div class="campl-horizontal-teaser campl-teaser clearfix campl-focus-teaser">
                                    <div class="campl-focus-teaser-img">
                                        <div class="campl-content-container campl-horizontal-teaser-img">
                                            <a href="${fn:escapeXml(c.URL)}" class="campl-teaser-img-link"><img
                                                alt=""
                                                src="/images/collectionsView/collection-${cudlfn:uriEnc(c.id)}.jpg"
                                                class="campl-scale-with-grid" /></a>
                                        </div>
                                    </div>
                                    <div class="campl-focus-teaser-txt">
                                        <div class="campl-content-container campl-horizontal-teaser-txt">
                                            <h3 class='campl-teaser-title'>
                                                <a href="${fn:escapeXml(c.URL)}"><c:out value="${c.title}"/></a>
                                            </h3>
                                            <a href="${fn:escapeXml(c.URL)}" class="ir campl-focus-link">Read
                                                more</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="campl-column4">
                    <!-- twitter feed -->
                    <a class="twitter-timeline" href="https://twitter.com/CamDigLib"
                        data-widget-id="309321526665154560">Tweets by @CamDigLib</a>
                    <script>
                        !function(d, s, id) {
                            var js, fjs = d.getElementsByTagName(s)[0];
                            if (!d.getElementById(id)) {
                                js = d.createElement(s);
                                js.id = id;
                                js.src = "//platform.twitter.com/widgets.js";
                                fjs.parentNode.insertBefore(js, fjs);
                            }
                        }(document, "script", "twitter-wjs");
                    </script>
                    <!-- end of twitter feed -->
                </div>

                <div class="campl-column4">
                    <div id="latestNewsDiv">
                        <c:import charEncoding="UTF-8" url="${contentHTMLURL}/index-latest-news.html" />
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
