package ulcambridge.foundations.viewer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controls the download of a file from the storage, currently used for the
 * download of jpg image files under the creative commons license. 
 * 
 * @author jennie
 */
@Controller
public class DownloadController {

	protected final Log logger = LogFactory.getLog(getClass());

	// type should be url encoded twice.
	// on path /download/{type}/{name}.{ext}
	@RequestMapping(value = "/{type}/{name}.{ext}")
	public ModelAndView handleRequest(@RequestParam("path") String path,
			@PathVariable("type") String type,
			@PathVariable("name") String name, @PathVariable("ext") String ext,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// unencode
		type = URLDecoder.decode(type, "UTF-8");
		String filename = name + "." + ext;

		// FIXME validation.
		String baseURL = request.getRequestURL().substring(0,
				request.getRequestURL().indexOf("/", 9));
		URL url = new URL(baseURL + path);

		// Set the type of file to download.
		response.setContentType(type);

		// Set the name by which user will be prompted to save the file.
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ filename + "\"");

		InputStream in = url.openStream();
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		for (int b; (b = in.read()) != -1;) {
			out.write(b);
		}
		out.close();
		in.close();

		return null;
	}
}