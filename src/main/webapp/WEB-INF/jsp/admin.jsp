<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
    <jsp:param name="activeMenuIndex" value="1" />
    <jsp:param name="displaySearch" value="true" />
    <jsp:param name="subtitle" value="Admin" />
</jsp:include>

<div class="campl-row campl-content campl-recessed-content">
    <div class="campl-wrap clearfix">
        <div class="campl-column9  campl-main-content" id="content">
            <div class="campl-content-container">
                <form method="get" action="/admin/jsonsuccess">
                    <button type="submit" class="campl-primary-cta">Cudl-data Live</button>
                    <p>copy json from git master to git branch</p>
                </form>
                <form method="get" action="/admin/dbsuccess">
                    <button type="submit" class="campl-primary-cta">Database Live </button>
                    <p>copy dev database to live database</p>
                </form>

                <p><a href="/admin/auth/logout">Logout</a></p>

            </div>
        </div>
    </div>
</div>

<jsp:include page="header/footer-full.jsp" />


