<%@page autoFlush="false" %>

<%@taglib prefix="cudl" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>How do I add a bookmark?</h2>
<div class="campl-content-container">
    <p>Go to the item's view page.</p>
    <a class="fancybox-inline" href="<c:url value="/img/general/bookmark-link1.png?v=2"/>">
        <img alt="screenshot" src="<c:url value="/img/general/bookmark-link1-thumb.png?v=2"/>">
    </a>
</div>
<div class="campl-content-container">
    <p>Scroll down if necessary, and click the 'Bookmark Image' button.</p>
    <a class="fancybox-inline" href="<c:url value="/img/general/bookmark-link2.png?v=2"/>">
        <img src="<c:url value="/img/general/bookmark-link2-thumb.png?v=2"/>">
    </a>
</div>
<div class="campl-content-container">
    <p>Then click 'Yes'.</p>
    <a class="fancybox-inline" href="<c:url value="/img/general/bookmark-link3.png?v=2"/>">
        <img src="<c:url value="/img/general/bookmark-link3-thumb.png?v=2"/>">
    </a>
</div>
<div class="campl-content-container">
    <h2>How do I delete a bookmark?</h2>

    <p>Just click on the remove link next to each item to remove that
        bookmark from your collection.</p>
</div>
<div class="campl-content-container">
    <cudl:logout/>
</div>
