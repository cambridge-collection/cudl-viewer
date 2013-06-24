<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="header/header-login.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<script>
	function showOrHideOpenId() {
		var div = document.getElementById('openid_input_area');
		if (div.style.display = 'none') {
			div.style.display = 'inline';
		} else {
			div.style.display = 'none';
		}
	}

	function onSumbitMyOpenId() {
		var username = document.forms["myopenid_form"]["openid_username"].value;
		document.forms["myopenid_form"]["openid_identifier"].value = "http://"
				+ username + ".myopenid.com/";
		return true;
	}
</script>

<div class="grid_20">
	<div class="clear"></div>

	<section id="content" class="grid_20 content ">
		<h3>Access Denied</h3>
		<div id="error">${error}</div>

		<div class="grid_18">
			You do not have permission to access the page requested. <br /> <br />
		</div>
	</section>
		
</div>
<jsp:include page="footer/footer.jsp" />