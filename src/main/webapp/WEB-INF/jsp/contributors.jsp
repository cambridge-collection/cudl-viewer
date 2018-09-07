<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <json:property name="isAdmin" value="${true}"/>
                <json:array name="editableAreas">
                    <json:object>
                        <json:property name="id" value="contributorsDiv"/>
                        <json:property name="filename" value="contributors.html"/>
                    </json:object>
                </json:array>
            </sec:authorize>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${4}" displaySearch="true"
                  subtitle="Contributors to the Cambridge Digital Library"/>

        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <cudl:about-nav />
                <div class="campl-column8  campl-main-content" id="content">
                    <div class="campl-content-container">
                        <div id="contributorsDiv">
                            <c:import charEncoding="UTF-8" url="/html/contributors.html" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
