<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>

<c:set var="pagetype" value="STANDARD"/>
<c:set var="metaDescription">
    A home for the discovery of digitised material and research
    outputs from the University of Cambridge and beyond
</c:set>

<cudl:generic-page pagetype="${pagetype}" title="${collection.title}">

    <jsp:attribute name="head">
        <cudl:head-content pagetype="${pagetype}" metaDescription="${metaDescription}"/>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${0}" displaySearch="true"/>

        <c:if test="${not empty downtimeWarning}">
            <c:out value="${downtimeWarning}" escapeXml="false"/>
        </c:if>

        <!--Carousel-->
        <!--Images=1280x640-->
        <div class="container">
        <div class="slideshow-container">

            <div class="mySlides fade">

                <img src="/img/tmp/carousel-newton.jpg" alt="Newton Papers" style="width:100%">
                <div class="text"><a href="/collections/newton">Newton Papers</a></div>
                <div class="sub-text">Discover the Papers of Sir Isaac Newton</div>
            </div>

            <div class="mySlides fade">

                <img src="/img/tmp/carousel-genizah.jpg" alt="Cairo Genizah" style="width:100%">
                <div class="text"><a href="/collections/genizah">Cairo Genizah</a></div>
                <div class="sub-text">A window on the medieval world</div>
            </div>

            <div class="mySlides fade">

                <img src="/img/tmp/carousel-treasures.jpg" alt="Treasures of the Library" style="width:100%">
                <div class="text"><a href="/collections/treasures">Treasures of the Library </a></div>
                <div class="sub-text">Books, manuscripts and other items from across our collections that are especially significant</div>
            </div>

        </div>
        <br>

        <div style="text-align:center">
            <span class="dot"></span>
            <span class="dot"></span>
            <span class="dot"></span>
        </div>

        <!--Featured Collections-->
        <!--Images=390x400-->

        <div class="featured-collections element clearfix"> <h2>Featured Collections</h2>
            <div class="tile-container clearfix">
                <div class="tile">
                    <img src="/img/tmp/PR-ATLAS-00004-00057-00003-000-00001.jpg" alt="Maps" class="image">
                    <div class="tile-caption-container"> <div class="tile-caption"><a href="/collections/maps">Maps</a></div></div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/MS-ADD-09597-00006-00002-000-00010.jpg" alt="Macclesfield Collection" class="image">
                    <div class="tile-caption-container"> <div class="tile-caption"><a href="/collections/macclesfield">Macclesfield Collection</a></div> </div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/PH-GEOGRAPHY-70KGQ-00018-000-00001.jpg" alt="Landscape histories from the air" class="image">
                    <div class="tile-caption-container"><div class="tile-caption"><a href="/collections/landscapehistories">Landscape histories from the air</a></div></div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/Treasures.jpg" alt="Treasures of the Library" class="image">
                    <div class="tile-caption-container"><div class="tile-caption"><a href="/collections/treasures">Treasures of the Library</a></div></div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/MS-DD-00009-00069-000-00279.jpg" alt="Greek Manuscripts" class="image">
                    <div class="tile-caption-container"><div class="tile-caption"><a href="/collections/greekmanuscripts">Greek Manuscripts</a></div></div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/DarwinSQ.jpg" alt="Charles Darwin" class="image">
                    <div class="tile-caption-container"><div class="tile-caption"><a href="/collections/darwin_mss">Charles Darwin</a></div></div>
                </div></div>
            <div class="more-link-container">
                <div class="more-link"><a href="/collections/">All Collections &gt;</a></div></div> </div>


        <div class="element clearfix">
            <h2>Watch</h2>
            <div class="watch-container clearfix">
                <div class="watch-feature"><iframe width="100%" height="400px" src="https://www.youtube.com/embed/videoseries?list=PLG24w6ETyHS01Mdob5IKBlFZZISgYb7yC" title="YouTube video player" frameborder="0" allow="autoplay;" allowfullscreen></iframe>
                </div><div class="watch-list">
                <ul class="watch-list-ul">
                    <li class="watch-list-item"><a href="https://www.youtube.com/watch?list=PLG24w6ETyHS01Mdob5IKBlFZZISgYb7yC&v=rjMtoxeblbE">
                        From shelf to screen – with ‘Digital’, a poem by Imtiaz Dharker &gt;
                    </a></li>
                    <li class="watch-list-item"><a href="https://www.youtube.com/watch?list=PLG24w6ETyHS01Mdob5IKBlFZZISgYb7yC&v=XxXb8qBYgPQ">
                        Codex Zacynthius MS Add.10062, Recovering the Text of the Oldest New Testament Catena Manuscript &gt;
                    </a></li>
                    <li class="watch-list-item"><a href="https://www.youtube.com/watch?list=PLG24w6ETyHS01Mdob5IKBlFZZISgYb7yC&v=Npf3M2cvAs8">
                        Scroll digitising XYZ and light box technique &gt;
                    </a></li>
                    <li class="watch-list-item"><a href="https://www.youtube.com/watch?list=PLG24w6ETyHS01Mdob5IKBlFZZISgYb7yC&v=lgmpZW8JZB8">
                        Seeing the positives in digitising negatives &gt;</a></li>

                </ul></div>    </div>

            <div class="element clearfix">
                <h2>Discover</h2><div class="tile-container clearfix">
                <div class="tile">
                    <img src="/img/tmp/MS-ADD-10062-UNDERTEXT-000-00128.jpg" alt="Codex Zacynthius" class="image">
                    <div class="tile-caption-container"><div class="tile-caption"><a href="/collections/codexzacynthius">Codex Zacynthius</a></div></div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/PH-UA-PHOT-00174-00004-000-00001.jpg" alt="The Rising Tide" class="image">
                    <div class="tile-caption-container"> <div class="tile-caption"><a href="/collections/therisingtide">The Rising Tide</a></div> </div>
                </div>
                <div class="tile">
                    <img src="/img/tmp/MS-ORCS-00001-00001-000-00004.jpg" alt="Curious Objects" class="image">
                    <div class="tile-caption-container"> <div class="tile-caption"><a href="/collections/curiousobjects">Curious Objects</a></div></div>
                </div>
            </div></div>

            <div class="element clearfix">
                <h2>Get Involved</h2><div class="tile-container clearfix">
                <div class="article">
                    <img src="/img/tmp/Rackham.jpg" alt="Oliver Rackham" class="image">
                    <h2><a href="https://cambridge-digital-library.github.io/Crowdsourcing/">Oliver Rackham</a></h2>
                    <p>Help us transcribe the notebooks of Oliver Rackham</p>

                </div>
                <div class="article">
                    <img src="/img/tmp/MS-CCCC-00014-00006-00002-00002-MAWO-00003-000-00029.jpg" alt="Oliver Rackham" class="image">
                    <h2><a href="https://cambridge-digital-library.github.io/Crowdsourcing/">Oliver Rackham</a></h2>
                    <p>Help us transcribe the notebooks of Oliver Rackham</p>

                </div>
                <div class="article">
                    <img src="/img/tmp/Constable-Collection.jpg" alt="Walking with Constable" class="image">
                    <h2><a href="https://walking-the-landscape.fitzmuseum.cam.ac.uk/">Walking with Constable</a></h2>
                    <p>What happens when we take digital technologies for a walk?</p>

                </div></div></div>
            <div class="element clearfix">
                <h2>Partner Collections</h2>
                <div class="tile-container clearfix">
                    <div class="tile">
                        <img src="/img/tmp/Girton_Crest.jpg" alt="Girton College" class="image">
                        <div class="tile-caption-container"> <div class="tile-caption"><a href="/collections/girton">Girton College</a></div></div>
                    </div>
                    <div class="tile">
                        <img src="/img/tmp/PH-CAVENDISH-P-00049-000-00001.jpg" alt="Cavendish Laboratory" class="image">
                        <div class="tile-caption-container"><div class="tile-caption"><a href="/collections/cavendish">Cavendish Laboratory</a></div></div>
                    </div>
                    <div class="tile">
                        <img src="/img/tmp/MYCENAE-SQ.jpg" alt="Mycenae Archive" class="image">
                        <div class="tile-caption-container"> <div class="tile-caption"><a href="/collections/mycenae">Mycenae Archive</a></div> </div>
                    </div>

                </div>
            </div>
            <div class="element clearfix">
                <h2>Follow Us</h2>
                <div class="social clearfix"></div>
                <a href="https://www.facebook.com/camdiglib"><span class="icon icon-facebook"></span></a><a href="https://twitter.com/CamDigLib"><span class="icon icon-twitter"></span></a><a href="https://www.youtube.com/user/CamUniLib"><span class="icon icon-youtube"></span></a>
            </div></div>
        </div>
    </jsp:body>
</cudl:generic-page>
