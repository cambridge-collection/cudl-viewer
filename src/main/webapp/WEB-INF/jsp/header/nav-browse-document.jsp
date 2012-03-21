<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<nav id="navPrimary" class="grid_20" style="width:100%; left:-8px;">
<ul>
	<li class="first"><a href="/" title="Home">
			Home </a>
	</li>
	<li><a href="/collections/" title="Browse" class="active"> Browse </a>
	</li>
	<li><a href="/news/" title="News"> News </a>
	</li>	
	<li><a href="/about/" title="About"> About </a>
	</li>
	<li><a href="/help/" title="Help"> Help </a>
</ul>

	<form class="grid_5 prefix_1 omega" action="/search">
		<input id="search" type="text" value="" name="keyword" placeholder="Search"
			autocomplete="off" /> <input id="submit" type="submit"
			value="Search" />
	</form>
<!-- 
<nav id="navSecondary" class="grid_20" style="width:100%; left:-8px;">
<ul>
	<li><a href="/collections/newton" title="Newton Papers" class="active"> Newton Papers </a>
	</li>
</ul>
</nav> -->
</nav>
<!-- end #navPrimary -->