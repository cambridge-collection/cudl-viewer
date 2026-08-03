<%@tag description="Base search results page" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<%@attribute name="title" required="true" type="java.lang.String" %>
<%@attribute name="queryInfo" required="true" fragment="true" %>
<%@attribute name="queryHelp" required="false" fragment="true" %>

<cudl:generic-page pagetype="ADVANCED_SEARCH_RESULTS" title="${title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <json:property name="queryString" value="${queryString}"/>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${2}" displaySearch="true" title="${title}"/>

        <div id="main_content" class="container">

                <div class="row" id="content">
                    <div class="col-md-4 campl-secondary-content">

                        <div class="searchform box">

                            <div class="campl-content-container">
                                <jsp:invoke fragment="queryInfo"/>

                                <div id="selected_facets"></div>

                                <cudl:search-result-info/>
                            </div>

                            <div class="campl-content-container search-facets">
                                <h5>Refine by:</h5>

                                <ol id="tree" class="campl-unstyled-list"></ol>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-8 camp-content">
                        <div class="searchexample campl-content-container">
                            <div class="search-no-results"></div>

                            <jsp:invoke fragment="queryHelp"/>
                        </div>

                        <div class="search-error campl-content-container"></div>

                        <!-- start of list -->
                        <div id="collections_carousel" class="collections_carousel">
                        </div>
                        <!-- end of list -->
                        <div class="pagination toppagination"></div>
                    </div>
                </div>

        </div>
    </jsp:body>
</cudl:generic-page>
