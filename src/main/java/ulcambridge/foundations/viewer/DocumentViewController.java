package ulcambridge.foundations.viewer;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for viewing a specific document or a specific page within a
 * document. Documents and pages are specified in the url requested.
 * 
 * @author jennie
 * 
 */
@Controller
public class DocumentViewController {

	protected final Log logger = LogFactory.getLog(getClass());

	// on path /view/
	@RequestMapping(value = "/")
	public ModelAndView handleRequest() throws Exception {

		ModelAndView modelAndView = new ModelAndView("jsp/collections");

		return modelAndView;
	}

	// on path /view/{docId}
	@RequestMapping(value = "/{docId}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			HttpServletRequest request) {

		String requestURL = request.getRequestURL().toString();
		String docURL = requestURL;

		if (docURL.lastIndexOf("/") == docURL.length() - 1) {
			// cut off last character.
			docURL = docURL.substring(0, docURL.length() - 1);
		}

		// check doc exists, if not return 404 page.
		if (!docExists(docURL)) {
			return new ModelAndView("jsp/errors/404");
		}

		ModelAndView modelAndView = new ModelAndView("jsp/document");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", 1); // defaults to first page.
		modelAndView.addObject("docURL", docURL);
		modelAndView.addObject("requestURL", requestURL);

		return modelAndView;
	}

	// on path /view/{docId}/{page}
	@RequestMapping(value = "/{docId}/{page}")
	public ModelAndView handleRequest(@PathVariable("docId") String docId,
			@PathVariable("page") String page, HttpServletRequest request) {

		String requestURL = request.getRequestURL().toString();
		String docURL = requestURL;
		if (docURL.lastIndexOf("/") == docURL.length() - 1) {
			// cut off last character.
			docURL = docURL.substring(0, docURL.length() - 1);
		}
		// cut off the page part of the url.
		docURL = docURL.substring(0, docURL.lastIndexOf("/"));

		// check doc exists, if not return 404 page.
		if (!docExists(docURL)) {
			return new ModelAndView("jsp/errors/404");
		}

		ModelAndView modelAndView = new ModelAndView("jsp/document");
		modelAndView.addObject("docId", docId);
		modelAndView.addObject("page", page);
		modelAndView.addObject("docURL", docURL);
		modelAndView.addObject("requestURL", requestURL);
		return modelAndView;
	}

	/**
	 * Checks to see if the document at the specified url exists on the file
	 * system or not.
	 * 
	 * @param docURL
	 * @return
	 */
	private boolean docExists(String docURL) {

		String[] docURLParts = docURL.split("/");

		// See if the json file is there.
		String jsonURL = docURLParts[0] + "//" + docURLParts[2] + "/json/"
				+ docURLParts[4] + ".json";

		try {
			URL url = new URL(jsonURL);

			if (((HttpURLConnection) url.openConnection()).getResponseCode() == 200) {
				return true;
			}
		} catch (Exception e) {
			/* do nothing */
		}
		return false;

	}
}