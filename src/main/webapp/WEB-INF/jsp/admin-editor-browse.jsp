<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="cudlfn" uri="/WEB-INF/cudl-functions.tld" %>


<cudl:base-page pagetype="ADMIN_FILE_BROWSE">
	<jsp:attribute name="pageData">
		<cudl:default-context>
			<json:property name="currentDir" value="${currentDir}"/>
			<json:property name="ckEditor" value="${ckEditor}"/>
			<json:property name="ckEditorFunctionId" value="${ckEditorFuncNum}"/>
			<json:property name="language" value="${langCode}"/>
		</cudl:default-context>
	</jsp:attribute>
	<jsp:body>
		<div>
			<nav class="navbar navbar-default">
				<div class="container">
					<div class="navbar-header">
						<a class="navbar-brand"
							href="?CKEditor=${cudlfn:uriEnc(ckEditor)}&CKEditorFuncNum=${cudlfn:uriEnc(ckEditorFuncNum)}&langCode=${cudlfn:uriEnc(langCode)}&browseDir=${cudlfn:uriEnc(homeDir)}">Browse
							Server Images</a>
					</div>

					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav">
							<li><a
								href="?CKEditor=${cudlfn:uriEnc(ckEditor)}&CKEditorFuncNum=${cudlfn:uriEnc(ckEditorFuncNum)}&langCode=${cudlfn:uriEnc(langCode)}&browseDir=${cudlfn:uriEnc(homeDir)}">Home</a></li>
						</ul>

						<p class="navbar-text navbar-right">
							<a href="#" class="navbar-link btn-add-image">Add image</a>
						</p>
					</div>
				</div>
			</nav>

			<div class="container" id="content">

				<h3><c:out value="${currentDir}"/></h3>

				<c:set var="filesPerRow" value="${4}"/>

				<c:forEach items="${imageFiles.children}" var="child" varStatus="loop">
					<c:set var="isDirectory" value="${child.type == 'DIRECTORY'}"/>

					<c:if test="${loop.index % filesPerRow == 0}">
						<div class="row">
					</c:if>

					<div class="col-xs-6 col-md-3">
						<div class="thumbnail type-${fn:toLowerCase(child.type)}"
							<cudl:attr name="data-file">
								<json:object escapeXml="false">
									<json:property name="name" value="${child.filename}"/>
									<json:property name="type" value="${child.type}"/>
									<json:property name="url" value="${child.fileURL}"/>
								</json:object>
							</cudl:attr>
							>
							<c:choose>
								<c:when test="${isDirectory}">
									<img src="/img/general/folder.png" title="directory">
								</c:when>
								<c:otherwise>
									<a class="fancybox" href="${fn:escapeXml(child.fileURL)}">
										<img src="${fn:escapeXml(child.fileURL)}" alt="">
									</a>
								</c:otherwise>
							</c:choose>

							<div class="caption">
								<p>
									<c:out value="${child.filename}"/>
								</p>
								<p>
									<c:choose>
										<c:when test="${isDirectory}">
											<a href="?CKEditor=${cudlfn:uriEnc(ckEditor)}&CKEditorFuncNum=${cudlfn:uriEnc(ckEditorFuncNum)}&langCode=${cudlfn:uriEnc(langCode)}&browseDir=${cudlfn:uriEnc(child.filePath)}"
												class="btn btn-primary" role="button">Open</a>
										</c:when>
										<c:otherwise>
											<a href="#" class="btn btn-primary btn-select" role="button">Select</a>
										</c:otherwise>
									</c:choose>

									<a href="#" class="btn btn-default btn-delete" role="button">Delete</a>
								</p>
							</div>
						</div>
					</div>

					<c:if test="${loop.index % 4 == filesPerRow - 1}">
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>

		<div id="addFile" style="display: none" class="alert alert-info" role="alert">
			<button type="button" class="close btn-close" aria-label="Close">
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
							<span class="input-group-addon" id="folderSelect"> Folder</span>
							<input type="text" name="directory" class="form-control"
								value="${fn:escapeXml(currentDir)}" aria-describedby="basic-addon1">
						</div>

						<input class="input" type="submit" value="Upload File">
					</form>

				</div>
			</div>
		</div>

		<div id="deleteFile" style="display: none" class="alert " role="alert">
			<button type="button" class="close btn-close" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>


			<div class="panel panel-danger">

				<div class="panel-heading">DELETE</div>
				<div class="panel-body">
					<div id="deleteInfo"></div>
					<p>
						<b>This operation CANNOT be undone. </b>
					</p>
				</div>
				<div class="panel-footer">
					<a href="#" class="btn btn-danger btn-delete">Delete</a>
					<a href="#" class="btn btn-close">Cancel</a>
				</div>
			</div>
		</div>
	</jsp:body>
</cudl:base-page>
