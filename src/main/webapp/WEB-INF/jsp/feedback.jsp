<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

.feedbackform {
	color: black;
	background: #ffffff;
	padding-bottom: 18px;
}

.feedbackform input,.feedbackform textarea {
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

		<h3>Feedback</h3>


		<div class="feedbackform">
			<form:form method="post" commandName="feedbackForm">

				<p>We welcome all comments, feedback and suggestions. Please
					tell us what you liked, disliked, would like to see improved, or if
					you think something is not right. Fields marked with a * are
					required.</p>

				<p>				
					<form:label path="name">Name:</form:label> <em>*</em><form:input path="name" />
					<form:errors path="name" cssClass="error"/>
				</p>
				<p>
					<form:label path="email">E-Mail:</form:label> <em>*</em><form:input path="email"
						name="email" size="30" />
					<form:errors path="email" cssClass="error"/>
				</p>
				<p>

					<form:label path="comment">Your comment:</form:label> <em>*</em>
					<form:textarea path="comment" name="comment" cols="50" rows="4"></form:textarea>
					<form:errors path="comment" cssClass="error"/>						
				</p>

				<input type="submit" name=submit value="Send Feedback">
			</form:form>

		</div>
	</div>

</body>
</html>





