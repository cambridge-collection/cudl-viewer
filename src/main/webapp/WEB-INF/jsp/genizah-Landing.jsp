<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="header/genizah-header.jsp" />
</head>
<jsp:include page="genizah-bodyStart.jsp" />

<div class="clear"></div>

<jsp:include page="genizah-Search.jsp">
	<jsp:param name="checkedOption" value="AUTHOR"/>
</jsp:include>

<jsp:include page="genizah-footer.jsp" />