<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>               

<jsp:include page="header/header-document.jsp" />


<div id="center"><div id="doc" style="height:100%;width:100%"></div></div>
<jsp:include page="document-about.jsp" />


<div id="transcription_normal">
 <iframe id="transcription_normal_frame" onload="unmaskRightPanel(1)" src="/transcription?url=">
 </iframe>
</div>

<div id="transcription_diplomatic">
 <iframe id="transcription_diplomatic_frame" onload="unmaskRightPanel(2)" src="/transcription?url=">
 </iframe>
</div>

<div id="logical_structure"></div>

<jsp:include page="footer/footer-document.jsp" />