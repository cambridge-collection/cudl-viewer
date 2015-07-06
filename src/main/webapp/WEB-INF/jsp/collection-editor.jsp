<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.List, java.util.ArrayList, java.util.Arrays"%>
	
	<!-- This file adds the CKEditor element to specified divs.  To use include with JSP:include pages="" and
specify the attributes: dataElements (comma separated list of HTML div ids) and filenames (comma separated).
Must have the same number of elements in each.  -->

<script src="/scripts/ckeditor/ckeditor.js"></script>
<link rel="stylesheet" href="/styles/cudl-editor.css">

<div id="cudl-editor-confirmation" class="alert alert-info cudl-editor-confirmation" style="display: none">
		<a href="#" class="close" onclick="$('.alert').hide();">&times;</a> 
		Saving the page as:<br /> <br />
        <input id="filename" type="text" name="filename" value=""></input><br /> <br />

		<button type="button" class="btn btn-default btn-success"
			onclick="saveData()">Yes</button>
		<button type="button" class="btn btn-default"
			onclick="$('#confirmation').hide();">Cancel</button>
</div>
	

<script type="text/javascript">

   CKEDITOR.config.allowedContent = true;  // prevent tag filtering   
   CKEDITOR.disableAutoInline = true;
   CKEDITOR.config.filebrowserBrowseUrl = '/editor/browse/';
   CKEDITOR.config.filebrowserImageBrowseUrl = '/editor/browse/';   
   CKEDITOR.config.filebrowserUploadUrl = '/editor/upload/';
   CKEDITOR.config.filebrowserImageUploadUrl = '/editor/upload/';   
   
   var filename='';
   var data='';

    // Create a function for each element specified.
   <% 
     String dataElements = request.getParameter("dataElements");
     String filenames = request.getParameter("filenames");
     List<String> dataElementList = Arrays.asList(dataElements.split(","));
     List<String> filenameList = Arrays.asList(filenames.split(","));
     
     for (int i=0; i<dataElementList.size() && i<filenameList.size(); i++) {
    	 String dataElement = dataElementList.get(i);
    	 String filename = filenameList.get(i);
     
     %>       
    		
   CKEDITOR.inline( '<%=dataElement%>', {
       on: {
           save: function( event ) {
               data = event.editor.getData();
               filename = '<%=filename%>';
               openConfirmation();
           }
       	}
   } );
    
   <% } %>
   
   var saveData = function () {	  
	   
       // Submit data to /editor/update/               
       $.ajax({
    	   type: "POST",
    	   url: "/editor/update/",
    	   data: { html: data, filename: $('#filename').val() }
    	 })
    	   .done(function( msg ) {
    	     if (msg.writesuccess) {
    	    	 alert("Changes Saved.");
    	     } else {
    	    	 alert("There was a problem saving your changes.");
    	     }
    	 });
       
       $('#cudl-editor-confirmation').hide();
   }
   
   var openConfirmation = function () {	   
	   	   
	   $('#filename').val(filename);  
       $('#cudl-editor-confirmation').show();
   }
</script>