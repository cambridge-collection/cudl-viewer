<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:generic-page pagetype="STANDARD" title="${organisationalCollection.title}">
    <cudl:nav activeMenuIndex="${1}" displaySearch="true"
              title="${organisationalCollection.title}"/>
    
    <div class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <div class="campl-column12  campl-main-content" id="content">
    
                <div class="campl-column8">
                    <div class="campl-content-container">
                        <div class="campl-column10">
                            <c:if test="${parentCollection != null}">
                                <a href="${fn:escapeXml(parentCollection.URL)}">
                                    <c:out value="${parentCollection.title}"/>
                                </a>
                                &gt;
                            </c:if>

                            <a href="${fn:escapeXml(organisationalCollection.URL)}">
                                <c:out value="${organisationalCollection.title}"/>
                            </a>
                            <br/><br/>
    
                            <h2><c:out value="${itemTitle}"/></h2>
                            <div class="grid_12 essaytext">
                                <img class="essay_image" src="${fn:escapeXml(itemThumbnailURL)}">

                                <c:out value="${content}" escapeXml="false"/>
                            </div>

                            <div class="grid_12 longitudeessaymetadata">
                                <c:if test="${fn:length(essayItem.associatedPeople) > 0}">

                                    <strong>Associated People</strong>:

                                    <c:forEach items="${essayItem.associatedPeople}" var="person">
                                        <a href="/search?keyword=${cudlfn:uriEnc(person)}"><c:out value="${person}"/></a>;&nbsp;
                                    </c:forEach>
                                </c:if>

                                <c:if test="${fn:length(essayItem.associatedOrganisations) > 0}">

                                    <br/><br/>
                                    <strong>Organisations</strong>:

                                    <c:forEach items="${essayItem.associatedOrganisations}"
                                               var="organisation">

                                        <a href="/search?keyword=${cudlfn:uriEnc(organisation)}"><c:out value="${organisation}"/></a>;&nbsp;
                                    </c:forEach>
                                </c:if>

                                <c:if test="${fn:length(essayItem.associatedSubjects) > 0}">
                                    <br/><br/>
                                    <strong>Subjects</strong>:
                                    <c:forEach items="${essayItem.associatedSubjects}"
                                               var="subject">
                                        <a href="/search/advanced/results?subject=${cudlfn:uriEnc(subject)}"><c:out value="${subject}"/></a>;&nbsp;
                                    </c:forEach>
                                </c:if>

                                <c:if test="${fn:length(essayItem.associatedPlaces) > 0}">
                                    <br/><br/>
                                    <strong>Places</strong>:
                                    <c:forEach items="${essayItem.associatedPlaces}"
                                               var="place">
                                        <a href="/search/advanced/results?location=${cudlfn:uriEnc(place)}"><c:out value="${place}"/></a>;&nbsp;
                                    </c:forEach>
                                </c:if>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="campl-column4 campl-secondary-content">
                    <div class="campl-content-container">
    
                        <h4>Related Items</h4>

                        <c:forEach items="${relatedItems}" var="itemId">
                            <c:set var="item" value="${cudlfn:getItem(itemFactory, itemId)}"/>

                            <div class="relatedItem grid_6">
                                <h5>
                                    <a href="/view/${cudlfn:uriEnc(item.id)}">
                                        <c:out value="${item.title} (${item.shelfLocator})"/>
                                    </a>
                                </h5>

                                <c:choose>
                                    <c:when test="${fn:trim(item.thumbnailOrientation) == 'portrait'}">
                                        <c:set var="imageDimensions" value="height: 100%"/>
                                    </c:when>
                                    <c:when test="${fn:trim(item.thumbnailOrientation) == 'landscape'}">
                                        <c:set var="imageDimensions" value="width: 100%"/>
                                    </c:when>
                                </c:choose>

                                <div class="left">
                                    <div class="collections_carousel_image_box">
                                        <div class="collections_carousel_image">
                                            <a href="/view/${cudlfn:uriEnc(item.id)}/">
                                                <img alt="${fn:escapeXml(item.id)}"
                                                     src="${fn:escapeXml(item.thumbnailURL)}"
                                                     style="${fn:escapeXml(imageDimensions)}">
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>    
</cudl:generic-page>
