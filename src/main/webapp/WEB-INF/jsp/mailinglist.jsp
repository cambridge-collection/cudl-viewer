<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>

<head>

<title>Cambridge Digital Library - Feedback</title>
<link rel="stylesheet" href="/styles/uoc.min.css">
<!--[if lt IE 9]>
   <link rel="stylesheet" href="/styles/ie.min.css"/>
   <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<style>
body {
	background: #ffffff;
}

.mailinglistform {
	color: black;
	background: #ffffff;
	padding-bottom: 18px;
}

.mailinglistform input,.mailinglistform textarea {
	border: 1px inset;
	margin: 3px;
	padding: 2px;
}

.error {
	color: red;
}
</style>
</head>
<body>
	<div class="grid_11 container">

		<h3>Keep me informed</h3>


		<div class="mailinglistform">
			<form:form method="post" commandName="mailingListForm">

				<p>If you would like to receive details of any updates or
					changes to this site, then join our mailing list. Our email service
					will alert you when new material is added to this website. Just
					fill in the form below (fields marked with a * are required).</p>
				<p>
					<form:label path="name">Name:</form:label>
					<em>*</em>
					<form:input path="name" />
					<form:errors path="name" cssClass="error" />
				</p>
				<p>
					<form:label path="email">E-Mail:</form:label>
					<em>*</em>
					<form:input path="email" name="email" size="30" />
					<form:errors path="email" cssClass="error" />
				</p>

				<input type="submit" name=submit value="Sign up">
			</form:form>

		</div>
	</div>

</body>
</html>





