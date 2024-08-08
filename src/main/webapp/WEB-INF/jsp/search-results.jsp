<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<%-- Unlike the advanced search page, the regular search page uses the same
     template for the query definition page and result page. Certain things have
     to be hidden on the query page. --%>
<c:set var="userHasSearched" value="${not empty form.keyword}"/>

<cudl:search-results-page title="Search">
    <jsp:attribute name="queryInfo">
        <form:form modelAttribute="searchForm" class="" action="/search" method="GET">

            <form:input path="keyword" class="search" type="text" value="${form.keyword}" name="keyword" placeholder="Search" autofocus="autofocus"/>
            <input class="campl-search-submit " src="/img/interface/btn-search-header.png" type="image">
            <c:forEach items="${form.facets}" var="facet">
                <%-- FIXME: Should this be using form:input? --%>
                <input path="${fn:escapeXml(facet.key)}"
                       type="hidden"
                       name="facet-${fn:escapeXml(facet.key)}"
                       value="${fn:escapeXml(facet.value)}">
            </c:forEach>
            <form:input path="fileID" type="hidden" name="fileID" value="${form.fileID}"/>
        </form:form>
        <div class="altsearchlink">
            <form:form modelAttribute="searchForm" action="/search/advanced/query" method="GET">
                <input type="hidden" value="${fn:escapeXml(form.keyword)}" name="keyword">
                <input class="altsearchlink" type="submit" disabled value="Advanced Search (Coming Soon)">
            </form:form>
        </div>
    </jsp:attribute>
    <jsp:attribute name="resultInfo">
        <%-- Don't show the result count before the user has performed a search --%>
        <c:if test="${userHasSearched}">
            <cudl:search-result-info results="${results}"/>
        </c:if>
    </jsp:attribute>
    <jsp:attribute name="queryHelp">
        <%-- Don't show the "couldn't find any items" text before the user has performed a search --%>
        <c:if test="${userHasSearched} && ${results!=null}">
            <cudl:search-no-results/>
        </c:if>
        <cudl:search-examples/>
    </jsp:attribute>
    <jsp:body/>
</cudl:search-results-page>
