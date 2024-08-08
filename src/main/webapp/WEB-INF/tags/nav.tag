<%@tag description="The CUDL local navigation" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="activeMenuIndex" required="true" type="java.lang.Integer" %>
<%@attribute name="displaySearch" required="true" type="java.lang.Boolean" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="subtitle" required="false" type="java.lang.String" %>
<%@attribute name="collection" required="false" type="java.lang.Object" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@uiThemeBean.themeUI.title" var="defaultTitle" />
<c:set var="defaultTitle" value="${defaultTitle}"/>

<div class="content main-nav-section ${(activeMenuIndex == 0) ? 'home' : ''}">

    <div class="container clearfix">
        <div class="fixed-top">
            <div class="masthead-container">
                <div class="flex-center">
                    <nav class="justify-content-between main-nav">
                        <cudl:ui-image name="logo" cssClass="cam-logo" />
<%--                        <a id="dl-logo-link" class="item" href="${themeUI.getImage("logo").src}">--%>
<%--                            <div id="dl-logo" class="cam-logo"></div>--%>
<%--                        </a>--%>
                        <div class="item text-white nav-right-items">
                            <div class="search">
                                <form action="/search" method="GET" class="search-form d-flex flex-row rounded-2">
                                    <span class="input-group-prepend">
                                        <button type="submit" id="search-label" class="btn btn-light border rounded-start-2 rounded-end-0 ms-n5 border">
                                            <i class="fa fa-search" aria-hidden="true"></i>
                                            <span class="d-none">Search</span>
                                        </button>
                                    </span>
                                    <input name="keyword" placeholder="Search" type="text" class="search form-control border border border-start-0  rounded-start-0 rounded-end-2" value="">
                                </form>
                            </div>
                            <div class="dropdown">
                                <a href="#" id="menuDropdownOpenButton" class="bg-white">
                                    <span class="icon icon-menu dropbtn"></span>
                                </a>
                            </div>
                        </div>
                    </nav>
                </div>
                <div id="myDropdown" class="dropdown-content">
                    <div class="clearfix">
                        <a href="#" id="menuDropdownCloseButton" class="bg-white">
                            <span class="closebtn icon"></span>
                        </a>
                    </div>
                    <ul>
                        <li><a href="/">Home</a></li>
                        <li><a href="/collections/">View all collections</a></li>
                        <li><a href="#"><span style="color:#444">Advanced Search (Coming Soon)</span></a></li>
                        <li><a href="/about/">About</a></li>
                        <li><a href="/help/">Help</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!--end of Uni of Cam banner-->
    <!--CUDL banner-->

    <div class="container banner">

        <div class="cudl-banner">
            <div class="banner-part"><h1><a id="dl-header-link" href="/">${defaultTitle}</a></h1></div>

        </div>

        <cudl:crumbtrail title="${title}" subtitle="${subtitle}" collection="${collection}" />
    </div>
</div>
