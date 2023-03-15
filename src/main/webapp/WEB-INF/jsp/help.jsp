<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:generic-page pagetype="STANDARD">
    <cudl:nav activeMenuIndex="${5}" displaySearch="true" title="Help: FAQ"/>

    <div class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <div class="campl-column9  campl-main-content" id="content">
                <div class="campl-content-container">

                    <div class="panel light faq">

                        <div class="grid_11">
                            <c:import charEncoding="UTF-8" url="${contentHTMLURL}/help.html"/>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</cudl:generic-page>
