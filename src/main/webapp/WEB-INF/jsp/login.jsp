<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="2" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Login" />
</jsp:include>

<!-- Simple OpenID Selector -->

<script type="text/javascript" src="/scripts/jquery-1.6.3.min.js"></script>
<script type="text/javascript" src="/scripts/openid/openid-jquery.js"></script>
<script type="text/javascript" src="/scripts/openid/openid-en.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		openid.init('openid_identifier');
		//openid.setDemoMode(true); //Stops form submission for client javascript-only test purposes
	});
</script>

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

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column12  campl-main-content" id="content">
			<div class="campl-content-container">

                <div id="login_error">${error}</div>

				<div class="campl-column12">Login to create or view your
					collection of bookmarks.</div>
				<div class="campl-column4">&nbsp;</div>
				<div class="campl-column4 campl-login-form">
					<div class="campl-content-container">
						<form id="openid_form" method="post"
							action="/j_spring_openid_security_check">
							<fieldset>
								Sign in with your<br /> <img src="/images/google-logo.jpg"
									width="20px" height="20px"> <span class="google_account">Google
									Account</span><br />

								<button type="button" class="btn btn-primary"
									onclick="javascript:openid.signin('google');">Sign In</button>

								<div>
									<a title="log in with OpenID" class="login_alt_link"
										href="javascript:openid.signin('openid');"
										onclick="showOrHideOpenId()">Use an alternative OpenID
										provider</a>
								</div>

								<div id="openid_input_area" style="display: none">
									<div class="campl-control-group">
										<input type="hidden" value="verify" name="action"> <label
											for="openid_identifier">Enter your OpenID</label>
										<div class="campl-controls">
											<input id="openid_identifier" value="http://"
												class="campl-input-block-level" type="text">
										</div>
									</div>
			
									<button type="submit" class="campl-btn campl-primary-cta campl-float-right">Sign In2</button>	
								</div>
					</div>
					</fieldset>
					</form>
				</div>
			</div>
			<div class="campl-column4"></div>
			<div class="campl-column12">
				<br />
				<br />
			</div>

		</div>
	</div>
</div>


<jsp:include page="header/footer-full.jsp" />


