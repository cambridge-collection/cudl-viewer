<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,java.util.Iterator,ulcambridge.foundations.viewer.ItemFactory"%>
	

<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>
	
<jsp:include page="header/header-full.jsp" >
	<jsp:param name="title" value="<%=collection.getTitle()%>" />
</jsp:include>	
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>
<jsp:include page="header/nav-browse-submenu.jsp" />

<div class="clear"></div>

<section id="content" class="grid_20 content">

	<jsp:include page="<%=collection.getSummary()%>" />

	<jsp:include page="<%=collection.getSponsors()%>" />
</section>

<jsp:include page="footer/footer.jsp" />



