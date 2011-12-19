<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div id="metadata" class="document-about">
	<div style="height: 100%; overflow-y: auto;">

		<!-- AddThis Button BEGIN -->
		<div class="addthis">
			<div class="addthis_toolbox addthis_default_style ">
				<a class="addthis_button_email"></a><a
					class="addthis_button_facebook"></a> <a
					class="addthis_button_twitter"></a> <a
					class="addthis_button_compact"></a>
			</div>
			<script type="text/javascript"
				src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4ec62d915a6efb9f"></script>
		</div>
		<!-- AddThis Button END -->

		<div id="metadata-about">

			<!-- Title -->
			<br /> <span class="document-about-title" id="metadata-title"></span>
			<span id="metadata-author"></span> <br />
			<div class="document-spacer"></div>
			&nbsp;Part of the <a href="${collectionURL}">${collectionTitle}</a>.


			<!-- Metadata -->
			<br /> <br />

			<div id="metadata-abstract"></div>

			<div style="width: 100%; height: 240px">
				<!--  About the document -->
				<div class="rbroundbox">
					<div class="rbtop">
						<div></div>
					</div>
					<div class="rbcontent">
						<div class="document-about-doc">
							Physical location: <span id="metadata-physicalLocation"></span> <br />
							Classmark: <span id="metadata-shelfLocation"></span> <br />
							Subject: <span id="metadata-subject"></span> <br /> Date
							created: <span id="metadata-dateCreatedDisplay"></span>
						</div>
					</div>
					<div class="rbbot">
						<div></div>
					</div>
				</div>

				<!--  About the page -->
				<div class="rbroundbox">
					<div class="rbtop">
						<div></div>
					</div>
					<div class="rbcontent">
						<!--  about the page -->
						<div class="document-about-page">
							Current page/folio: <span id="metadata-page"></span> <br /> <br />
							<b><a href=""
								onclick="bookmarkPage('Cambridge Digital Library', docURL+'/'+pagenum); return false;">Bookmark
									direct link to this page</a> </b> <br /> <b><a href=""
								onclick="downloadImageCheck(); return false;">Download image
									for this page</a> </b><br /> <b><a
								href="http://www.lib.cam.ac.uk/deptserv/imagingservices/rights_form/rights_form.html"
								target="_blank">Request reproduction rights to this image</a> </b>

						</div>
					</div>
					<div class="rbbot">
						<div></div>
					</div>
				</div>
			</div>
			<!--  About the rights -->
			<br /> <span id="metadata-display-rights"></span> <br />
		</div>
	</div>
</div>