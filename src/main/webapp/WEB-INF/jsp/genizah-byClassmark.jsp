<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,
				 java.net.URLEncoder,
				 ulcambridge.foundations.viewer.genizah.*"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<%
	List<Fragment> resultSet = ((List<Fragment>) request.getAttribute("fragments"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));	
%>

<div class="clear"></div>

<section id="content" class="grid_20 content">
	<div class="grid_13 container" id="pagination_container">

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table>");
				for (Fragment fragment : resultSet) {
					out.println("<tr>");
					out.println("<td>" + fragment.getClassmark() + "</td>");
					out.println("</tr>");
				}
				out.println("</table>");
			}
		%>
</div>
</section>

<jsp:include page="footer/footer.jsp" />



