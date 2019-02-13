<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:generic-page pagetype="STANDARD">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="mainContent" filename="about-main.html"/>
                <cudl:editable-area id="sideContent" filename="about-side.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${-1}" displaySearch="true" title="Cambridge Digital Library Platform"/>

        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">

                <div class="campl-column6  campl-main-content" id="content">
                    <div class="campl-content-container" id="mainContent">
                        <p>Cambridge Digital Library launched in 2011, seeking to open up Cambridge’s unique collections
                            to the widest possible audience for research, education, curiosity and enjoyment.</p>

                        <p>Since then, with generous support from the Polonsky Foundation, more than £2 million has been
                            invested in the Digital Library, with contributions from technical specialists, curators and
                            researchers from Cambridge and beyond. We provide rich digital content alongside research
                            output on a purpose-built platform written by a team of in-house developers at Cambridge
                            University Library. The <a href="https://cudl.lib.cam.ac.uk" target="_blank">Cambridge
                                Digital Library</a> (or CUDL - informally known as ‘cuddle’), is a well-loved,
                            innovative, world-class resource for everyone.</p>

                        <p>Cambridge University Library’s mission is to provide further <b>access</b> to world heritage
                            collections, to <b>collaborate</b> with our partners in maximising reach and impact, to <b>engage</b>
                            our audiences and <b>inspire</b> thirst for knowledge and understanding. We aim to make the
                            Cambridge Digital Library Platform available to other institutions, so that universities,
                            libraries, museums and galleries can join Cambridge in sharing their collections with the
                            world.</p>

                        <p>The first step has already been taken. A collaborative project with the University of
                            Manchester is in progress to give the world access to the digitised special collections at
                            John Rylands Library, starting with their collection of <a
                                href="https://www.digitalviewer.manchester.ac.uk/collections/hebrew/" target="_blank">Hebrew
                                manuscripts</a>, including the Hebrew Bible known as Writings (Ketuvim), and <a
                                href="https://www.digitalviewer.manchester.ac.uk/collections/petrarch/" target="_blank">Petrarch</a>
                            commentaries and exegesis of their Renaissance Italy collections.</p>

                        <p>For further information, please enquire via email to <a
                            href="mailto:cdl-enquires@lib.cam.ac.uk">cdl-enquires@lib.cam.ac.uk</a>.</p>

                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
