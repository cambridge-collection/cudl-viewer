package ulcambridge.foundations.viewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteViewController {

	protected final Log logger = LogFactory.getLog(getClass());
	
	// on path /
	@RequestMapping(value = "/")
	public ModelAndView handleRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/index");
		return modelAndView;
	}
		
	// on path /news/
	@RequestMapping(value = "/news/")
	public ModelAndView handleNewsRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/news");
		return modelAndView;
	}	

	// on path /about/
	@RequestMapping(value = "/about/")
	public ModelAndView handleAboutRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/about");
		return modelAndView;
	}	
	
	// on path /terms/
	@RequestMapping(value = "/terms/")
	public ModelAndView handleTermsRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/terms");
		return modelAndView;
	}	
	
	// on path /contributors/
	@RequestMapping(value = "/contributors/")
	public ModelAndView handleContributorsRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/contributors");
		return modelAndView;
	}		
	
}