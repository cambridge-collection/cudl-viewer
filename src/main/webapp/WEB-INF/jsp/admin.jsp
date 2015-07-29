<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="-1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Admin" />
</jsp:include>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">
			<div class="campl-content-container">

				<!-- Publish JSON Changes -->
				<div class="panel panel-danger">
					<div class="panel-heading">
						<h3 class="panel-title">1. Publish JSON Changes</h3>
					</div>
					<div class="panel-body">
						<div class="campl-column8">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<ul>
									<li>This button copies any changes made to the Dev master
										branch on cudl-data into a branch that live will read.</li>
									<li><b>Please ensure you have tested your changes in
											Dev thoroughly before publishing.</b></li>
									<li>This button is used you want to update any information
										about an item described in its metadata.</li>
									<li>There will be a short delay before these new items
										appear in the search.</li>
									<li><b>THIS OPERATION CANNOT BE UNDONE</b>.</li>
								</ul>
								<form method="get" action="/admin/publishjson">
									<button type="submit" class="campl-primary-cta"
										onClick="javascript:return confirm('Are you sure you want to copy data from dev to live?')">Publish
										JSON (cudl-data)</button>
								</form>
							</div>
						</div>
					</div>
				</div>

				<!-- Publish Database Changes -->
				<div class="panel panel-danger">
					<div class="panel-heading">
						<h3 class="panel-title">2. Publish Database Changes</h3>
					</div>
					<div class="panel-body">
						<div class="campl-column8">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<ul>
									<li>This button copies any changes made to the Dev
										Database (collections, items and itemsincollection tables)
										from the DEV database to the LIVE database.</li>
									<li><b>Please ensure you have tested your changes in
											Dev thoroughly before publishing.</b></li>
									<li>This button is used when you want to add new items to
										a collection or change the order of a collection on the LIVE
										homepage.</li>
									<li>If you are adding any new items please ensure that
										these items have been successfully published to live by using
										the 'Publish JSON' button first.</li>
									<li><b>THIS OPERATION CANNOT BE UNDONE</b>.</li>
								</ul>
								<form method="get" action="/admin/publishdb">
									<button type="submit" class="campl-primary-cta"
										onClick="javascript:return confirm('Are you sure you want to copy database from dev to live?')">Publish
										Database Changes.</button>
								</form>
							</div>
						</div>
					</div>
				</div>


				<!-- Publish Database Changes -->
				<div class="panel panel-danger">
					<div class="panel-heading">
						<h3 class="panel-title">3. Publish Website Content</h3>
					</div>
					<div class="panel-body">
						<div class="campl-column8">
							<div class="campl-content-container campl-horizontal-teaser-txt">
								<ul>
									<li>This button copies any changes made to the Dev master
										branch on cudl-viewer-content into a branch that live will
										read.</li>
									<li><b>Please ensure you have tested your changes in
											Dev thoroughly before publishing.</b></li>
									<li>This button is used when you want to push any changes
										made to the website text and images into the live website.</li>
									<li>If you are adding any new items or collections please
										ensure that these items have been successfully published to
										live by using the 'Publish JSON' and 'Publish Database Changes' buttons
										first.</li>
									<li><b>THIS OPERATION CANNOT BE UNDONE</b>.</li>
								</ul>
								<form method="get" action="/admin/publishcontent">
									<button type="submit" class="campl-primary-cta"
										onClick="javascript:return confirm('Are you sure you want to copy content from dev to live?')">Publish
										Website Content (cudl-viewer-content)</button>
								</form>
							</div>
						</div>
					</div>
				</div>
				
				<p><a href="/admin/auth/logout">Logout</a></p>

			</div>
		</div>
	</div>
</div>

<jsp:include page="header/footer-full.jsp" />


