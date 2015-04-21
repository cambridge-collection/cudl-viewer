<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header/header-full.jsp" />
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="4" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="title" value="Help: FAQ" />
</jsp:include>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">
		<div class="campl-column9  campl-main-content" id="content">
			<div class="campl-content-container">

				<div class="panel light faq">

					<div class="grid_11">

						<ol>
							<li><a href="#mailinglist"><b>How can I find out
										when new content is made available?</b> </a></li>
							<li><a href="#links"><b>How do I link to a specific
										page within a book?</b> </a></li>
							<li><a href="#view"><b>How do I change page while
										seeing as much of the manuscript as possible?</b> </a></li>
							<li><a href="#socialmedia"><b>How do I share a book
										on Facebook, Twitter, etc.?</b> </a></li>
							<li><a href="#tech"><b>What technologies are behind
										the Cambridge Digital Library?</b> </a></li>
							<li><a href="#standards"><b>What kind of standards
										do you use for data?</b> </a></li>
							<li><a href="#pdf"><b>Will you be offering whole
										pdfs of the manuscripts to download (for e-readers for
										example)?</b> </a></li>
							<li><a href="#digitisation"><b>Why can't I see all
										books in Cambridge University Library online?</b> </a></li>
							<li><a href="#searchexample"><b>What types of
										searches do you support?</b> </a></li>
							<li><a href="#cookies"><b>How are cookies are used
										on this site?</b> </a></li>
						</ol>
					</div>
					<div class="grid_11">

						<p>
							<a id="mailinglist"><b>How can I find out when new
									content is made available?</b> </a>
						</p>

						<p>
							Subscribe to our mailing list by sending an email to <a
								href="mailto:cudl-updates-subscribe@caret.cam.ac.uk?subject=Subscribe&amp;body=I%20would%20like%20to%20receive%20update%20news%20from%20Cambridge%20University%20Digital%20Library.">cudl-updates-subscribe@caret.cam.ac.uk</a>
							and we will do our best to keep you informed of updates.
						</p>

						<p>
							<a id="links"><b>How do I link to a specific page within
									a book?</b> </a>
						</p>

						<p>Our website URLs have the format:</p>
						<p>http://cudl.lib.cam.ac.uk/view/[item id]/[image number]</p>
						<p>
							So <a href="/view/MS-ADD-03996/5">http://cudl.lib.cam.ac.uk/view/MS-ADD-03996/5</a>
							links to image 5 in item MS-ADD-03996 (The Trinity College
							Notebook).
						</p>


						<p>
							<a id="view"><b>How do I change page while seeing as much
									of the manuscript as possible?</b> </a>
						</p>

						<p>The right panel, which shows some information about the
							item can be collapsed by clicking on the arrow icon in the far
							right of the screen. This will allow you to view more of the
							image while you can still page forward and backwards through the
							book. There is also a button within the viewer that allows you to
							expand it to cover the full screen.</p>

						<p>
							<a id="socialmedia"><b>How do I share a book on Facebook,
									Twitter, etc.?</b> </a>
						</p>

						<p>When you are looking at an item, on the right hand panel
							select 'View more options' then 'Download or Share'. Under the
							Share section on this tab are there are icons which will let you
							share the page on facebook, twitter and a number of other social
							media sites.</p>
						<p>
							<a id="tech"><b>What technologies are behind the
									Cambridge Digital Library?</b> </a>
						</p>

						<p>
							We aim to use primarily open source technologies, and to avoid
							the use of plugins to make the site accessible to as many people
							as possible. For the website we use a collection of JavaScript
							libraries including <a href="https://openseadragon.github.io/"
								target="_blank">OpenSeadragon</a> for the zoomable images, <a
								href="http://getbootstrap.com/" target="_blank">Bootstrap</a>
							and <a href="http://jquery.com/" target="_blank">JQuery</a>.
							Behind the scenes we use <a href="http://xtf.cdlib.org/"
								target="_blank"> the eXtensible Text Framework (XTF)</a> and <a
								href="http://www.goobi.org/" target="_blank">Goobi</a> for
							digitising and indexing the data. The website was created by the
							digital library team at Cambridge University Library and is
							written in Java. You can find out more by contacting the Digital
							Library Team (see our <a href="/contributors/">Contributors
								Page</a> for details on how to do this).
						</p>

						<p>
							<a id="standards"><b>What kind of standards do you use
									for data?</b> </a>
						</p>
						<p>
							Our data is primarily in METS, MODS and TEI formats. We are
							planning to provide our data for others to download and use.
							Please <a href="mailto:foundations@lib.cam.ac.uk">let us know</a>
							what formats/APIs would be most useful to you.
						</p>

						<p>
							<a id="pdf"><b>Will you be offering whole pdfs of the
									manuscripts to download (for e-readers for example)?</b> </a>
						</p>

						<p>This is something which we would like to do, however this
							may result in very large files, so may not be suitable for all
							books.</p>

						<p>
							<a id="digitisation"><b>Why can't I see all books in
									Cambridge University Library online?</b> </a>
						</p>

						<p>
							We are just starting to build our online collection, so we only
							have a few works online at the moment. You may be able to find
							what you are looking for at the <a
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
										newton</span> Searches for 'isaac' AND 'newton'<br /> Searching for
									<span class="search">"isaac newton"</span> Searches for the
									phrase 'isaac newton'<br /> The characters <b>?</b> and <b>*</b>
									can be used as wildcards in your search.<br /> Use <b>?</b> to
									represent one unknown character and <b>*</b> to represent any
									number of unknown characters.
								</p>
								<p>
									Try <a href="/search">searching our collections</a>.
								</p>
							</div>

						</div>
						<p>
							<a id="cookies"><b>How are cookies are used on this site?</b>
							</a>
						</p>

						<p>Cookies are used to provide the essential functionality
							provided by the Cambridge Digital Library website, to store your
							preferences and to monitor usage statistics, so we can improve
							performance.</p>

						<p>1. Essential Cookies</p>
						<p>These cookies are essential for the running of our website.
							Without the use of these cookies parts of our website would not
							function.</p>
						<p>2. Analytical Performance Cookies</p>
						<p>We use these types of cookies to monitor our websites
							performance and how different people use our website. These
							cookies provide us with information that helps us provide a
							better user experience and also to identify any areas that may
							need maintenance.</p>
						<p>3. Functional Cookies</p>
						<p>Functional cookies are used to remember your preferences on
							our website, such as which page you were on in a multiple page
							collection, or which messages you may have seen or not seen.</p>
						<p>4. Embedded content</p>
						<p>
							In our presentation of documents, we sometimes embed photos and
							video content from other websites such as YouTube. As a result,
							when you visit a page containing such content, you may be
							presented with cookies from these websites. Cambridge Digital
							Library does not control the dissemination of these cookies and
							you should check the relevant third party's website for more
							information. <br />
						</p>
						<p>
							If you want to block or restrict cookies which are set on your
							device then you can do this by changing your browser settings.
							The Help function within your browser should tell you how.
							Alternatively, you may wish to visit <a
								href="http://www.aboutcookies.org">www.aboutcookies.org</a>,
							which contains comprehensive information on how to do this on a
							wide variety of desktop browsers.
						</p>
						<p>
							<b>Do you have any other questions? Please contact us using
								the <a class="iframe" href="/feedbackform.html">Feedback
									form</a>
							</b>
						</p>

					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<jsp:include page="header/footer-full.jsp" />



