<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.ItemFactory,java.util.List"%>
<%@taglib prefix="c" 
       uri="http://java.sun.com/jsp/jstl/core" %>	
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>       
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Browse our collections" />
</jsp:include>


<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">

			<div id="collectionsDiv">				
		        <c:import charEncoding="UTF-8" url="/html/collections/collections.html" /> 
			</div>
				
		</div>
	</div>
</div>

<sec:authorize access="hasRole('ROLE_ADMIN')">
 
<script>$('#collectionsDiv').attr('contenteditable', 'true');</script>
<jsp:include page="editor.jsp" >
  <jsp:param name='dataElements' value='collectionsDiv'/>
  <jsp:param name='filenames' value='collections/collections.html'/>
</jsp:include>

</sec:authorize>



<jsp:include page="header/footer-full.jsp" />



