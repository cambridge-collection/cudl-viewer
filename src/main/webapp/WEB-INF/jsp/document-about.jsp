<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="java.net.URLEncoder, ulcambridge.foundations.viewer.model.*, java.util.List"%>

<% 
	List<Collection> collections = (List<Collection>) request.getAttribute("collections");
%>
<div id="metadata" class="document-about box">
	<div style="height: 100%; overflow-y: auto; overflow-x: auto;">

		<div id="metadata-about" style="display: none">

			<!-- Title -->
			<div class="document-about-box">
				<span class="document-about-title" id="metadata-title"></span><br />
				<span id="metadata-author"></span> <br />
				<div class="document-spacer"></div>
				<% for (int i=0; i<collections.size(); i++) { 
					Collection collection = collections.get(i);
				%>
				Part of the <a class="cudlLink" href="<%=collection.getURL()%>"><%=collection.getTitle()%></a>
				Collection. <br />
				<% } %>
				<br /> 
				<!-- Abstract -->
				<div id="metadata-abstract"></div>
				<div id="metadata-relatedresources"></div>
			</div>

			<br />

			<!--  Metadata -->
			<div class="document-about-box">
				<b>Expand for detailed description</b><br /> <br />
				<ul id="tree" style="margin-left: 0px;">
				</ul>

			</div>

			<br />

			<!--  What can I do? -->
			<div class="document-about-box">
			
				
				<b>What can I do with this image?</b><br /> <br />
				<div class="document-about-page">
				    <div id=download_img>
					<a href="" onclick="cudl.downloadImageCheck(); return false;">
						<img src="/img/icon-download-blue.png"
						style="float: left; padding-right: 3px" /> Download image
					</a> <span id="metadata-download-rights" class="rights-statement"></span>
				     </div>

					<br /> <br /> <a id="metadata-rights-link"
						href="http://www.lib.cam.ac.uk/deptserv/imagingservices/rights_form/rights_form.html"
						target="_blank"><img src="/img/icon-reproduction-blue.png"
						style="float: left; padding-right: 3px" /> Request reproduction
						rights to this image</a> <span id="metadata-display-rights"
						class="rights-statement"></span> 
						
					<br /> <br /> <a
						id="bookmarkLink"
						onclick="cudl.bookmarkImageCheck(); return false;"
						href=""><img
						src="/img/icon-bookmark-blue.png"
						style="float: left; padding-right: 3px" /> Bookmark this image</a> <br />

					<br /><br />  
			
					<a href="" onclick="cudl.linkImageCheck(); return false;">
						<img src="/img/icon-link.png"
						style="float: left; padding-right: 3px" /> Share the link to this image
					</a>

					<br /><br /> <br />   
										
					<!-- AddThis Button BEGIN -->
					<div>
						<div class="grid_5">
							<div class="addthis_toolbox addthis_default_style ">

								<a class="addthis_button_email"></a><a
									class="addthis_button_facebook"></a> <a
									class="addthis_button_twitter"></a> <a
									class="addthis_button_compact"></a> <a
									class="addthis_counter addthis_bubble_style"></a>
							</div>
							<script type="text/javascript"
								src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=xa-4ec62d915a6efb9f"></script>
						</div>
					</div>
					<!-- AddThis Button END -->


				</div>

			<br /> <br />
			<i>
			<div id="metadata-docAuthority"></div>
			</i>
			
			</div>

			<br /> <br /> <br /> <br />
		</div>
	</div>
</div>
