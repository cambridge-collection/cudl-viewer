<%@page autoFlush="true" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<!--Links to a temporary directory created just for testing -->

    <link type="text/css" href="${pageContext.request.contextPath}/bootstrap5-test-css/bootstrap.min.css" rel="stylesheet">
    <link  type="text/css" href="${pageContext.request.contextPath}/bootstrap5-test-css/all%20collections-test.css" rel="stylesheet">
    <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap5-test-css/bootstrap.bundle.min.js"></script>



<c:set var="pagetitle" value="View all collections"/>

<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="${pagetitle}"/>

        <div id="main_content" class="container bg-white border">
            <div class="campl-wrap clearfix">
                <div class="container bg-white border" id="content">
                    <div id="collectionsDiv">
                        <cudl:page-title title="${pagetitle}"/>
                        <c:forEach items="${collections}" var="c">
                            <c:if test="${empty c.parentCollectionId}">

                                <div class="row row-cols-1 row-cols-md-2 row-cols-xl-3 g-3">
                                    <div class="col">
                                        <a href="${fn:escapeXml(c.URL)}" class="collection campl-teaser-img-link">
                                            <div class="card my-2 mx-2 text-white border-0 px-0 py-0">
                                                <div class="row g-0">
                                                    <div class="col-5">

                                                            <img alt=""
                                                                src="/images/collectionsView/collection-${cudlfn:uriEnc(c.id)}.jpg"
                                                                class="img-fluid"/>

                                                    </div>
                                                    <div class="col-7">
                                                        <div class="card-body">
                                                            <h3 class="card-title">
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

