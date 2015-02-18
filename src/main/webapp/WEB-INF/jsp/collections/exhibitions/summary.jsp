<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*"%>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");
%>
<div class="grid_10">
	<h1><%=collection.getTitle()%></h1>

	<!--  NOTE This is a virtual collection and has a different layout. -->

	<div class="campl-content-container">

		<div class="campl-column6 virtual_collection_textgroup">

			<blockquote class="cam-quote-mark">

				Some books are to be tasted, others to be swallowed, and some few
				to be chewed and digested" <br /> <br /> <cite>
					Francis Bacon, 'Of Studies' from <i>Essays</i> 1625
				</cite>

			</blockquote>
			<div class="campl-content-container">
<p>The Library holds regular exhibitions, usually organised
					around a particular theme or collection. Depending on the chosen
					theme, the exhibition cases might contain anything from
					irreplaceable works of world importance to newspapers or printed
					ephemera. They demonstrate the breadth of the Library's holdings
					and often yield surprises.</p>

				<p>Exhibitions are full of fascinating stories - items are
					carefully selected and captioned and are set within larger contexts
					and narratives. However curators are frequently required to make
					very tough choices. They can only open the books or manuscripts at
					one place. There are constraints in the number of items that can be
					shown. Some material might not be possible to display at all
					because of its fragile condition.</p>

				<p>Alongside the Digital Library, Cambridge University Library
					is developing a series of virtual exhibitions to extend the reach
					of its exhibition programme and offer a fuller experience of some
					of the items displayed. Some virtual exhibitions will correspond
					closely to physical exhibitions or events; others will be entirely
					online.</p>

				<p>Where we have full digital facsimiles of exhibited items,
					these will be included within the Digital Library and linked with
					the virtual exhibitions websites. This collection brings together
					exhibited items in one place, enabling you to see them alongside
					other resources within the Digital Library, or to link though to an
					associated exhibition and explore the item within that context.</p>

			</div>
		</div>
		<div class="campl-column6">
			<div class="virtual_collection_summary_image"
				style="background: url('/images/collectionsView/exhibition1.jpg');">
				<a href="/view/PR-01886-00012-00019/1"><img src="/img/blank.gif"
					style="width: 100%; height: 100%;"
					alt="The broad and narrow way"
					title="The broad and narrow way" /> </a>
			</div>
			<div class="virtual_collection_summary_image"
				style="background: url('/images/collectionsView/exhibition2.jpg');">
				<a href="/view/PR-01890-00011-00067/27"><img
					src="/img/blank.gif" style="width: 100%; height: 100%;"
					alt="The book of bosh" title="The book of bosh" /> </a>
			</div>
			<div class="virtual_collection_summary_image"
				style="background: url('/images/collectionsView/exhibition3.jpg');">
				<a href="/view/PR-A-01871-01039/3"><img src="/img/blank.gif"
					style="width: 100%; height: 100%;"
					alt="Dick Turpin : quadrille"
					title="Dick Turpin : quadrille" /> </a>
			</div>
			<div class="virtual_collection_summary_image"
				style="background: url('/images/collectionsView/exhibition4.jpg');">
				<a href="/view/PR-CCD-00007-00025-00008/4"><img
					src="/img/blank.gif" style="width: 100%; height: 100%;"
					alt="The youth's moral pilot"
					title="The youth's moral pilot" /> </a>
			</div>
		</div>
	</div>
</div>
<div class="campl-column12">&nbsp;</div>
