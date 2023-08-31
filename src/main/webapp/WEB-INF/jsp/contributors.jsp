<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<!--Links to a temporary directory created just for testing -->

<link type="text/css" href="${pageContext.request.contextPath}/bootstrap5-test-css/bootstrap.min.css" rel="stylesheet">
<link  type="text/css" href="${pageContext.request.contextPath}/bootstrap5-test-css/all%20collections-test.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap5-test-css/bootstrap.bundle.min.js"></script>


<cudl:generic-page pagetype="STANDARD">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="contributorsDiv" filename="contributors.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${4}" displaySearch="true"
                  subtitle="Contributors to the Cambridge Digital Library"/>

        <div id="main_content" class="container bg-white border">
            <div class="row">
                <div class="col-md-3">
                    <cudl:about-nav />
                </div>
                <div class="col-md-8" id="content">

                        <div id="contributorsDiv">
                            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/contributors.html" />
                        </div>

                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
