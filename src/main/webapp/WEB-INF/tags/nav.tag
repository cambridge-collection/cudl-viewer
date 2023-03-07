<%@tag description="The CUDL local navigation" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@attribute name="activeMenuIndex" required="true" type="java.lang.Integer" %>
<%@attribute name="displaySearch" required="true" type="java.lang.Boolean" %>
<%@attribute name="title" required="false" type="java.lang.String" %>
<%@attribute name="subtitle" required="false" type="java.lang.String" %>
<%@attribute name="collection" required="false" type="java.lang.Object" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="defaultTitle" value="Cambridge Digital Library"/>

<div class="content main-nav-section ${(activeMenuIndex == 0) ? 'home' : ''}">

    <div class="container clearfix">
        <div class="fixed-top">
            <nav class="row justify-content-between main-nav">
                <a class="item" href="http://www.cam.ac.uk">
                    <img class="cam-logo" alt="University of Cambridge" src="/img/interface/uoc_logo.svg"/>
                </a>
                <div class="item text-white nav-right-items">
                    <div class="search">
                        <a href="/search">
                            <span class="icon icon-search"></span>
                        </a>
                    </div>
                    <div class="dropdown">
                        <a href="#" onclick="toggleDropdown()" class="bg-white">
                            <span class="icon icon-menu dropbtn"></span>
                        </a>
                    </div>
                </div>
            </nav>
            <div id="myDropdown" class="dropdown-content">
                <div class="clearfix">
                    <button onclick="toggleDropdown()" class="closebtn icon"/>
                </div>
                <ul>
                    <li><a href="/">Home</a></li>
                    <li><a href="/collections/">View all collections</a></li>
                    <li><a href="/search">Search</a></li>
                    <li><a href="/about/">About</a></li>
                    <li><a href="/help/">Help</a></li>
                </ul>
            </div>
        </div>
    </div>
    <script>
        /* When the user clicks on the button,
        toggle between hiding and showing the dropdown content */
        function toggleDropdown() {
            document.getElementById("myDropdown").classList.toggle("show");
        }

        // Close the dropdown if the user clicks outside of it
        window.onclick = function(event) {
            if (!event.target.matches('.dropbtn')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                var i;
                for (i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }
    </script>

    <!--end of Uni of Cam banner-->
    <!--CUDL banner-->

    <div class="container">

        <div class="cudl-banner">
            <div class="banner-part"><h1><a href="/">Cambridge Digital Library</a></h1></div>

        </div>

        <cudl:crumbtrail title="${title}" subtitle="${subtitle}" collection="${collection}" />
    </div>
</div>
