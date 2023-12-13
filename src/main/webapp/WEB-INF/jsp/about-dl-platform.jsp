<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:generic-page pagetype="STANDARD">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="aboutDLPlatformDiv" filename="about-dl-platform.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${4}" displaySearch="true" subtitle="Community"/>

        <div id="main_content" class="container">
            <div class="row">
                <div class="col-md-3">
                  <cudl:about-nav />
                </div>
                <div class="col-md-7 p-auto" id="content">

                        <div id="aboutDLPlatformDiv">
                            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/about-dl-platform.html"/>
                        </div>

                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
