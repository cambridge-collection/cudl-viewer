<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="ulcambridge.foundations.viewer.model.*,ulcambridge.foundations.viewer.model.Collection,java.util.List,ulcambridge.foundations.viewer.ItemFactory"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	Collection collection = (Collection) request
			.getAttribute("collection");

	List<Collection> subCollections = (List<Collection>) request
			.getAttribute("subCollections");
	
	String contentHTMLURL = (String) request.getAttribute("contentHTMLURL");
%>
<jsp:include page="header/header-full.jsp">
	<jsp:param name="title" value="<%=collection.getTitle()%>" />
</jsp:include>
<jsp:include page="header/nav.jsp">
	<jsp:param name="activeMenuIndex" value="1" />
	<jsp:param name="displaySearch" value="true" />
	<jsp:param name="subtitle" value="<%=collection.getTitle()%>" />
</jsp:include>

<div class="campl-row campl-content campl-recessed-content">
	<div class="campl-wrap clearfix">

		<!-- side nav -->
		<div class="campl-column3">
			<div class="campl-tertiary-navigation">
				<div class="campl-tertiary-navigation-structure">
					<ul class="campl-unstyled-list campl-vertical-breadcrumb">
						<li><a href="/">Cambridge Digital Library<span
								class="campl-vertical-breadcrumb-indicator"></span></a></li>
					</ul>
					<ul
						class="campl-unstyled-list campl-vertical-breadcrumb-navigation">
						<li class="campl-selected"><a href="<%=collection.getURL()%>"><%=collection.getTitle()%></a>
							<ul
								class='campl-unstyled-list campl-vertical-breadcrumb-children'>

								<%
									for (int i = 0; i < subCollections.size(); i++) {
										Collection c = subCollections.get(i);
								%>
								<li><a href="<%=c.getURL()%>"><%=c.getTitle()%></a></li>
								<%
									}
								%>
							</ul></li>

					</ul>
				</div>
			</div>
		</div>
	
	  <div id="summaryDiv" contenteditable="true">
	     <% String summaryURL = contentHTMLURL+"/"+collection.getSummary();  %>
		 <c:import charEncoding="UTF-8" url="<%=summaryURL%>" /> 
      </div>
      
      <div id="sponsorDiv" class="campl-column12 campl-content-container" contenteditable="true">
          <% String sponsorsURL = contentHTMLURL+"/"+collection.getSponsors();  %>
		  <c:import charEncoding="UTF-8" url="<%=sponsorsURL%>" /> 
      </div>

	</div>
</div>

<% String filenames = collection.getSummary()+","+collection.getSponsors(); %>
<jsp:include page="editor.jsp" >
  <jsp:param name='dataElements' value='summaryDiv,sponsorDiv'/>
  <jsp:param name='filenames' value='<%=filenames%>'/>
</jsp:include>

<jsp:include page="header/footer-full.jsp" />












