<%@page autoFlush="true" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="Browse our collections"/>

        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <div class="campl-column12  campl-main-content" id="content">
                    <div id="collectionsDiv">
                        <h2>Browse our collections</h2>
                        <c:forEach items="${collections}" var="c">
                            <c:if test="${empty c.parentCollectionId}">
                                <div class="campl-column4">
                                    <div class="campl-content-container campl-side-padding">
                                        <div class="campl-horizontal-teaser campl-teaser clearfix campl-focus-teaser">
                                            <div class="campl-focus-teaser-img">
                                                <div class="campl-content-container campl-horizontal-teaser-img">
                                                    <a href="${fn:escapeXml(c.URL)}" class="campl-teaser-img-link"><img
                                                        alt=""
                                                        src="/images/collectionsView/collection-${cudlfn:uriEnc(c.id)}.jpg"
                                                        class="campl-scale-with-grid"/></a>
                                                </div>
                                            </div>
                                            <div class="campl-focus-teaser-txt">
                                                <div class="campl-content-container campl-horizontal-teaser-txt">
                                                    <h3 class="campl-teaser-title">
                                                        <a href="${fn:escapeXml(c.URL)}"><c:out value="${c.title}"/></a>
                                                    </h3>
                                                    <a href="${fn:escapeXml(c.URL)}" class="ir campl-focus-link">Read
                                                        more</a>
                                                </div>
                                            </div>
                                        </div>
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
