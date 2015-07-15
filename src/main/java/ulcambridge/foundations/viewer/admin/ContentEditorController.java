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
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

	protected final String contentHTMLPath = Properties
			.getString("cudl-viewer-content.html.path");
	protected final String contentHTMLURL = Properties
			.getString("cudl-viewer-content.html.url");
	protected final String contentImagesPath = Properties
			.getString("cudl-viewer-content.images.path");
	protected final String contentImagesURL = Properties
			.getString("cudl-viewer-content.images.url");
	
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Request on URL /editor/update/html
	 * 
	 * Used by CKEditor to update the HTML on specified sections of the website.
	 * Also commits to git.
	 * 
	 * @param response
	 * @param writeParams
	 * @param errors
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/update/html")
	public synchronized ModelAndView handleUpdateRequest(
			HttpServletResponse response,
			@Valid @ModelAttribute() UpdateHTMLParameters writeParams,
			BindingResult errors) throws IOException, JSONException {

		if (errors.hasErrors()) {
			throw new IOException("HTML Update failed due to invalid parameters. Please check "+
		    "your filename contains only valid characters.");
		}

		boolean success = saveHTML(writeParams);
		JSONObject json = new JSONObject();
		json.put("writesuccess", success);

		response.setContentType("application/json");
		write(json.toString(), response.getOutputStream());
		return null;

	}

	/**
	 * Request on URL /editor/add/image
	 * 
	 * Used by CKEditor to upload an image file to the server.
	 * 
	 * @param request
	 * @param response
	 * @param addParams
	 * @param bindResult
	 * @return
	 * @throws IOException
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/add/image")
	public ModelAndView handleAddImageRequest(HttpServletRequest request,
			HttpServletResponse response,
			@Valid @ModelAttribute() AddImagesParameters addParams,
			BindingResult bindResult) throws IOException {

		// Check file extension and content type are image.
		uploadFileValidation(addParams, bindResult);

		if (bindResult.hasErrors()) {
			throw new IOException("Your image upload failed. Please ensure you have selected a file "+
		    "and that your directory path contains only valid characters.");
		}
		
		String filename = addParams.getUpload().getOriginalFilename();
		InputStream is = addParams.getUpload().getInputStream();

		// Save the file to disk.
		boolean saveSuccessful = FileSave.save(contentImagesPath+File.separator+addParams.getDirectory(), filename, is);

		response.setContentType("text/html");
		write("<html><head><script> window.opener.CKEDITOR.tools.callFunction( "
				+ addParams.getCKEditorFuncNum() + ", '" + contentImagesURL
				+"/"+ addParams.getUpload().getOriginalFilename()
				+ "', 'save successful: " + saveSuccessful + "' );window.close();</script>"
				, response.getOutputStream());

		return null;

	}

	/**
	 * Request on /editor/browse/images
	 * 
	 * Used by CKEditor to browse the images available on the server.
	 * 
	 * @param request
	 * @param response
	 * @param ckEditor
	 * @param ckEditorFuncNum
	 * @param langCode
	 * @return
	 * @throws IOException
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/browse/images")
	public ModelAndView handleBrowseImagesRequest(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("CKEditor") String ckEditor,
			@RequestParam("CKEditorFuncNum") String ckEditorFuncNum,
			@RequestParam("langCode") String langCode,
			@RequestParam(value = "browseDir", required = false) String browseDir)
			throws IOException {

		// Get a list of images on the server.
		File imagesDir = new File(contentImagesPath);
		if (browseDir == null) {
			browseDir = imagesDir.getPath();
		}

		// File[] files = imagesDir.listFiles();
		BrowseFile imageFiles = buildFileHeirachy(imagesDir);

		imageFiles = buildBrowseDirectory(browseDir, imageFiles);

		ModelAndView modelAndView = new ModelAndView("jsp/admin-editor-browse");
		modelAndView.addObject("CKEditor", ckEditor);
		modelAndView.addObject("CKEditorFuncNum", ckEditorFuncNum);
		modelAndView.addObject("langCode", langCode);
		modelAndView.addObject("imageFiles", imageFiles);
		modelAndView.addObject("browseDir", browseDir);
		modelAndView.addObject("homeDir", imagesDir.getPath());
		modelAndView.addObject("currentDir", browseDir.replaceFirst(contentImagesPath, ""));

		return modelAndView;
	}
	
	/**
	 * on Path /editor/delete/image
	 * 
	 * Deletes the image at the specified path.  Must start with contentImagesPath. 
	 * 
	 * @param request
	 * @param response
	 * @param deleteParams
	 * @param bindResult
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/delete/image")
	public ModelAndView handleDeleteImageRequest(HttpServletRequest request,
			HttpServletResponse response,
			@Valid @ModelAttribute() DeleteImagesParameters deleteParams,
			BindingResult bindResult) throws IOException, JSONException {
		
		if (bindResult.hasErrors()) {
			throw new IOException("Your image or directory delete failed. Please ensure the filePath exists "
					+ "and has only got allowed characters in.");
		}
		
		
		// delete the file. 
		String filePath = deleteParams.getFilePath();
		File file = (new File (contentImagesPath+File.separator+filePath)).getCanonicalFile();
		boolean successful = false;
		
		if (file.exists() && !file.isDirectory()) {
			successful = file.delete(); // delete file
		} 
		else if (file.exists() && file.list().length==0) {
			successful = file.delete(); // delete empty directory. 
		}
		

		JSONObject json = new JSONObject();
		json.put("deletesuccess", successful);

		response.setContentType("application/json");
		write(json.toString(), response.getOutputStream());
		return null;

	}	

	/**
	 * Builds a list of image files on the server to be displayed by the browse
	 * page.
	 * 
	 * Files should be under the directory specified in contentPath.
	 * 
	 * @param files
	 * @param browsePath
	 *            the path for the directory which this BrowseFile should
	 *            represent.
	 * @return
	 */
	private BrowseFile buildFileHeirachy(File file) {

		if (!file.getPath().startsWith(contentImagesPath)) {
			return null;
		}

		String fileURL = contentImagesURL
				+ file.getPath().replaceFirst(contentImagesPath, "");

		if (!file.isDirectory()) {
			return new BrowseFile(file.getName(), file.getPath(), fileURL,
					file.isDirectory(), null);
		}

		ArrayList<BrowseFile> children = new ArrayList<BrowseFile>();
		for (int i = 0; i < file.listFiles().length; i++) {

			File child = file.listFiles()[i];
			children.add(buildFileHeirachy(child));
		}
		Collections.sort(children);
		return new BrowseFile(file.getName(), file.getPath(), fileURL,
				file.isDirectory(), children);
	}

	/**
	 * Picks out the directory currently being viewed and it's children.
	 * 
	 * @return
	 */
	private BrowseFile buildBrowseDirectory(String browseDir, BrowseFile imageFiles) {
		
		if (imageFiles.isDirectory() && imageFiles.getFilePath().equals(browseDir)) {
			return imageFiles;
		}
		
		List<BrowseFile> children = imageFiles.getChildren();
		if (children ==null) return null;
		
		for (int i=0; i<children.size(); i++) {
			BrowseFile f = buildBrowseDirectory(browseDir, children.get(i));
			if (f!=null) { return f;}		
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
	private synchronized boolean saveHTML(UpdateHTMLParameters writeParams)
			throws IOException {

		// Read in data from request
		String html = writeParams.getHtml();
		String filename = writeParams.getFilename();

		// Save the file to disk.
		return FileSave.save(contentHTMLPath, filename, new ByteArrayInputStream(
				html.getBytes("UTF-8")));

	}

	// Performs validation on parameters used for writing images.
	public static class AddImagesParameters {

		@NotNull
		private String CKEditor;

		@NotNull
		private String CKEditorFuncNum;

		@NotNull
		private String langCode;

		// File validation is separate. 
		private MultipartFile file;
		
		@NotNull
		@Pattern(regexp = "^[-_/A-Za-z0-9]*$", message = "Invalid directory")
		private String directory;

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

		public String getDirectory() {
			return directory;
		}

		public void setDirectory(String directory) {
			this.directory = directory;
		}
	}
	
	// Performs validation on parameters used for deleting images.
	public static class DeleteImagesParameters {
		
		@NotNull
		@Pattern(regexp = "^[-_/A-Za-z0-9]+(\\.(?i)(jpg|jpeg|png|gif|bmp))??$", message = "Invalid characters in filePath")
		private String filePath;
		
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
		public String getFilePath() {
			return filePath;
		}		
		
	}

	private void uploadFileValidation(AddImagesParameters uploadParams,
			BindingResult bindResult) throws IOException {
		if (uploadParams.getUpload().getSize() == 0) {
			bindResult.rejectValue("upload", "Upload file required.");
		}

		// check file extension / content type for image.
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^[-_A-Za-z0-9]+\\.(?i)(jpg|jpeg|png|gif|bmp)$");
		boolean isImageFile = pattern.matcher(
				uploadParams.getUpload().getOriginalFilename()).matches();
		boolean isImageContent = uploadParams.getUpload().getContentType()
				.startsWith("image");

		if (!isImageFile && isImageContent) {
			bindResult.rejectValue("upload",
					"Upload filename should be jpg, png, gif or bmp.");
		}
	}

	// Performs validation on parameters used for writing html.
	public static class UpdateHTMLParameters {

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

	private boolean write(String text, OutputStream outputStream) {
		PrintStream out = null;
		try {
			out = new PrintStream(new BufferedOutputStream(outputStream), true,
					"UTF-8");
			out.print(text);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		return true;
	}

}
