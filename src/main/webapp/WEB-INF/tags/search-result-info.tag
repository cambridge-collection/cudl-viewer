<%@tag description="Number of results and query time" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="en_GB"/>

<%@attribute name="results" required="true" type="ulcambridge.foundations.viewer.search.SearchResultSet" %>

<div class="_mib"></div>
<div class="campl-column12 resultcount">
    About <fmt:formatNumber value="${results.numberOfResults}"/> results (<span id="reqtime"></span></span>)
</div>
