<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>               

<!DOCTYPE html>

<jsp:include page="header/header-document.jsp" />

<div id="doc"></div>
<jsp:include page="document-about.jsp" />

<div id="transcription_normal">
 <iframe id="transcription_normal_frame" width="100%" height="100%" frameborder='0' src="">
 </iframe>
</div>

<div id="transcription_diplomatic">
 <iframe id="transcription_diplomatic_frame" width="100%" height="100%" frameborder='0' src="">
 </iframe>
</div>

<div id="logical_structure">
</div>

<jsp:include page="footer/footer-document.jsp" />