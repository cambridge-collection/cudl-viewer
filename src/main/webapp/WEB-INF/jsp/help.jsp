<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="4" />
	<jsp:param name="displaySearch" value="true" />
</jsp:include>

<div class="clear"></div>

<section id="content" class="grid_20 content">
<div class="grid_16">

	<div class="panel light faq">

		<h3 style="margin-left: 8px">Help: FAQ</h3>

		<div class="grid_11">

			<ol>
				<li><a href="#mailinglist"><b>How can I find out when
							new content is made available?</b> </a></li>
				<li><a href="#links"><b>How do link to a specific page
							within a book?</b> </a></li>
				<li><a href="#view"><b>How do I change page while
							seeing as much of the manuscript as possible?</b> </a></li>
				<li><a href="#socialmedia"><b>How do I share a book on
							Facebook, Twitter, etc.?</b> </a></li>
				<li><a href="#tech"><b>What technologies are behind the
							Cambridge Digital Library?</b> </a></li>
				<li><a href="#standards"><b>What kind of standards do
							you use for data?</b> </a></li>
				<li><a href="#pdf"><b>Will you be offering whole pdfs
							of the manuscripts to download (for e-readers for example)?</b> </a></li>
				<li><a href="#digitisation"><b>Why can't I see all
							books in Cambridge University Library online?</b> </a></li>
				<li><a href="#searchexample"><b>What types of searches
							do you support?</b> </a></li>
			</ol>
		</div>
		<div class="grid_11">

			<p>
				<a id="mailinglist"><b>How can I find out when new content
						is made available?</b> </a>
			</p>

			<p>
				Subscribe to our mailing list by sending an email to <a
					href="mailto:cudl-updates-subscribe@caret.cam.ac.uk?subject=Subscribe&amp;body=I%20would%20like%20to%20receive%20update%20news%20from%20Cambridge%20University%20Digital%20Library.">cudl-updates-subscribe@caret.cam.ac.uk</a>
				and we will do our best to keep you informed of updates.
			</p>

			<p>
				<a id="links"><b>How do link to a specific page within a
						book?</b> </a>
			</p>

			<p>Our website URLs have the format:</p>
			<p>http://cudl.lib.cam.ac.uk/view/[item id]/[image number]</p>
			<p>
				So <a href="/view/MS-ADD-03996/5">http://cudl.lib.cam.ac.uk/view/MS-ADD-03996/5</a>
				links to image 5 in item MS-ADD-03996 (The Trinity College
				Notebook).
			</p>


			<p>
				<a id="view"><b>How do I change page while seeing as much of
						the manuscript as possible?</b> </a>
			</p>

			<p>The right panel, which shows some information about the item
				can be collapsed by clicking on the double arrow icon in the far
				right of the screen. This will allow you to view more of the image
				while you can still page forward and backwards through the book.
				There is also a button within the viewer that allows you to expand
				it to cover the full screen.</p>

			<p>
				<a id="socialmedia"><b>How do I share a book on Facebook,
						Twitter, etc.?</b> </a>
			</p>

			<p>When you are looking at an item there are social media icons
				in the top right of the About tab. Clicking on these will allow you
				to share the item with others.</p>
			<p>
				<a id="tech"><b>What technologies are behind the Cambridge
						Digital Library?</b> </a>
			</p>

			<p>
				We aim to use primarily open source technologies, and to avoid the
				use of plugins to make the accessible to as many people as possible.
				For the website we use a collection of JavaScript libraries
				including <a
					href="http://gallery.expression.microsoft.com/SeadragonAjax"
					target="_blank">Seadragon Ajax</a> for the zoomable images, <a
					href="http://www.sencha.com/products/extjs" target="_blank">ExtJS</a>,
				<a href="http://jquery.com/" target="_blank">JQuery</a> and <a
					href="http://www.bbc.co.uk/glow/" target="_blank">Glow</a>. Behind
				the scenes we use <a href="http://xtf.cdlib.org/" target="_blank">
					the eXtensible Text Framework (XTF)</a> and <a
					href="http://www.goobi.org/" target="_blank">Goobi</a> for
				digitising and indexing the data. The website was created by the
				digital library team at Cambridge University Library and is written
				in Java. You can find out more by contacting the Development Team
				(see our <a href="/contributors/">Contributors Page</a>). page.
			</p>

			<p>
				<a id="standards"><b>What kind of standards do you use for
						data?</b> </a>
			</p>
			<p>
				Our data is primarily in METS, MODS and TEI formats. We are planning
				to provide our data for others to download and use. Please <a
					href="mailto:foundations@lib.cam.ac.uk">let us know</a> what
				formats/APIs would be most useful to you.
			</p>

			<p>
				<a id="pdf"><b>Will you be offering whole pdfs of the
						manuscripts to download (for e-readers for example)?</b> </a>
			</p>

			<p>This is something which we would like to do, however this may
				result in very large files, so may not be suitable for all books.</p>

			<p>
				<a id="digitisation"><b>Why can't I see all books in
						Cambridge University Library online?</b> </a>
			</p>

			<p>
				We are just starting to build our online collection, so we only have
				a few works online at the moment. You may be able to find what you
				are looking for at the <a
					href="http://www.lib.cam.ac.uk/eresources/" target="_blank">eresources@cambridge
					website</a>.
			</p>

			<p>
				<a id="searchexample"><b>What types of searches do you
						support?</b> </a>
			</p>

			<div class="searchexample">

				<div>
					<p>
						Searching for <span class="search">newton</span> Searches the
						metadata for 'newton'<br /> Searching for <span class="search">isaac
							newton</span> Searches for 'isaac' AND 'newton'<br /> Searching for <span
							class="search">"isaac newton"</span> Searches for the phrase
						'isaac newton'<br /> The characters <b>?</b> and <b>*</b> can be
						used as wildcards in your search.<br /> Use <b>?</b> to represent
						one unknown character and <b>*</b> to represent any number of
						unknown characters.
					</p>
					<p>
						Try <a href="/search">searching our collections</a>.
					</p>
				</div>

			</div>

			<br /> 
			<p>
				<b>Do you have any other questions? Please contact us using the
					<a class="iframe" href="/feedbackform.html">Feedback form</a> </b>
			</p>

		</div>
	</div>

</div>

</section>

<jsp:include page="footer/footer.jsp" />



