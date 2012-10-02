<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
int activeIndex = Integer.valueOf(request.getParameter("activeSubmenu"));
%>
<nav id="navSecondary" class="grid_20">
<ul>
  <li><a href="/about/" title="Cambridge Digital Library" <% if (activeIndex==0) { %>class="active" <% } %>> Cambridge Digital Library</a></li>
  <li><a href="/news/" title="News" <% if (activeIndex==1) { %>class="active" <% } %>> News</a></li>
  <li><a href="/contributors/" title="Contributors" <% if (activeIndex==2) { %>class="active" <% } %>> Contributors</a></li>
  <li><a href="/terms/" title="Terms & Conditions" <% if (activeIndex==3) { %>class="active" <% } %>> Terms & Conditions</a></li>

</ul>

</nav>
<!-- end #navSecondary -->