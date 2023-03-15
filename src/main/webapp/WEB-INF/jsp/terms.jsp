<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD" title="Terms & Conditions">
    <cudl:nav activeMenuIndex="${4}" displaySearch="true" subtitle="Terms & Conditions"/>

    <div class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <cudl:about-nav />
            <div class="campl-column8  campl-main-content" id="content">
                <div class="campl-content-container">
                    <c:import charEncoding="UTF-8" url="${contentHTMLURL}/terms.html"/>
                </div>
            </div>
        </div>
    </div>
</cudl:generic-page>
