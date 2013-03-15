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
<jsp:include page="header.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		$('#resultsTable').dataTable( {
				"sPaginationType": "full_numbers"
		} );
	} );
</script>
</head>
<jsp:include page="bodyStart.jsp" />

<%
	List<FragmentSearchResult> resultSet = ((List<FragmentSearchResult>) request.getAttribute("fragmentResults"));
	GenizahQuery query = ((GenizahQuery) request.getAttribute("query"));
	ItemFactory itemFactory = (ItemFactory) request.getAttribute("itemFactory");
%>

<div class="clear"></div>
<jsp:include page="searchForm.jsp">
	<jsp:param name="queryString" value="<%=query.getQueryString()%>"/>
	<jsp:param name="checkedOption" value="CLASSMARK"/>
</jsp:include>
<section id="content" class="grid_20 content">

		<%
			// No results were returned. So print out some help.
			if (resultSet.size() == 0) {
				out.println("<p class=\"box\">We couldn't find any items matching <b>"
						+ query.getQueryString() + "</b></p>");
			} else {
				out.println("<table id=\"resultsTable\"><thead>");
				out.println("<tr>");
				out.println("<th>Classmark</th>");
				out.println("<th>References To Fragment</th>");
				out.println("<th>Link To Reference List</th>");
				out.println("</tr></thead><tbody>");
				String fragmentBaseURL = "http://cudl.lib.cam.ac.uk/view/";
				for (FragmentSearchResult fragmentResult : resultSet) {
					Fragment fragment = fragmentResult.getFragment();
					String label = fragment.getLabel();
					String classmark = fragment.getClassmark();
					out.println("<tr>");
					out.println("<td>" + label + "</td>");
					out.println("<td>" + fragmentResult.getRefCount() + "</td>");
					String url = "/genizah?query=" + classmark + "&queryType=CLASSMARKID";
					out.println("<td><a href=\"" + url + "\">RefList</a></td>");
					out.println("</tr>");
				}
				out.println("</tbody></table>");
			}
		%>
</section>

<jsp:include page="footer.jsp" />



