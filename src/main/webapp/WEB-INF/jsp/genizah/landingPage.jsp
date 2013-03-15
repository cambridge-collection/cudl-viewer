<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="header.jsp" />
</head>
<jsp:include page="bodyStart.jsp" />

<div class="clear"></div>

<jsp:include page="searchForm.jsp">
	<jsp:param name="checkedOption" value="AUTHOR"/>
</jsp:include>

<jsp:include page="footer.jsp" />
