package ulcambridge.foundations.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

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
	@RequestMapping(value = "/transcription")
	public ModelAndView handleRequest(@RequestParam("url") String url,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// The url for the transcription specified should be something like the
		// following:.
		// http://www.newtonproject.sussex.ac.uk/view/extract/normalized/THEM00009/start=p001r&end=p001r
		// onload should be the function to call onpage load.

		// no url param or empty url - so no transcription available for this
		// page.
		if (url == null || url.trim().equals("")) {

			String page = "<html><head>"
					+ "<link href=\"styles/style-transcription.css\" rel=\"stylesheet\" type=\"text/css\" />\n"
					+ "</head><body><div class=\"transcription\"> No transcription available for this image. </div></body></html>";

			writePage(response, page);
			return null;
		}

		// Only allow requests from transcription providers specified in the
		// properties file.
		boolean allowedURL = false;
		String[] providers = Properties.getString("transcriptionProviders")
				.split(",");
		for (int i = 0; i < providers.length; i++) {
			String provider = providers[i];
			if (provider!=null && !provider.equals("") && url.startsWith(provider)) {
				allowedURL = true;
			}
		}
		if (!allowedURL) {
			return null;
		}

		String sourcePage = readContent(new URL(url));

		// String fullRequestURL = request.getRequestURL().append("?" +
		// request.getQueryString()).toString();

		// This should be http://www.newtonproject.sussex.ac.uk/
		String baseURL = url.substring(0, url.indexOf('/', 7));

		String transcriptionPage = generateNewtonTranscriptionPage(sourcePage,
				baseURL, url);

		writePage(response, transcriptionPage);

		return null;

	}

	private String readContent(URL url) throws IOException {

		URLConnection connection = (url).openConnection();
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

	private String generateNewtonTranscriptionPage(String sourcePage,
			String baseURL, String requestURL) {

		StringBuffer output = new StringBuffer();

		// replace any relative links, should all start with /mainui
		// FIXME - temporary until sussex has the appropriate feed setup.
		sourcePage = sourcePage.replaceAll("\"/mainui", "\"" + baseURL
				+ "/mainui");

		// FIXME remove problem js

		// replace any links to the view (diplomatic or normal)
		// thisURL = thisURL.replaceAll("&view=\\w", "");
		// sourcePage =
		// sourcePage.replaceAll("\"/view/extract/diplomatic/[\\w|/|\\d|&|=]*\"",
		// thisURL+"&view=diplomatic" );
		// sourcePage =
		// sourcePage.replaceAll("\"/view/extract/normalized/[\\w|/|\\d|&|=]*\"",
		// thisURL+"&view=normal" );

		// Add HTML tag and DOCTYPE.
		output.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html>");

		// include the page head from the source
		if (sourcePage.indexOf("<head>") != -1
				&& sourcePage.indexOf("</head>") != -1) {
			// output.append("<script type='text/javascript'>window.onerror = function() { alert (\"error\") }</script>\n");
			output.append(sourcePage.substring(sourcePage.indexOf("<head>"),
					sourcePage.indexOf("</head>")));
			output.append("<link href=\"styles/style-transcription.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

			output.append("</head><body><div class=\"transcription\">\n");
			// Add link to Newton Project
			output.append("<div class=\"transcription-credit\">Transcription by the <a target='_blank' href='"
					+ requestURL + "'>Newton Project</a></div>");
			// output.append("<div class=\"transcription-credit\">Transcription by the <a target='_blank' href='http://www.newtonproject.sussex.ac.uk/'>Newton Project</a></div>");

		}

		// include the content //<!--start-text-container-->
		if (sourcePage.indexOf("<div id=\"tei\">") != -1
				&& sourcePage.indexOf("<!--end-text-container-->") != -1) {

			output.append(sourcePage.substring(
					sourcePage.indexOf("<div id=\"tei\">"),
					sourcePage.indexOf("<!--end-text-container-->")));

		}

		// End Tag (inc end of transcription div.)
		output.append("</div><div id=\"navigation\"></div></body></HTML>");

		return output.toString();
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
