<%@tag description="Add collection iiif manifest download button" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@attribute name="collectionId" required="true" type="java.lang.String" %>

<div class="button usebutton">
    <a class="btn btn-info left" href="/iiif/collection/${collectionId}" title="Download Collection IIIF Manifest" download><img src="/img/logo-iiif-34x30.png" title="International Image Interoperability Framework"><span>Collection IIIF Manifest</span></a>
</div>
