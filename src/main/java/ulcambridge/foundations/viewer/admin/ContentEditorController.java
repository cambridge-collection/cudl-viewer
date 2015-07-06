package ulcambridge.foundations.viewer.admin;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;

import ulcambridge.foundations.viewer.model.Properties;

/**
 * Controller for editing content through CKEditor on path /editor
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
	public synchronized ModelAndView handleUpdateRequest(
			HttpServletResponse response,
			@Valid @ModelAttribute() UpdateParameters writeParams,
			BindingResult errors) throws IOException, JSONException {

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

	// TODO security
	@RequestMapping(value = "/upload/")
	public ModelAndView handleUploadRequest(HttpServletRequest request,
			HttpServletResponse response,
			@Valid @ModelAttribute() UploadParameters uploadParams,
			BindingResult bindResult) throws IOException {	
		
		// further validation
		if( uploadParams.getUpload().getSize() == 0 ){
	        bindResult.rejectValue("upload","Upload file required.");
	    }
		
		// check file extension / content type for image. 
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[-_A-Za-z0-9]+\\.(jpg|png|gif|bmp)$");
		boolean isImageFile = pattern.matcher(uploadParams.getUpload().getOriginalFilename()).matches();
		boolean isImageContent = uploadParams.getUpload().getContentType().startsWith("image");
		
		if (!isImageFile && isImageContent) {
			bindResult.rejectValue("upload","Upload filename should be jpg, png, gif or bmp.");
		}
		
				
		if (bindResult.hasErrors()) {
			List<ObjectError> errors = bindResult.getAllErrors();
			for (int i=0; i<errors.size(); i++) {
				System.err.println(errors.get(i).toString());
			}
			throw new IOException("invalid request params");
		}		
		
		
		String filename = "images"+File.separator+uploadParams.getUpload().getOriginalFilename();
		InputStream is = uploadParams.getUpload().getInputStream();		
		
		// Save the file to disk.
		String contentPath = Properties.getString("cudl-viewer-content.path");
		boolean saveSuccessful = FileSave.save(contentPath, filename, is);
		
		response.setContentType("text/html");
		PrintStream out = null;

		try {
			out = new PrintStream(new BufferedOutputStream(
					response.getOutputStream()), true, "UTF-8");
			String contentURL = Properties.getString("cudl-viewer-content.url");
			out.print("<script>"
					+ "window.parent.CKEDITOR.tools.callFunction( "
					+ uploadParams.getCKEditorFuncNum()
					+ ", '"+contentURL+"/images/"+uploadParams.getUpload().getOriginalFilename()+"', 'save successful: "+saveSuccessful+"' );</script>");

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

	@RequestMapping(value = "/browse/")
	public ModelAndView handleBrowseRequest(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("CKEditor") String ckEditor,
			@RequestParam("CKEditorFuncNum") String ckEditorFuncNum,
			@RequestParam("langCode") String langCode) throws IOException {

		System.out.println("CKEDitor: " + ckEditor); // id of the div
		System.out.println("ckEditorFuncNum: " + ckEditorFuncNum); // should be
																	// used in
																	// return
																	// call
		System.out.println("In Browse");

		response.setContentType("text/html");
		PrintStream out = null;

		try {
			out = new PrintStream(new BufferedOutputStream(
					response.getOutputStream()), true, "UTF-8");
			out.print("<html><body><script>var call = function () {"
					+ "window.opener.CKEDITOR.tools.callFunction( "
					+ ckEditorFuncNum
					+ ", '/images/collectionsView/newton.jpg' );}</script>"
					+ "<a href='' onclick='call()'>SELECTED FILE</a></body></html>");

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
	 * Note: Does not validate params. This should occur before calling this
	 * method.
	 * 
	 * @return
	 * @throws IOException
	 */
	private synchronized boolean saveHTML(UpdateParameters writeParams) throws IOException {

		// Read in data from request
		String html = writeParams.getHtml();
		String filename = writeParams.getFilename();
		
		String contentPath = Properties.getString("cudl-viewer-content.path");
		
		// Save the file to disk. 
		return FileSave.save(contentPath, filename, new ByteArrayInputStream(html.getBytes("UTF-8")));
		
	}
	
	


	// Performs validation on parameters used for writing html.
	public static class UploadParameters {

		@NotNull
		private String CKEditor;
		
		@NotNull
		private String CKEditorFuncNum;
		
		@NotNull
		private String langCode;
		
		private MultipartFile file;	

		public String getCKEditor() {
			return CKEditor;
		}

		public void setCKEditor(String CKEditor) {
			this.CKEditor = CKEditor;
		}
		
		public String getCKEditorFuncNum() {
			return CKEditorFuncNum;
		}

		public void setCKEditorFuncNum(String CKEditorFuncNum) {
			this.CKEditorFuncNum = CKEditorFuncNum;
		}	
		
		public String getLangCode() {
			return langCode;
		}

		public void setLangCode(String langCode) {
			this.langCode = langCode;
		}
		
		public MultipartFile getUpload() {
			return file;
		}

		public void setUpload(MultipartFile file) {
			this.file = file;
		}		
	}
	
	// Performs validation on parameters used for writing html.
	public static class UpdateParameters {

		@NotNull
		@Pattern(regexp = "^[-_/A-Za-z0-9]+\\.html$", message = "Invalid filename")
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

			InputStream inputStream = new ByteArrayInputStream(
					input.getBytes(StandardCharsets.UTF_8));
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
