<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
  <head>
    <title>Simple Viewer</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
      #viewer {
        width: 100%;
        height: 100%;
        position: fixed;
      }
    </style>
    <link rel="stylesheet" type="text/css" href="/mirador/css/mirador-combined.css">
    <script src="/mirador/mirador.min.js"></script>
  </head>
  <body>
  
    <div id="viewer"></div>
    <script type="text/javascript">
    $(function() {
        myMiradorInstance = Mirador({
          id: "viewer",
          layout: "1x1",
          buildPath: "mirador/",
          data: [
            { manifestUri: "<c:out value="${baseURL}"/>view/iiif/<c:out value="${id}"/>.json", location: "Cambirdge University Library"}
            
          ],
          windowObjects: [{
        	  loadedManifest: "<c:out value="${baseURL}"/>view/iiif/<c:out value="${id}"/>.json",
        	  canvasID: "<c:out value="${baseURL}"/>view/iiif/<c:out value="${id}"/>/canvas/<c:out value="${pagenum}"/>",
        	  viewType: "ImageView"
        	  }]
        });
      });    
    </script>
  </body>
</html>
