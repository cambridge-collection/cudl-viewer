<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="STANDARD" title="${collection.title}">

        <jsp:body>
            <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="View all collections"/>

            <div class="clear"></div>

            <div id="main_content" class="campl-row campl-content campl-recessed-content">
                <div class="campl-wrap clearfix">

                    <div class="campl-column12  campl-main-content campl-content-container" id="content">
                        <div id="summaryDiv" class="virtual_collection_summary">
                            <c:catch var="importException">
                                <c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.summary}"/>
                            </c:catch>
                            <c:if test="${importException != null}">
                                <!-- No summary. -->
                            </c:if>
                        </div>
                        <div class="campl-column12 virtual-collections-items">

                            <ol id="virtual_collections_carousel">
                                <c:forEach items="${collection.itemIds}" var="id" varStatus="loop">
                                    <c:set var="item" value="${cudlfn:getItem(itemDAO, id)}"/>

                                    <%-- FIXME: move this inline style into CSS and apply a class here --%>
                                    <c:choose>
                                        <c:when test="${item.thumbnailOrientation == 'portrait'}">
                                            <c:set var="imageDimensions" value="height: 100%"/>
                                            <c:set var="thumbnailUrl" value="${fn:escapeXml(item.thumbnailURL)}"/>
                                        </c:when>
                                        <c:when test="${item.thumbnailOrientation == 'landscape'}">
                                            <c:set var="imageDimensions" value="width: 100%"/>
                                            <c:set var="thumbnailUrl" value="${fn:escapeXml(item.thumbnailURL)}"/>
                                        </c:when>
                                    </c:choose>

                                    <li class="campl-column5">
                                        <div class="virtual_collections_carousel_item">
                                            <div class="virtual_collections_carousel_image_box campl-column6">
                                                <div class="virtual_collections_carousel_image" id="virtual_collections_carousel_item${loop.index + 1}">
                                                    <a href="/view/${fn:escapeXml(item.id)}/1">
                                                        <img src="${thumbnailUrl}"
                                                             alt="${fn:escapeXml(item.id)}"
                                                             style="${fn:escapeXml(imageDimensions)}">
                                                    </a>
                                                </div>
                                            </div>
                                            <div class='virtual_collections_carousel_text campl-column6'>
                                                <h5><c:out value="${item.title} (${item.shelfLocator})"/></h5>
                                                <c:out value="${item.abstractShort}"/> &hellip;
                                                <a href="/view/${fn:escapeXml(item.id)}/1">more</a>
                                            </div>
                                            <div class='clear'></div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ol>
                        </div>

                        <div id="sponsorDiv" class="campl-column12 virtual_collection_sponsor">
                            <c:catch var="importException">
                                <c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.sponsors}"/>
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
