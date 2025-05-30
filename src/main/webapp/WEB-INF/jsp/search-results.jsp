<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:search-results-page title="Search">
    <jsp:attribute name="queryInfo">
        <ul>
            <cudl:search-result-param form="${form}" label="Keyword" attr="keyword"/>
            <cudl:search-result-param form="${form}" label="Full Text" attr="fullText"/>
            <cudl:search-result-param form="${form}" label="Exclude Text" attr="excludeText"/>
            <cudl:search-result-param form="${form}" label="Classmark" attr="shelfLocator"/>
            <cudl:search-result-param form="${form}" label="CUDL ID" attr="fileID"/>
            <cudl:search-result-param form="${form}" label="Title" attr="title"/>
            <cudl:search-result-param form="${form}" label="Author" attr="author"/>
            <cudl:search-result-param form="${form}" label="Subjects" attr="subject"/>
            <cudl:search-result-param form="${form}" label="Language" attr="language"/>
            <cudl:search-result-param form="${form}" label="Associated Place or Origin" attr="place"/>
            <cudl:search-result-param form="${form}" label="Current Location" attr="location"/>
            <c:if test="${(not empty form.yearStart) and (empty form.yearEnd)}">
                <cudl:search-result-param form="${form}" label="Exact Year" attr="yearStart"/>
            </c:if>
            <c:if test="${not (empty form.yearStart or empty form.yearEnd)}">
                <cudl:search-result-param form="${form}" label="Year from" attr="yearStart"/>
                <cudl:search-result-param form="${form}" label="Year to" attr="yearEnd"/>
            </c:if>
        </ul>

            <div class="query-actions">
                <a class="change-query campl-btn campl-primary-cta" href="/search/query?${fn:escapeXml(form.getQueryParams())}">Change Query</a>
            </div>

    </jsp:attribute>
    <jsp:attribute name="resultInfo">

            <cudl:search-result-info results="${results}"/>

    </jsp:attribute>
    <jsp:attribute name="queryHelp">

            <c:choose>
                <c:when test="${results.numberOfResults==0}">
                    <cudl:search-no-results/>
                    <cudl:search-examples/>
                </c:when>
                <c:otherwise/>
            </c:choose>

    </jsp:attribute>
    <jsp:body/>
</cudl:search-results-page>
