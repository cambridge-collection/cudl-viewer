<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
int activeIndex = Integer.valueOf(request.getParameter("activeMenuIndex"));
boolean displaySearch = request.getParameter("displaySearch").toString().equals("true");
%>

<nav id="navPrimary" class="grid_20">
<ul>
	<li class="first"><a href="/" title="Home" <% if (activeIndex==0) { %>class="active" <% } %>> Home </a></li>
	<li><a href="/collections/" title="Browse" <% if (activeIndex==1) { %>class="active" <% } %>> Browse </a></li>
	<li><a href="/news/" title="News" <% if (activeIndex==2) { %>class="active" <% } %>> News </a></li>
	<li><a href="/about/" title="About" <% if (activeIndex==3) { %>class="active" <% } %>> About </a></li>
	<li><a href="/help/" title="Help" <% if (activeIndex==4) { %>class="active" <% } %>> Help </a></li>
</ul>

<%
if (displaySearch) { 
%>
	<form class="grid_5 prefix_1 omega" action="/search">
		<input id="search" type="text" value="" name="keyword" placeholder="Search"
			autocomplete="off" /> <input id="submit" type="submit"
			value="Search" />
	</form>	
<% 
} 
%>
</nav>
<!-- end #navPrimary -->