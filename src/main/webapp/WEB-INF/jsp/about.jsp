<%@page autoFlush="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@uiThemeBean.themeUI.title" var="defaultTitle" />
<c:set var="defaultTitle" value="${defaultTitle}"/>

<cudl:generic-page pagetype="STANDARD">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="aboutMainDiv" filename="about-main.html"/>
                <cudl:editable-area id="aboutSideDiv" filename="about-side.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${4}" displaySearch="true" subtitle="Introducing ${defaultTitle}"/>
        <div id="main_content" class="container">

                <div class="row">

                  <div class="col-md-3">

                    <cudl:about-nav/>

                  </div>
                  <div class="col-md-6 p-auto" id="content">

                        <div id="aboutMainDiv">
                            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/about-main.html"/>
                        </div>

                  </div>
                  <div class="col-md-3 p-auto">

                        <div id="aboutSideDiv">
                            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/about-side.html"/>
                        </div>

                  </div>
                </div>

        </div>
    </jsp:body>
</cudl:generic-page>

