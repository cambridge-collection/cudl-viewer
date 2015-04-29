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

<link rel="stylesheet" type="text/css"
	href="/styles/bootstrap-social.css">
<link rel="stylesheet" href="/styles/font-awesome/font-awesome.min.css">

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column12  campl-main-content" id="content">
			<div class="campl-content-container">

				<div id="login_error">${error}</div>

				<div class="campl-column12">Login to create or view your
					collection of bookmarks.</div>
				<div class="campl-column12">&nbsp;</div>
				<div class="campl-column12">
					<div class="campl-column4 campl-login-form">
						<div class="campl-content-container">
							<form class="openid_form" method="post"
								action="/auth/oauth2/google">
								<a class="btn btn-block btn-social btn-google" href="/auth/oauth2/google"> <i
									class="fa fa-google"></i> Sign In with Google  
								</a>
							</form>
							<br />
							<form class="openid_form" method="post"
								action="/auth/oauth2/facebook">
								<a class="btn btn-block btn-social btn-facebook" href="/auth/oauth2/facebook"> <i
									class="fa fa-facebook"></i> Sign In with Facebook
								</a>
							</form>
							<br />
							<form class="openid_form" method="post"
								action="/auth/oauth2/linkedin">
								<a class="btn btn-block btn-social btn-linkedin" href="/auth/oauth2/linkedin"> <i
									class="fa fa-linkedin"></i> Sign In with LinkedIn
								</a>
							</form>
						</div>
					</div>
				</div>

				<div class="campl-column12">
					<br /> <br />
				</div>
				<div class="campl-column12 center">

				</div>
			</div>


		</div>
	</div>
</div>


<jsp:include page="header/footer-full.jsp" />


