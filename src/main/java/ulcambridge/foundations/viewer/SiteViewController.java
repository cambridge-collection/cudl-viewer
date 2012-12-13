package ulcambridge.foundations.viewer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Properties;

@Controller
public class SiteViewController {

	protected final Log logger = LogFactory.getLog(getClass());
	private String showHoldingPage = Properties.getString("showHoldingPage");
	private ItemFactory itemFactory;

	@Autowired
	public void setItemFactory(ItemFactory factory) {
		this.itemFactory = factory;
	}

	// on path /
	@RequestMapping(value = "/")
	public ModelAndView handleRequest() {

		if (showHoldingPage != null && showHoldingPage.equals("true")) {
			ModelAndView modelAndView = new ModelAndView(
					"jsp/errors/holdingpage");
			return modelAndView;
		}

		ModelAndView modelAndView = new ModelAndView("jsp/index");
		ArrayList<Item> featuredItems = new ArrayList<Item>();
		String[] itemIds = Properties.getString("collection.featuredItems")
				.split("\\s*,\\s*");
		for (int i = 0; i < itemIds.length; i++) {
			String itemId = itemIds[i];
			featuredItems.add(itemFactory.getItemFromId(itemId));
		}
		modelAndView.addObject("featuredItems", featuredItems);
		modelAndView.addObject("downtimeWarning",
				Properties.getString("downtimeWarning"));

		return modelAndView;
	}

	// on path /auth/login/
	@RequestMapping(value = "/auth/login")
	public ModelAndView handleLoginRequest(
			@RequestParam(value = "error", required = false) boolean error,
			ModelMap model) {

		if (error == true) {
			// Assign an error message
			model.put("error",
					"Invalid username or password. Please try again.");
		} else {
			model.put("error", "");
		}

		ModelAndView modelAndView = new ModelAndView("jsp/login");
		return modelAndView;
	}

	// on path /auth/logout/
	@RequestMapping(value = "/auth/logout")
	public ModelAndView handleLogoutRequest(
			@RequestParam(value = "error", required = false) boolean error,
			ModelMap model) {

		ModelAndView modelAndView = new ModelAndView("jsp/login");
		model.put("error", "You have logged out.");
		return modelAndView;
	}

	/**
	 * Handles and retrieves the denied JSP page. This is shown whenever a
	 * regular user tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	// on path /auth/denied/
	@RequestMapping(value = "/auth/denied", method = RequestMethod.GET)
	public String getDeniedPage() {

		return "jsp/accessdenied";
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

	// on path /help/
	@RequestMapping(value = "/help")
	public ModelAndView handleHelpRequest() {

		ModelAndView modelAndView = new ModelAndView("jsp/help");
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

	// on path /robots.txt
	@RequestMapping(value = "/robots.txt")
	public ModelAndView handleRobots(HttpServletResponse response)
			throws IOException {

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(Properties.getString("robots.useragent"));
		out.println(Properties.getString("robots.disallow"));
		out.close();
		return null;
	}

}