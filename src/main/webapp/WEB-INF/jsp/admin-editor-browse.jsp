<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.admin.BrowseFile, java.util.List"%>
<html>
<head>

<!-- JQuery -->
<script type="text/javascript" src="/scripts/jquery-1.11.1.min.js"></script>

<!--  bootstrap -->
<link rel="stylesheet" href="/styles/bootstrap-default.min.css">
<script src="/scripts/bootstrap.min.js"></script>
<link rel="stylesheet" href="/styles/font-awesome/font-awesome.min.css">

<!-- Fancybox -->
<link rel="stylesheet" href="/scripts/fancybox/jquery.fancybox.css"
	type="text/css" media="screen" />
<script type="text/javascript"
	src="/scripts/fancybox/jquery.fancybox.pack.js"></script>

<style>
a.campl-external {
	background: #fff;
	padding-right: 0px;
}

.thumbnail {
	height: 210px;
}

.thumbnail .caption {
	position: absolute;
	bottom: 30px;
}

.fancybox img {
	max-height: 100px;
}

#addFile {
	position: fixed;
	width: 500px;
	margin: -300px auto auto -250px;
	top: 50%;
	left: 50%;
	text-align: center;
}

#addFile .input {
	margin: 10px;
}
</style>
<%
	BrowseFile files = (BrowseFile) request.getAttribute("imageFiles");
   String ckEditor = (String) request.getParameter("CKEditor");
   String ckEditorFuncNum = (String) request.getParameter("CKEditorFuncNum");
   String langCode = (String) request.getParameter("langCode");
   String browseDir = request.getParameter("browseDir");
   String homeDir = (String) request.getAttribute("homeDir");
   String currentDir = (String) request.getAttribute("currentDir");
%>
<script>
 	var select = function(url) {
		window.opener.CKEDITOR.tools.callFunction(<%=ckEditorFuncNum%>, url);
		window.close();
	}
	
	var openAddFile = function () {
		$("#addFile").show();
	} 
	
	$(".fancybox").fancybox();
</script>

</head>
<body>

	<div>
		<nav class="navbar navbar-default">
			<div class="container">
				<div class="navbar-header">
					<a class="navbar-brand"
						href="?CKEditor=<%=ckEditor%>&CKEditorFuncNum=<%=ckEditorFuncNum%> &langCode=<%=langCode%>&browseDir=<%=homeDir%>">Browse
						Server Images</a>
				</div>

				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li><a
							href="?CKEditor=<%=ckEditor%>&CKEditorFuncNum=<%=ckEditorFuncNum%>&langCode=<%=langCode%>&browseDir=<%=homeDir%>">Home</a></li>
					</ul>

					<p class="navbar-text navbar-right">
						<a href="#" onclick="openAddFile()" class="navbar-link">Add image</a>
					</p>
				</div>
			</div>
		</nav>

		<div class="container" id="content">

			<h3><%=currentDir%></h3>

			<%
				List<BrowseFile> children = files.getChildren();
				for (int i = 0; i < children.size(); i++) {
					BrowseFile child = children.get(i);

					if (i % 4 == 0) {
						out.print("<div class=\"row\">");
					}
			%>
			<div class="col-xs-6 col-md-3">
				<div class="thumbnail">
					<%
						if (child.isDirectory()) {
					%>
					<img src='/images/folder.png' title='directory' />

					<div class="caption">
						<p>
							<%=child.getFilename()%>
						</p>
						<p>
							<a
								href="?CKEditor=<%=ckEditor%>&CKEditorFuncNum=<%=ckEditorFuncNum%>&langCode=<%=langCode%>&browseDir=<%=child.getFilePath()%>"
								class="btn btn-primary" role="button">Open</a> <a href="#"
								onclick="delete('<%=child.getFileURL()%>')"
								class="btn btn-default" role="button">Delete</a>
						</p>
					</div>

					<%
						} else {
					%>

					<a class='fancybox' href='<%=child.getFileURL()%>'> <img
						src='<%=child.getFileURL()%>' alt='' /></a>

					<div class="caption">
						<p>
							<%=child.getFilename()%>
						</p>
						<p>
							<a href="#" onclick="select('<%=child.getFileURL()%>')"
								class="btn btn-primary" role="button">Select</a> <a href="#"
								onclick="delete('<%=child.getFileURL()%>')"
								class="btn btn-default" role="button">Delete</a>
						</p>
					</div>
					<%
						}
					%>
				</div>

			</div>
			<%
				if (i % 4 == 3) {
						out.print("</div>");
					}
				}
			%>



		</div>
	</div>

	<div id="addFile" style="display:none" class="alert alert-info" role="alert">
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>

		<h4>Upload an image to the server</h4>

		<div class="panel panel-primary">

			<div class="panel-heading">
				Select file and specify a directory to upload to.
			</div>
			<div class="panel-body">
				<form action="/editor/upload" method="GET">
					<input class="input" type="file" name="upload" accept="image/*" />

					<div class="input-group">
						<span class="input-group-addon" id="folderSelect">
							Folder</span> <input type="text" class="form-control"
							placeholder="<%=currentDir%>" aria-describedby="basic-addon1">
					</div>

					<input type="hidden" name="CKEditorFuncNum" value="0" /> <input
						class="input" type="submit" value="Upload File" />
				</form>

			</div>
		</div>
	</div>


</body>
</html>


