<%@tag description="The standard project light navigation header" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<a href="#primary-nav" class="campl-skipTo">skip to primary
        navigation</a>
<a href="#content" class="campl-skipTo">skip to content</a>

<div class="campl-row campl-global-header">
    <div class="campl-wrap clearfix">
        <div class="campl-header-container campl-column8"
            id="global-header-controls">
            <a href="http://www.manchester.ac.uk/" class="campl-main-logo"> <img
                alt="University of Manchester"
                src="/img/interface/main-logo-small.png" />
            </a>
        </div>

        <div class="campl-column2">

            <div class="campl-site-search" id="site-search-btn">

                <label for="header-search" class="hidden">Search site</label>
                <div class="campl-search-input">
                    <form action="/search" method="get">
                        <input id="header-search" type="text" name="keyword" value=""
                            placeholder="Search" /> <input type="image"
                            class="campl-search-submit "
                            src="/img/interface/btn-search-header.png" />
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- .campl-global-header ends -->
