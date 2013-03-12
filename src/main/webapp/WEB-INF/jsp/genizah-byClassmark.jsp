<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="java.util.*,
				 java.net.URLEncoder,
				 ulcambridge.foundations.viewer.ItemFactory,
				 ulcambridge.foundations.viewer.JSONReader,
				 ulcambridge.foundations.viewer.dao.*,
				 ulcambridge.foundations.viewer.model.Item,
				 ulcambridge.foundations.viewer.genizah.*
				 "%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav-search.jsp" />

<link rel="stylesheet" href="/styles/genizah.css"/>

<%
	List<Fragment> resultSet = ((List<Fragment>) request.getAttribute("fragments"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
	ItemFactory itemFactory = (ItemFactory) request.getAttribute("itemFactory");
%>

<div class="clear"></div>
<jsp:include page="genizah-Search.jsp">
	<jsp:param name="checkedOption" value="CLASSMARK"/>
</jsp:include>
<section id="content" class="grid_20 content">

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>Classmark</th>");
				out.println("<th>Title</th>");
				out.println("<th>Abstract</th>");
				out.println("</tr>");
				String fragmentBaseURL = "http://cudl.lib.cam.ac.uk/view/";
				for (Fragment fragment : resultSet) {
					String classmark = fragment.getClassmark();
					out.println("<tr>");
					
					
					Item item = null;
					if (itemFactory != null) {
						item = itemFactory.getItemFromId(classmark);
					}
					if (item == null) {
						out.println("<td>" + fragment.getLabel() + "</td>");
						out.println("<td class=\"emptyRow\">" + "NOT FOUND" + "</td>");	
					} else {
						out.println("<td><a href=\"" + fragmentBaseURL + classmark + "\">");
						out.println(fragment.getLabel() + "</a></td>");
						out.println("<td>" + item.getTitle() + "</td>");
						String itemAbstract = item.getAbstract();
						if (itemAbstract.equals("")) {
							out.println("<td class=\"emptyCell\"></td>");
						} else {
							out.println("<td>" + itemAbstract + "</td>");
						}
					}
					out.println("</tr>");
				}
				out.println("</table>");
			}
		%>
</section>

<jsp:include page="footer/footer.jsp" />



