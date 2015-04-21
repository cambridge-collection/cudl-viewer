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

<script type="text/javascript" src="/scripts/jquery-1.6.3.min.js"></script>

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
						<form id="openid_form" method="post" action="/auth/oauth2/google">
							<fieldset>
								Sign in with your<br /> <img src="/images/google-logo.jpg"
									width="20px" height="20px"> <span class="google_account">Google
									Account</span><br />
								<button type="submit" class="btn btn-primary">Sign In</button>
							</fieldset>
						</form>
					</div>
				</div>
				<div class="campl-column4"></div>
				<div class="campl-column12">
					<br /> <br />
				</div>
				<div class="campl-column12 center">

					<div class="alert alert-danger" role="alert">Note: Due to
						changes in the method of authentication some bookmarks stored in
						older accounts may need to be recreated.</div>
				</div>
			</div>


		</div>
	</div>
</div>


<jsp:include page="header/footer-full.jsp" />


