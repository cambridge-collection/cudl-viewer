package ulcambridge.foundations.viewer.transcriptions;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import ulcambridge.foundations.viewer.model.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class forwards any requests to /xtf to the xtf URL specified in the 
 * cudl-gloabl.properties file. 
 * 
 * This allows relative URLs to XTF to be used that will use the spacified 
 * version of XTF you have given (and for dev will the the dev XTF and for live
 * the live XTF without having to change the URL). 
 * 
 * @author jennie
 *
 */
@Controller
public class XTFProxy {

	protected final Log logger = LogFactory.getLog(getClass());

	// on path /transcription
	// passed url parameter which is url from the json data for this page.
	@RequestMapping(value = "/**")
	public ModelAndView handleViewRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// do nothing if proxy is off in the properties file. 
		if (!Properties.getString("useProxy").equals("true")) {
			return null;
		}
		
		URL url = new URL(new URL(Properties.getString("xtfURL")), request.getRequestURI()+"?"+request.getQueryString());

		BufferedOutputStream out = new BufferedOutputStream(
				response.getOutputStream());
		InputStream is = null;
		try {
			is = url.openStream();
			byte[] byteChunk = new byte[4096];

			int n;

			while ((n = is.read(byteChunk)) > 0) {
				out.write(byteChunk, 0, n);
			}
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (is != null) {
				is.close();
			}
			if (out != null) {
				out.close();
			}
		}

		return null;
	}
}

