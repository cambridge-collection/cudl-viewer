package ulcambridge.foundations.viewer.transcriptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Properties;

/**
 * Reads in the URL specified by the newtonDoc parameter and filters the content
 * to only display what we want.
 * 
 * @author jennie
 * 
 */
@Controller
public class TranscriptionViewController {

	protected final Log logger = LogFactory.getLog(getClass());

	// on path /transcription
	// passed url parameter which is url from the json data for this page.
	@RequestMapping(value = "/transcription")
	public ModelAndView handleRequest(@RequestParam("url") String url,
			@RequestParam("doc")  String doc, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// no url param or empty url - so no transcription available for this
		// page.
		if (url == null || url.trim().equals("")) {

			String page = "<html><head>"
					+ "<link href=\"styles/style-transcription.css\" rel=\"stylesheet\" type=\"text/css\" />\n"
					+ "</head><body><div class=\"transcription\"> No transcription available for this image. </div></body></html>";

			writePage(response, page);
			return null;
		}

		// relative url should be translated into a absolute url.
		if (url.startsWith("/")) {
			URL absoluteURL = new URL (new URL(request.getRequestURL().toString()),url);
			url = absoluteURL.toString();
		}

		if (!isAllowedURL(url)) {
			throw new IOException(
					"url does not exist is list of transcription providers");
		}

		String sourcePage = readContent(url);

		// Reformat the result.  FIXME - URL is hardcoded. Could do with a nice way of 
		// setting up transcriptions and URLs from the configuration. 
		String transcriptionPage;
		if (url.startsWith("http://www.newtonproject.sussex.ac.uk")) {
			TranscriptionFormatter formatter = new NewtonTranscriptionFormatter();
			transcriptionPage = formatter.format(url, sourcePage);
		
		} else{
			transcriptionPage = sourcePage;
		}
		
		writePage(response, transcriptionPage);
		
		return null;

	}

	protected String readContent(String url) throws IOException {

		URLConnection connection = new URL(url).openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		String inputLine;
		StringBuffer input = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			input.append(inputLine);
			input.append("\n");
		}

		try {
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return input.toString();

	}

	/**
	 * is this URL on the list of transcription providers?
	 **/
	protected boolean isAllowedURL(String url) throws IOException {

		String[] providers = Properties.getString("transcriptionProviders")
				.split(",");
		for (int i = 0; i < providers.length; i++) {
			String provider = providers[i];
			if (provider != null && !provider.equals("")
					&& url.startsWith(provider)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets content type as html
	 * 
	 * @param response
	 * @param page
	 * @throws IOException
	 */
	private void writePage(HttpServletResponse response, String page)
			throws IOException {
		response.setHeader("content-type", "text/html");
		PrintWriter out = response.getWriter();
		out.print(page);
		out.close();
	}
}
