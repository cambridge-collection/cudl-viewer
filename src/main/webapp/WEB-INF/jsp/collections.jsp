<%@page autoFlush="true" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="pagetitle" value="View all collections"/>

<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="${pagetitle}"/>



                <div class="container" id="content">
                    <div id="collectionsDiv">
                        <cudl:page-title title="${pagetitle}"/>
                        <div class="row row-cols-1 row-cols-md-2 row-cols-xl-3 g-3">
                            <c:forEach items="${collections}" var="c">
                              <c:if test="${empty c.parentCollectionId}">

                                    <div class="col">
                                        <a href="${fn:escapeXml(c.URL)}">
                                            <div class="card my-2 mx-2 text-white border-0 px-0 py-0">
                                                <div class="row g-0">
                                                    <div class="col-5">

                                                            <img alt=""
                                                                src="/images/collectionsView/collection-${cudlfn:uriEnc(c.id)}.jpg"
                                                                class="img-link img-fluid"/>

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
                              </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </div>


    </jsp:body>
</cudl:generic-page>
