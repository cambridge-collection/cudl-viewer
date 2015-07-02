package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

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
	// on path /editor/update/
	@RequestMapping(value = "/update/")
	public synchronized ModelAndView handleUpdateRequest(HttpServletResponse response,
			@Valid @ModelAttribute() WriteParameters writeParams,
	        BindingResult errors ) throws IOException, JSONException {
		
		if (errors.hasErrors()) {
	        throw new IOException("invalid request params");
	    }
					
		boolean success = saveHTML(writeParams);
		JSONObject json = new JSONObject();
		json.put("writesuccess", success);		
		
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
	
	
	/**
	 * Note: Does not validate params.  This should occur before calling this 
	 * method. 
	 * 
	 * @return
	 * @throws IOException 
	 */
	private boolean saveHTML(WriteParameters writeParams) throws IOException {
		
  		// Read in data from request
		String html = writeParams.getHtml();        
		String filename = writeParams.getFilename();
		String contentPath = Properties.getString("cudl-viewer-content.path");						
		
		// Setup variables
		boolean task1done = false, task2done = false, task3done = false, task4done = false, task5done = false;					
		File newFile = new File(contentPath, filename + ".new_cudl_viewer_version").getCanonicalFile(); 
		InputStream is = new ByteArrayInputStream( html.getBytes( "UTF-8" ) );
		File parentDir = new File(newFile.getParent()).getCanonicalFile();
		File existingParentDir = existingParent(parentDir);
		File oldFile = new File(contentPath, filename + ".old_cudl_viewer_version").getCanonicalFile();
		File file = new File(contentPath, filename).getCanonicalFile();
		
		try {
			
	      // Task 1 - Create directory (if needed).  		  
		  parentDir.mkdirs();
		  task1done = true;		 		  
		  	
		  // Task 2 - Write file to file system.
		  Files.copy(is, newFile.toPath());	
		  task2done = true;		  		
		  
		  // Task 3 - Copy existing file (if available) for rollback.
		  if (file.exists()) {
			  Files.copy(file.toPath(), oldFile.toPath());
		  }
		  task3done = true;		 
		  
		  // Task 4 - rename file (overwriting if exists already).  
		  newFile.renameTo(file);
		  task4done = true;		  		  
		  
		  // Task 5 - Commit to git
		  // TODO 
		  task5done = true;		  		  	
		  
		  // Clean up.  
		  if (oldFile.exists()) { oldFile.delete(); }
		  
		  // Write response out in JSON. 		  
		  return true;
			
		} catch (Exception e) {
						
			e.printStackTrace();
			return false; 
			
        } finally {
        	
            if (! task5done) {
                if (task4done) {
                    // Rollback task 4
                	Files.copy(file.toPath(), newFile.toPath());
                	if (oldFile.exists()) { oldFile.renameTo(file); } 
                	else { file.delete(); } 
                    task4done = false;
                }
            }          	
            if (! task4done) {
                if (task3done) {
                    // Rollback task 3
                	if (oldFile.exists()) { oldFile.delete(); }
                    task3done = false;
                }
            }        	
            if (! task3done) {
                if (task2done) {
                    // Rollback task 2
                	if (newFile.exists()) { newFile.delete(); } // NB: Possible to lose any .new_cudl_viewer_version File that was overwritten
                    task2done = false;
                }
            }
            if (! task2done) {
                if (task1done) {
                	// Rollback task 1 - Creating the parent directory(ies). 
                	deleteAllDirectoriesUpTo(parentDir, existingParentDir);
                    task1done= false;
                }
            }
        }			
	}
	
	/**
	 * Gets lowest level directory that already exists on file system. 
	 *  
	 * @param dir
	 * @return
	 */
	private File existingParent(File dir) {
	    if(dir.exists()) {
	    	return dir;
	    }
	        
	    while(!dir.exists()) {
	        dir = dir.getParentFile();
	    }
	    
	    return dir;
	}
	
	/**
	 * Deletes all directories in the hierarchy up to parentDir
	 * childDir should be a sub-directory of parentDir.
	 * Only deletes empty directories.  
	 * 
	 * @param parentDir
	 * @param childDir
	 * @return
	 * @throws IOException 
	 */
	private boolean deleteAllDirectoriesUpTo(File childDir, File parentDir) throws IOException {
		
		childDir = childDir.getCanonicalFile();
		parentDir = parentDir.getCanonicalFile();
		
		if (!childDir.exists() || !parentDir.exists() || !fileIsParent(childDir, parentDir)) {
			return false;
		}
			    
	    while (!childDir.equals(parentDir)) {
	    	
	    	// delete dir if empty. 
	    	if (childDir.exists() && childDir.isDirectory() && childDir.list().length==0) {
				childDir.delete();
				childDir = childDir.getParentFile();
	    	} else {
	    		return false;	    		
	    	}
	    }
	    
	    return true;
	
	}
	
	private boolean fileIsParent(File child, File parent) throws IOException
	{
	    if (!parent.exists() || !parent.isDirectory()) {
	        return false;
	    }
	 
	    while (child != null) {
	        if (child.equals(parent)) {
	            return true;
	        }
	        child = child.getParentFile();
	    }
	    
	    // No match found
	    return false;
	}


	// Performs validation on parameters used for writing html. 
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
			this.html = tidyHTML(html);
		}		
		private String tidyHTML(String input) {
			
			InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
			OutputStream outputStream = new ByteArrayOutputStream();		

			Tidy tidy = new Tidy(); // obtain a new Tidy instance
			tidy.setXHTML(false);
			tidy.setCharEncoding(Configuration.UTF8);
			tidy.setMakeClean(false);
			tidy.setTidyMark(false);
			tidy.setDropEmptyParas(false);
			tidy.setDocType("omit");
			tidy.setQuiet(true);
			tidy.setShowWarnings(false);
			tidy.parse(inputStream, outputStream);
			return outputStream.toString();
			
		}
		  
	}
	
}
