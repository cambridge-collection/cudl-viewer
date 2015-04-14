package ulcambridge.foundations.viewer.authentication;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	protected final Log logger = LogFactory.getLog(getClass());
	private final OAuth2RestOperations userTemplate;
	private UserDetailsService userDetailsService;

	@Autowired
	public LoginController(OAuth2RestOperations userTemplate) {
		this.userTemplate = userTemplate;
	}

	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	// on path /auth/login/
	@RequestMapping(value = "/login")
	public ModelAndView handleLoginRequest(
			@RequestParam(value = "error", required = false) String error,
			ModelMap model) {

		ModelAndView modelAndView = new ModelAndView("jsp/login");
		model.put("error", error);
		return modelAndView;
	}

	// on path /auth/logout/
	@RequestMapping(value = "/logout")
	public ModelAndView handleLogoutRequest(
			@RequestParam(value = "error", required = false) boolean error,
			ModelMap model) {

		// TODO actually log out.

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
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String getDeniedPage() {

		return "jsp/accessdenied";
	}

	// Login using Google oauth.  Note this is also return uri.
	// on path /auth/oauth2/google 
	@RequestMapping(value = "/oauth2/google")
	public ModelAndView handleRequest(HttpServletResponse response)
			throws JSONException, IOException {

		// Make Google profile request 
		String result = userTemplate
				.getForObject(
						URI.create("https://www.googleapis.com/plus/v1/people/me/openIdConnect"),
						String.class);

		System.out.println("RESULT IS:" + result);
		JSONObject json = new JSONObject(result);
		String id = json.getString("sub");

		// Setup user in spring security
		UserDetails details = userDetailsService.loadUserByUsername(id);

		Authentication auth = new PreAuthenticatedAuthenticationToken(id, null,
				AuthorityUtils.createAuthorityList("ROLE_USER"));
		SecurityContextHolder.getContext().setAuthentication(auth);
		// https://www.googleapis.com/oauth2/v1/userinfo?alt=json

		// forward to /mylibrary/
		response.sendRedirect("/mylibrary/");

		return null;
	}
}