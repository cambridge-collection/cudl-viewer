<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="summaryDiv" filename="${collection.summary}"/>
                <cudl:editable-area id="sponsorDiv" filename="${collection.sponsors}"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="View all collections"/>

        <div id="main_content" class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <!-- side nav -->
                <div class="campl-column3">
                    <div class="campl-tertiary-navigation">
                        <div class="campl-tertiary-navigation-structure">
                            <ul class="campl-unstyled-list campl-vertical-breadcrumb">
                                <li><a href="/">Cambridge Digital Library<span
                                        class="campl-vertical-breadcrumb-indicator"></span></a></li>
                            </ul>
                            <ul class="campl-unstyled-list campl-vertical-breadcrumb-navigation">
                                <li class="campl-selected"><a href="${fn:escapeXml(collection.URL)}"><c:out value="${collection.title}"/></a>
                                    <ul class='campl-unstyled-list campl-vertical-breadcrumb-children'>

                                        <c:forEach items="${subCollections}" var="c">
                                            <li><a href="${fn:escapeXml(c.URL)}"><c:out value="${c.title}"/></a></li>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <div id="summaryDiv">
                    <c:catch var="importException">
                        <c:import charEncoding="UTF-8" url="${contentHTMLURL}/${collection.summary}"/>
                    </c:catch>
                    <c:if test="${importException != null}">
                        <!-- No summary. -->
                    </c:if>
                </div>

                <div id="sponsorDiv" class="campl-column12 campl-content-container">
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
    </jsp:body>
</cudl:generic-page>
