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
		<h3>My Library</h3>

		<div id="error">${error}</div>
		<div class="grid_18">
			Sign in to create or view your collection of bookmarks. <br /> <br />
		</div>
		<div class="grid_8 box">

			<div style="width: 190px">
				<form id="openid_form" method="post"
					action="/j_spring_openid_security_check">

				<div
					style="background-color: #333; color: #fff; padding: 5px; width: 180px">

					<a style="color: #fff; text-decoration: none"
						href="javascript:openid.signin('CUDLIdp')" >Sign in ></a>
	
				</div>
				
					<div class="grid_7">
						<br />Alternatively you can use
					</div>
					<div class="grid_8">
						<a class="google openid_large_btn"
							style="background: #FFF url(../../img/openid/openid-providers-en.png); background-position: 0px 0px"
							href="javascript:openid.signin('google');"
							title="log in with Google"></a> <a class="yahoo openid_large_btn"
							style="background: #FFF url(../../img/openid/openid-providers-en.png); background-position: -100px 0px"
							href="javascript:openid.signin('yahoo');"
							title="log in with Yahoo"></a>
							 <a class="openid openid_large_btn"
							onclick="showOrHideOpenId()"
							style="background: #FFF url(../../img/openid/openid-providers-en.png); background-position: -400px 0px"
							href="javascript:openid.signin('openid');"
							title="log in with OpenID"></a>
							
					</div>
					<div class="grid_9">
						<div id="openid_input_area" style="display: none;">

							<input type="hidden" value="verify" name="action">
							<p>Enter your OpenID.</p>
							<input id="openid_identifier" type="text" value="http://"
								name="openid_identifier"
								style="background: #FFF url(../../img/openid/openid-inputicon.gif) no-repeat scroll 0 50%; padding-left: 18px;">
							<input id="openid_submit" type="submit" value="Sign-In">

						</div>
					</div>
				</form>
			</div>
		</div>

</div>
<jsp:include page="footer/footer.jsp" />