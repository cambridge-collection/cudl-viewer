<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>

<cudl:generic-page pagetype="STANDARD" title="${collection.title}">
	<jsp:attribute name="pageData">
		<cudl:default-context>
			<cudl:context-editable-areas>
				<cudl:editable-area id="newsDiv" filename="news.html"/>
			</cudl:context-editable-areas>
		</cudl:default-context>
	</jsp:attribute>

	<jsp:body>
		<cudl:nav activeMenuIndex="${4}" displaySearch="true" subtitle="News"/>

		<div class="campl-row campl-content campl-recessed-content">
        	<div class="campl-wrap clearfix">

        		<!-- side nav -->
        		<div class="campl-column3">
        			<div class="campl-tertiary-navigation">
        				<div class="campl-tertiary-navigation-structure">
        					<ul class="campl-unstyled-list campl-vertical-breadcrumb">
        						<li><a href="/">Cambridge Digital Library<span
        								class="campl-vertical-breadcrumb-indicator"></span></a></li>
        					</ul>
        					<ul
        						class="campl-unstyled-list campl-vertical-breadcrumb-navigation">
        						<li class="campl-selected"><a href="/about/">About</a>
        							<ul
        								class='campl-unstyled-list campl-vertical-breadcrumb-children'>
        								<li><a href="/about/">Introducing the Cambridge Digital
        										Library</a></li>
        								<li><a href="/news/">News</a></li>
        								<li><a href="/contributors/">Contributors</a></li>
        								<li><a href="/terms/">Terms & Conditions</a></li>
        							</ul></li>

        					</ul>
        				</div>
        			</div>
        		</div>


        		<div class="campl-column6  campl-main-content" id="content">
        			<div class="campl-content-container news_items">


        			<div id="newsDiv">
        		        <c:import charEncoding="UTF-8" url="/html/news.html" />
        			</div>


        			</div>
        		</div>
        	</div>
        </div>
	</jsp:body>
</cudl:generic-page>
