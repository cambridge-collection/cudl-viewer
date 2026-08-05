<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<%-- Shares the organisation collections' chunk: both page types load their tiles
     from the same itemJSON endpoint, and both bundles carry the same stylesheet. --%>
<cudl:generic-page pagetype="COLLECTION_ORGANISATION" title="${collection.title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <json:property name="collectionUrl" value="${collection.URL}"/>
            <json:property name="collectionTitle" value="${collection.title}"/>
            <json:property name="collectionTotal" value="${collectionTotal}"/>
            <json:property name="collectionBatchSize" value="${collectionBatchSize}"/>
        </cudl:default-context>
    </jsp:attribute>

        <jsp:body>
            <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="View all collections"/>

            <div class="clear"></div>

            <div id="main_content" class="campl-row campl-content campl-recessed-content">
                <div class="campl-wrap clearfix">

                    <div class="campl-column12  campl-main-content campl-content-container" id="content">
                        <cudl:release-status type="notice" subject="collection"
                                             unreleased="${collection.unreleased}" status="${collection.status}"/>
                        <div id="summaryDiv" class="virtual_collection_summary">
                            <c:catch var="importException">
                                <c:import charEncoding="UTF-8" url="${collectionHTMLURL}/${collection.summary}"/>
                            </c:catch>
                            <c:if test="${importException != null}">
                                <!-- No summary. -->
                            </c:if>
                        </div>
                        <div class="campl-column12 virtual-collections-items">

                            <c:if test="${itemsUnavailable}">
                                <div class="alert alert-warning" role="alert">
                                    <strong>Items temporarily unavailable:</strong> this collection's
                                    items could not be retrieved. Please try again shortly.
                                </div>
                            </c:if>

                            <ol id="virtual_collections_carousel">
                                <%-- Tiles come from Solr (see CollectionViewController), not from
                                     item JSON on disk, so they match the organisation carousel's.
                                     Only the first batch is here; the rest are appended by the
                                     client from the same itemJSON endpoint as it scrolls. --%>
                                <c:forEach items="${items}" var="item" varStatus="loop">

                                    <%-- FIXME: move this inline style into CSS and apply a class here --%>
                                    <c:choose>
                                        <c:when test="${item.thumbnailOrientation == 'portrait'}">
                                            <c:set var="imageDimensions" value="height: 100%"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="imageDimensions" value="width: 100%"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:set var="thumbnailUrl" value="${fn:escapeXml(item.thumbnailURL)}"/>

                                    <li class="campl-column5">
                                        <div class="virtual_collections_carousel_item">
                                            <div class="virtual_collections_carousel_image_box campl-column6">
                                                <div class="virtual_collections_carousel_image" id="virtual_collections_carousel_item${loop.index + 1}">
                                                    <a href="/view/${fn:escapeXml(item.id)}/1">
                                                        <img src="${thumbnailUrl}"
                                                             alt="${fn:escapeXml(item.id)}"
                                                             style="${fn:escapeXml(imageDimensions)}">
                                                    </a>
                                                    <c:if test="${item.mainDisplay == 'rti'}">
                                                        <span class="virtual_collections_carousel-lightbulb-icon">
                                                            <img alt="RTI Item" height="30px" src="/document-views/rti/rti-light-bulb.png"/>
                                                        </span>
                                                    </c:if>
                                                    <cudl:release-status type="badge"
                                                        unreleased="${item.unreleased}" status="${item.itemStatus}"/>
                                                </div>
                                            </div>
                                            <div class='virtual_collections_carousel_text campl-column6'>
                                                <%-- Unescaped: titles carry TEI markup that nothing strips. --%>
                                                <h5>${item.title}<c:if test="${not empty item.shelfLocator}"> (${item.shelfLocator})</c:if></h5>
                                                <c:if test="${not empty item.abstractShort}">${item.abstractShort} &hellip;</c:if>
                                                <a href="/view/${fn:escapeXml(item.id)}/1">more</a>
                                            </div>
                                            <div class='clear'></div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ol>

                            <%-- Watched by the client; scrolling it into view appends the next
                                 batch. Left in place with no Javascript, where it renders as
                                 nothing and the server-rendered first batch stands alone. --%>
                            <div id="virtual_collections_sentinel"></div>
                        </div>

                        <div id="sponsorDiv" class="campl-column12 virtual_collection_sponsor">
                            <c:catch var="importException">
                                <c:import charEncoding="UTF-8" url="${collectionHTMLURL}/${collection.sponsors}"/>
                            </c:catch>
                            <c:if test="${importException != null}">
                                <!-- No sponsors. -->
                            </c:if>
                        </div>
                        <cudl:collection-manifest-button/>
                    </div>
                </div>
            </div>
    </jsp:body>
</cudl:generic-page>
