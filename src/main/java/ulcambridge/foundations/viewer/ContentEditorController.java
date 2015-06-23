package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for editing content through CKEditor
 * on path /editor
 * 
 * @author jennie
 * 
 */
@Controller
public class ContentEditorController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	// TODO method level security
	// TODO make transaction using Task Manager
	// on path /editor/update/
	@RequestMapping(value = "/update/")
	public ModelAndView handleUpdateRequest(HttpServletResponse response,
			@Valid @ModelAttribute() WriteParameters writeParams,
	        BindingResult errors ) throws IOException, JSONException {
		
		if (errors.hasErrors()) {
	        throw new IOException("invalid request params");
	    }
		
		// read in data from request
		String html = writeParams.getHtml();        
		String filename = writeParams.getFilename();
		String contentPath = Properties.getString("cudl-viewer-content.path");						
		
		// write to file system and rename
		File file = new File(contentPath, filename + ".new"); // append .new which will be removed after copy completes.
		InputStream is = new ByteArrayInputStream( html.getBytes( "UTF-8" ) );
		Files.copy(is, file.toPath());	
		file.renameTo(new File(contentPath, filename));
			
		// commit to git
		//TODO 
		
		// Write response out in JSON. 
		JSONObject json = new JSONObject();
		json.put("writesuccess", true);
		
		response.setContentType("application/json");
		PrintStream out = null;

		try {
			out = new PrintStream(new BufferedOutputStream(
					response.getOutputStream()), true, "UTF-8");
			out.print(json.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		return null;
	
	}

	// Performs validation on parameters used for writing html. 
	// Do we want html validation? formatting?
	public static class WriteParameters {

		@NotNull
		@Pattern(regexp="^[-_/A-Za-z0-9]+\\.html$",
	             message="Invalid filename")
		private String filename;
		
		@NotNull
		private String html;
		  
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		public String getHtml() {
			return html;
		}
		public void setHtml(String html) {
			this.html = html;
		}
		  
	}
	
}
