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

#deleteFile {
	position: fixed;
	width: 500px;
	margin: -300px auto auto -250px;
	top: 50%;
	left: 50%;
	text-align: center;
}

#deleteFile .input {
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


</head>
<body>

	<div>
		<nav class="navbar navbar-default">
			<div class="container">
				<div class="navbar-header">
					<a class="navbar-brand"
						href="?CKEditor=<%=ckEditor%>&CKEditorFuncNum=<%=ckEditorFuncNum%>&langCode=<%=langCode%>&browseDir=<%=homeDir%>">Browse
						Server Images</a>
				</div>

				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li><a
							href="?CKEditor=<%=ckEditor%>&CKEditorFuncNum=<%=ckEditorFuncNum%>&langCode=<%=langCode%>&browseDir=<%=homeDir%>">Home</a></li>
					</ul>

					<p class="navbar-text navbar-right">
						<a href="#" onclick="return openAddFile()" class="navbar-link">Add
							image</a>
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
								onclick="return openDeleteFile('<%=currentDir%>/<%=child.getFilename()%>', <%=child.isDirectory()%>)"
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
								onclick="return openDeleteFile('<%=currentDir%>/<%=child.getFilename()%>', <%=child.isDirectory()%>)"
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

	<div id="addFile" style="display: none" class="alert alert-info"
		role="alert">
		<button type="button" class="close" onclick='$("#addFile").hide()'
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>

		<h4>Upload an image to the server</h4>

		<div class="panel">

			<div class="panel-heading">Select file and specify a directory
				to upload to.</div>
			<div class="panel-body">
				<form id="addFileForm" enctype="multipart/form-data" method="POST"
					dir="ltr" lang="en">

					<input class="input" type="file" name="upload" accept="image/*" />

					<div class="input-group">
						<span class="input-group-addon" id="folderSelect"> Folder</span> <input
							type="text" name="directory" class="form-control"
							value="<%=currentDir%>" aria-describedby="basic-addon1">
					</div>

					<input class="input" type="submit" value="Upload File" />
				</form>

			</div>
		</div>
	</div>

	<div id="deleteFile" style="display: none" class="alert " role="alert">
		<button type="button" class="close" onclick='$("#deleteFile").hide()'
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>


		<div class="panel panel-danger">

			<div class="panel-heading">DELETE</div>
			<div class="panel-body">
				<div id="deleteInfo"></div>
				<p>
					<b>This operation CANNOT be undone. </b>
				</p>
				<form id="deleteFileForm" method="POST" action="">

					<input type="hidden" name="filePath" value="" />
				</form>

			</div>
			<div class="panel-footer">

				<a href="#" onclick='$("#deleteFileForm").submit()'
					id="deleteButton" class="btn btn-danger">Delete</a> <a href="#"
					class="btn " onclick='$("#deleteFile").hide()'>Cancel</a>

			</div>
		</div>
	</div>

	<script>

    $(".fancybox").fancybox();

	var select = function(url) {
		window.opener.CKEDITOR.tools.callFunction(<%=ckEditorFuncNum%>, url);
		window.close();
	}

	var openDeleteFile = function(file, isFolder) {
		
		if (isFolder) {			
			$("#deleteInfo").html("<p>Only empty folders can be deleted.</p>");			
		} else {
			$("#deleteInfo").html("<p>Please make VERY sure this image is not used in any web pages before deleting it.</p>");			
		}
		document.forms["deleteFileForm"]["filePath"].value=file;
		$("#deleteFile").show();

		return false;
	}
	
	$("#deleteFileForm").submit(function() {

		var url = "/editor/delete/image"; 

	    $.ajax({
		    type: "POST",
		    url: url,
		    data: $("#deleteFileForm").serialize(), 		    		           
			success: function(data)
			{
			  location.reload(); // reload page. 
			}
		});
        return false;
        
	});	


	var openAddFile = function() {
		$("#addFile").show();

		return false;
	}

	function validateAddForm() {

		var upload = document.forms["addFileForm"]["upload"].value;
		var fileRegEx1 = /^.*\.(jpg|jpeg|png|gif|bmp)$/i;
		if (!fileRegEx1.test(upload)) {
			alert("Select an image file with the file extension .jpg .jpeg .bmp .png or .gif");
			return false;
		}

		var fileRegEx2 = /^[-_A-Za-z0-9]+\.(jpg|jpeg|png|gif|bmp)$/i;
		if (!fileRegEx2.test(upload)) {
			alert("The image file name must only contain the characters A-Z or 0-9 or - or _ without spaces.");
			return false;
		}

		var dir = document.forms["addFileForm"]["directory"].value;
		var dirRegEx = /^[-_/A-Za-z0-9]*$/;
		if (!dirRegEx.test(dir)) {
			alert("Folder name must only contain the characters A-Z or 0-9 or / or - or _ without spaces.");
			return false;
		}

		return true;

	}
	
	$("#addFileForm").submit(function() {

	    var url = "/editor/add/image?CKEditor=<%=ckEditor%>&CKEditorFuncNum=<%=ckEditorFuncNum%>&langCode=<%=langCode%>"; 

	    if (validateAddForm()) {	    	
		    	
		  $.ajax({
		           type: "POST",
		           url: url,
		           data: new FormData(this), 
		           cache: false,
		           contentType: false,
		           processData: false,		           
		           success: function(data)
		           {
		        	  location.reload(); //reload page. 
		           }
		         });

		   }
		   return false; 
	});
				


</script>
</body>
</html>


