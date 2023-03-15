<%@page autoFlush="true" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="pagetitle" value="View all collections"/>

<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="${pagetitle}"/>

        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <div class="campl-column12  campl-main-content" id="content">
                    <div id="collectionsDiv">
                        <cudl:page-title title="${pagetitle}"/>
                        <c:forEach items="${collections}" var="c">
                            <c:if test="${empty c.parentCollectionId}">
                                <div class="campl-column4">
                                    <div class="campl-content-container campl-side-padding">
                                        <a href="${fn:escapeXml(c.URL)}" class="collection campl-teaser-img-link">
                                            <div class="campl-horizontal-teaser campl-teaser clearfix campl-focus-teaser table">
                                                <div class="tr">
                                                    <div class="td campl-focus-teaser-img">
                                                        <div class="campl-content-container campl-horizontal-teaser-img">
                                                            <img alt=""
                                                                src="/images/collectionsView/collection-${cudlfn:uriEnc(c.id)}.jpg"
                                                                class="campl-scale-with-grid"/>
                                                        </div>
                                                    </div>
                                                    <div class="td campl-focus-teaser-txt">
                                                        <div class="campl-content-container campl-horizontal-teaser-txt">
                                                            <h3 class="campl-teaser-title">
                                                                <c:out value="${c.title}"/>
                                                            </h3>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
