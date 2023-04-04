<%@page autoFlush="true" %>
<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<cudl:generic-page pagetype="STANDARD">
    <cudl:nav activeMenuIndex="${2}" displaySearch="true" title="Access Denied"/>

    <div id="main_content" class="campl-row campl-content campl-recessed-content">
        <div class="campl-wrap clearfix">
            <div class="campl-column12  campl-main-content" id="content">
                <div class="campl-content-container">
                    <h2>Access Denied</h2>
                    <div id="error">${error}</div>

                    <div class="grid_18">
                        You do not have permission to access the page requested. <br /> <br />
                    </div>
                </div>
            </div>
        </div>
    </div>
</cudl:generic-page>
