<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_GB"/>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:search-results-page title="Advanced Search">
    <jsp:attribute name="queryInfo">
        <ul>
            <cudl:search-result-param form="${form}" label="Keyword" attr="keyword"/>
            <c:if test="${not empty form['keyword'] and enableTagging}">
                <div class="recall-slider">
                    <input id="recall-slider-input" type="text" name="recallScale"
                            data-slider-value="${fn:escapeXml(form.recallScale)}"
                            data-slider-min="0"
                            data-slider-max="1"
                            data-slider-step="0.1"
                            data-slider-ticks="[0, 0.5, 1]"
                            data-slider-ticks-labels='["Curated<br>metadata", "Secondary<br>literature", "Crowd-<br>sourced"]'
                            data-slider-tooltip="hide">
                    <input type="hidden" name="tagging" value="1">
                </div>
            </c:if>
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
            <a class="change-query campl-btn campl-primary-cta" href="/search/query?${fn:escapeXml(queryString)}">
                Change Query
            </a>
        </div>
    </jsp:attribute>
    <jsp:body/>
</cudl:search-results-page>
