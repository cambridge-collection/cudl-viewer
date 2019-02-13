<%@tag description="The CUDL local navigation" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="activeMenuIndex" required="true" type="java.lang.Integer" %>
<%@attribute name="displaySearch" required="true" type="java.lang.Boolean" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="subtitle" required="false" type="java.lang.String" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<c:set var="defaultTitle" value="Manchester Digital Library"/>


<%-- TODO: Move this to main CSS --%>
<style>
     .cudl-header-search {
         position: absolute;
         right: -20px;
         bottom: 0;
         width: 40%;
     }
     .campl-search-input {
         height: 32px;
         min-height: 32px;
     }
     .cudl-header-search input[type=text] {
         height: 32px;
         width: calc(100% - 31px);
     }
     form.campl-search-input input.campl-search-submit {
         height: 32px;
         width: 32px;
     }
     div.campl-site-search {
        display: none;
    }
    @media screen and (max-width: 767px) {
        div.campl-site-search {
            display: block;
        }
        .cudl-header-search {
            display: none;
        }
        #site-search-container input[type=text] {
            margin: 0;
            padding: 0;
            height: 32px;
            line-height: 32px;
        }
    }
</style>

<div class="campl-row campl-page-header campl-section-page">
    <div class="campl-wrap clearfix">
        <div class="campl-column12">
            <div class="campl-content-container">
                <div class="campl-breadcrumb" id="breadcrumb">
                    <ul class="campl-unstyled-list campl-horizontal-navigation clearfix">
                        <li class='first-child'><a href="http://www.manchester.ac.uk/" class="campl-home ir">Home</a></li>
                        <li><a href="http://www.library.manchester.ac.uk/">University of Manchester Library</a></li>

                        <c:choose>
                            <c:when test="${not empty title}">
                                <li><a href="/"><c:out value="${defaultTitle}"/></a></li>
                                <li>
                                    <p class="campl-current"><c:out value="${title}"/></p>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <p class="campl-current"><c:out value="${defaultTitle}"/></p>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>

                <div style="position:relative;">
                    <h1 class="campl-page-title"><c:out value="${defaultTitle}"/></h1>

                    <c:if test="${displaySearch}">
                        <div class="campl-column6 cudl-header-search">
                            <form action="/search" class="campl-search-input">
                                <input name="keyword" type="text" class="text" placeholder="Search" />
                                <input type="image" class="campl-search-submit " src="/img/interface/btn-search-inpage.png">
                            </form>
                        </div>
                    </c:if>
                </div>

                <p class="campl-mobile-parent">
                    <a href=""><span class="campl-back-btn campl-menu-indicator"></span>Department A-Z</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div class="campl-row campl-page-header">
    <div class="campl-wrap clearfix campl-local-navigation" id="local-nav">
        <div class="campl-local-navigation-container">
            <ul class="campl-unstyled-list">
                <li class="first"><a href="/" title="Home" class="${activeMenuIndex == 0 ? 'campl-selected' : ''}"> Home </a></li>
                <li><a href="/collections/" title="Browse" class="${activeMenuIndex == 1 ? 'campl-selected' : ''}"> Browse </a></li>
                <li><a href="/search" title="Search" class="${activeMenuIndex == 2 ? 'campl-selected' : ''}"> Search </a>
                    <ul class="campl-unstyled-list local-dropdown-menu">
                        <li><a href="/search">Simple Search</a></li>
                        <li><a href="/search/advanced/query">Advanced Search</a></li>
                    </ul>
                </li>
                <li><a href="/mylibrary/" title="My Library" class="${activeMenuIndex == 3 ? 'campl-selected' : ''}"> My Library </a></li>
                <li><a href="/about/" title="About" class="${(activeMenuIndex == 4) ? 'campl-selected' : ''}"> About </a>
                    <ul class="campl-unstyled-list local-dropdown-menu">
                        <li><a href="/about/">Introducing the Digital Library</a></li>
                        <li><a href="/about-dl-platform/">About the Cambridge Digital Library Platform</a></li>
                        <li><a href="/news/">News</a></li>
                        <li><a href="/contributors/">Contributors</a>
                        <li><a href="/terms/">Terms and Conditions</a></li>
                    </ul>
                </li>
                <li><a href="/help/" title="Help" class="${activeMenuIndex == 5 ? 'campl-selected' : ''}"> Help </a></li>

                <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li><a style="background:#ff4444" href="/admin/" title="Admin"> Admin </a></li>
                </sec:authorize>

            </ul>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty title}">
            <div class="campl-wrap clearfix campl-page-sub-title campl-recessed-sub-title">
                <div class="campl-column12">
                    <div class="campl-content-container">
                        <p class="campl-sub-title"><c:out value="${title}"/></p>
                    </div>
                </div>
            </div>
        </c:when>
        <c:when test="${not empty subtitle}">
            <div class="campl-wrap clearfix campl-page-sub-title campl-recessed-sub-title">
                <div class="campl-column3 campl-spacing-column">&nbsp;</div>
                <div class="campl-column9">
                    <div class="campl-content-container">
                        <h1 class="campl-sub-title"><c:out value="${subtitle}"/></h1>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>
</div>
<!--  End of Local Header -->

