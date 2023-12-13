<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:generic-page pagetype="STANDARD" title="Terms & Conditions">
    <cudl:nav activeMenuIndex="${4}" displaySearch="true" subtitle="Terms & Conditions"/>

    <div id="main_content" class="container">
        <div class="row">
            <div class="col-md-3">
              <cudl:about-nav />
            </div>
            <div class="col-md-7" id="content">

                    <c:import charEncoding="UTF-8" url="${contentHTMLURL}/terms.html"/>

            </div>
        </div>
    </div>
</cudl:generic-page>
