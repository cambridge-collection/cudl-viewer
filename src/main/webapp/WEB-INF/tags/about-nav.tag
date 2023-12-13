<%@tag description="Navigation for the About section" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@uiThemeBean.themeUI.title" var="defaultTitle" />

<!-- side nav -->

     <div class="tertiary-navigation mb-5">
        <div class="campl-tertiary-navigation-structure">
            <ul class="campl-unstyled-list campl-vertical-breadcrumb">
                <li><a href="/">${defaultTitle}<span
                    class="campl-vertical-breadcrumb-indicator"></span></a></li>
            </ul>
            <ul class="campl-unstyled-list campl-vertical-breadcrumb-navigation">
                <li class="campl-selected"><a href="/about/">About</a>
                    <ul class='campl-unstyled-list campl-vertical-breadcrumb-children'>
                        <li><a href="/about/">Introducing ${defaultTitle}</a></li>
                        <li><a href="/news/">News</a></li>
                        <li><a href="/about-dl-platform">Community</a></li>
                        <li><a href="/contributors/">Contributors</a></li>
                        <li><a href="/terms/">Terms & Conditions</a></li>
                    </ul></li>
            </ul>
        </div>
     </div>

