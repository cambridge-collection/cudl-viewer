<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>               

<!DOCTYPE html>

<jsp:include page="header/header-docview.jsp" />

<div id="center"></div>
<div id="metadata">
<b><div id="metadata-title"></div></b> by <div id="metadata-author"></div>
<br/>
Part of the <a href="/view/">Newton Collection</a>
<br/><br/>
You are viewing page: <div id="metadata-page"></div>
<br/>

Physical location: <el id="metadata-physicalLocation"></el><br/>
Shelf locator: <el id="metadata-shelfLocation"></el><br/>
Subject: <el id="metadata-subject"></el><br/>
Date created: <el id="metadata-dateCreatedDisplay"></el><br/>

<br/><br/>
<div id="metadata-rights"></div><br/>
</div>


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


</body>

</html>