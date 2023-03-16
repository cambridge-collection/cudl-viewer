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
            <cudl:context-editable-areas>
                <cudl:editable-area id="summaryDiv" filename="${collection.summary}"/>
                <cudl:editable-area id="sponsorDiv" filename="${collection.sponsors}"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="View all collections" collection="${collection}"/>

        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <div class="campl-column7  campl-main-content" id="content">
                    <div id="summaryDiv" class="campl-content-container">
                        <%-- FIXME: Make a custom tag for resolving external HTML of different types w/ collection attribute/param --%>
                        <c:catch var="importException">
                            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.summary}"/>
                        </c:catch>
                        <c:if test="${importException != null}">
                            <!-- No summary. -->
                        </c:if>
                    </div>
                </div>
                <div class="campl-column5 campl-secondary-content ">
                    <div class="pagination toppagination"></div>
                    <!-- start of list -->
                    <div id="collections_carousel"
                        class="collections_carousel campl-related-links"></div>
                    <!-- end of list -->
                    <div class="pagination toppagination"></div>
                </div>

                <div id="sponsorDiv" class="campl-column12 campl-content-container">
                    <c:catch var="importException">
                        <c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.sponsors}" />
                    </c:catch>
                    <c:if test="${importException != null}">
                        <!-- No sponsors. -->
                    </c:if>
                </div>
                <cudl:collection-manifest-button collectionId="${collection.id}"/>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
