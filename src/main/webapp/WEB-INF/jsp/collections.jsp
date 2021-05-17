<%@page autoFlush="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>


<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
    <jsp:attribute name="pageData">
        <cudl:default-context>
            <cudl:context-editable-areas>
                <cudl:editable-area id="collectionsDiv" filename="collections/collections.html"/>
            </cudl:context-editable-areas>
        </cudl:default-context>
    </jsp:attribute>

    <jsp:body>
        <cudl:nav activeMenuIndex="${1}" displaySearch="true" title="Browse our collections"/>

        <div class="campl-row campl-content campl-recessed-content">
            <div class="campl-wrap clearfix">
                <div class="campl-column9  campl-main-content" id="content">
                    <div id="collectionsDiv">
                        <c:import charEncoding="UTF-8" url="${contentHTMLURL}/collections/collections.html" />
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</cudl:generic-page>
