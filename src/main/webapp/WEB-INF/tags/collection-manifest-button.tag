<%@tag description="Add collection iiif manifest download button" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="button usebutton">
    <c:set var = "uriArray" value = "${fn:split(collection.URL,'/,')}"/>
    <a class="btn btn-info left" href="/iiif/collection/${uriArray[fn:length(uriArray)-1]}" title="Download Collection IIIF Manifest" download><img src="/img/logo-iiif-34x30.png" title="International Image Interoperability Framework"><span>Collection IIIF Manifest</span></a>
</div>
