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
    <link rel="stylesheet" type="text/css" href="/mirador-ui/css/mirador-combined.css">
    <script src="/mirador-ui/mirador.min.js"></script>
  </head>
  <body>
  
    <div id="viewer"></div>
    <script type="text/javascript">
    $(function() {    	
        myMiradorInstance = Mirador({
          "id": "viewer",
          "buildPath": "/mirador-ui/",          
          "i18nPath": "locales/",
          data: [
            { manifestUri: "<c:out value="${baseURL}"/>iiif/<c:out value="${id}"/>.json", location: "Cambirdge University Library"}            
          ],
          'windowSettings' : {
          "canvasControls": { 
        	  "annotations" : {
                  "annotationLayer" : false, //whether or not to make annotation layer available in this window
                  "annotationCreation" : false, /*whether or not to make annotation creation available in this window,
                               only valid if annotationLayer is set to True and an annotationEndpoint is defined.
                               This setting does NOT affect whether or not a user can edit an individual annotation that has already been created.*/
                  "annotationState" : 'off', //[_'off'_, 'on'] whether or not to turn on the annotation layer on window load
                  "annotationRefresh" : false, //whether or not to display the refresh icon for annotations
            },
          }},
          windowObjects: [{
        	  loadedManifest: "<c:out value="${baseURL}"/>iiif/<c:out value="${id}"/>.json",
        	  canvasID: "<c:out value="${baseURL}"/>iiif/<c:out value="${id}"/>/canvas/<c:out value="${pagenum}"/>",
        	  viewType: "ImageView"
        	  }]
        });
        console.log(myMiradorInstance);
      });
     
    </script>
  </body>
</html>
