<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="COLLECTION_ORGANISATION" title="${collection.title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <%--<json:property name="collectionCookieName" value="${collection.id}_pageNum"/>--%>
            <json:property name="collectionSize" value="${fn:length(collection.itemIds)}"/>
            <json:property name="collectionUrl" value="${collection.URL}"/>
            <json:property name="collectionTitle" value="${collection.title}"/>
            <json:property name="collectionPage" value="${pageNumber}"/>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="View all collections" collection="${collection}"/>

        <c:if test="${not empty unreleasedItemIds}">
            <script type="application/json" id="unreleased-item-ids">
                [<c:forEach items="${unreleasedItemIds}" var="uid" varStatus="s">"${fn:escapeXml(uid)}"<c:if test="${!s.last}">,</c:if></c:forEach>]
            </script>
            <script>
            // DOMContentLoaded ensures the collections_carousel element exists before
            // we attach the observer; the items themselves arrive later via AJAX.
            document.addEventListener('DOMContentLoaded', function() {
                var ids = JSON.parse(document.getElementById('unreleased-item-ids').textContent);
                var unreleasedSet = new Set(ids);
                var carousel = document.getElementById('collections_carousel');
                if (!carousel || unreleasedSet.size === 0) return;
                new MutationObserver(function(mutations) {
                    mutations.forEach(function(m) {
                        m.addedNodes.forEach(function(node) {
                            if (node.nodeType !== 1) return;
                            var link = node.querySelector('a[href^="/view/"]');
                            if (!link) return;
                            var itemId = link.getAttribute('href').replace(/^\/view\//, '').replace(/\/.*$/, '');
                            if (unreleasedSet.has(itemId)) {
                                var badge = document.createElement('span');
                                badge.className = 'badge bg-warning text-dark';
                                badge.textContent = 'Unreleased';
                                badge.style.cssText = 'position:absolute;top:4px;left:4px;z-index:1';
                                var imgBox = node.querySelector('.collections_carousel_image_box');
                                if (imgBox) { imgBox.style.position = 'relative'; imgBox.appendChild(badge); }
                            }
                        });
                    });
                }).observe(carousel, { childList: true });
            });
            </script>
        </c:if>

        <div id="main_content" class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <div class="campl-column7  campl-main-content" id="content">
                    <div id="summaryDiv" class="campl-content-container">
                        <c:if test="${collection.unreleased}">
                            <div class="alert alert-warning" role="alert">
                                <strong>Unreleased content:</strong> This collection is not yet publicly available.
                            </div>
                        </c:if>
                        <%-- FIXME: Make a custom tag for resolving external HTML of different types w/ collection attribute/param --%>
                        <c:catch var="importException">
                            <c:import charEncoding="UTF-8" url="${collectionHTMLURL}/${collection.summary}"/>
                        </c:catch>
                        <c:if test="${importException != null}">
                            <!-- No summary. -->
                        </c:if>
                    </div>
                </div>
                <div class="campl-column5 campl-secondary-content ">
                    <div id="topPagination" class="pagination"></div>
                    <!-- start of list -->
                    <div id="collections_carousel"
                        class="collections_carousel campl-related-links"></div>
                    <!-- end of list -->
                    <div id="bottomPagination" class="pagination"></div>
                </div>

                <div id="sponsorDiv" class="campl-column12 campl-content-container">
                    <c:catch var="importException">
                        <c:import charEncoding="UTF-8" url="${collectionHTMLURL}/${collection.sponsors}" />
                    </c:catch>
                    <c:if test="${importException != null}">
                        <!-- No sponsors. -->
                    </c:if>
                </div>
                <cudl:collection-manifest-button/>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
