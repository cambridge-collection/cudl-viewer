package ulcambridge.foundations.viewer;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteViewController {

	protected final Log logger = LogFactory.getLog(getClass());
	private ResourceBundle config = ResourceBundle.getBundle("cudl-global");
	private	String showHoldingPage = config.getString("showHoldingPage");
	
	// on path /
	@RequestMapping(value = "/")
	public ModelAndView handleRequest() {

		if (showHoldingPage!=null && showHoldingPage.equals("true")) {
			ModelAndView modelAndView = new ModelAndView(
					"jsp/errors/holdingpage");
			return modelAndView;
		}

		ModelAndView modelAndView = new ModelAndView("jsp/index");
		return modelAndView;
	}

	// on path /news/
	@RequestMapping(value = "/news")
	public ModelAndView handleNewsRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/news");
		return modelAndView;
	}

	// on path /about/
	@RequestMapping(value = "/about")
	public ModelAndView handleAboutRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/about");
		return modelAndView;
	}
	
	// on path /feedback/
	@RequestMapping(value = "/feedback")
	public ModelAndView handleFeedbackRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/feedback");
		return modelAndView;
	}	

	// on path /terms/
	@RequestMapping(value = "/terms")
	public ModelAndView handleTermsRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/terms");
		return modelAndView;
	}

	// on path /contributors/
	@RequestMapping(value = "/contributors")
	public ModelAndView handleContributorsRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/contributors");
		return modelAndView;
	}

	// on path /nojavascript
	@RequestMapping(value = "/nojavascript")
	public ModelAndView handleNoJavascriptRequest(
			@RequestParam("url") String url) {

		ModelAndView modelAndView = new ModelAndView("jsp/errors/nojavascript");
		modelAndView.addObject("requestURL", url);
		return modelAndView;
	}

	// on path /errors/404.html
	@RequestMapping(value = "/errors/404.html")
	public ModelAndView handle404() {

		ModelAndView modelAndView = new ModelAndView("jsp/errors/404");
		return modelAndView;
	}

	// on path /errors/500.html
	@RequestMapping(value = "/errors/500.html")
	public ModelAndView handle500() {

		ModelAndView modelAndView = new ModelAndView("jsp/errors/500");
		return modelAndView;
	}

}