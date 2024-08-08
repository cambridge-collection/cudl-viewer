<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:eval expression="@uiThemeBean.themeUI.title" var="defaultTitle" />
<c:set var="defaultTitle" value="${defaultTitle}"/>

<cudl:generic-page pagetype="STANDARD">

    <jsp:body>
        <cudl:nav activeMenuIndex="${4}" displaySearch="true"
                  subtitle="Contributors to the ${defaultTitle}"/>

        <div id="main_content" class="container">
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
