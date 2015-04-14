package ulcambridge.foundations.viewer.authentication;

import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private UsersDao usersDao;
	private UserDetailsService userDetailsService;

	@Autowired
	public LoginController(OAuth2RestOperations userTemplate) {
		this.userTemplate = userTemplate;
	}

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
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
	public ModelAndView handleRequest(HttpSession session, HttpServletResponse response)
			throws JSONException, IOException, NoSuchAlgorithmException {

		// Make Google profile request 
		String result = userTemplate
				.getForObject(
						URI.create("https://www.googleapis.com/plus/v1/people/me/openIdConnect"),
						String.class);
		// https://www.googleapis.com/oauth2/v1/userinfo?alt=json
		
		System.out.println("RESULT IS:" + result);
		JSONObject json = new JSONObject(result);
		String id = json.getString("sub");
		
		// Now we need to generate a unique username from the google id for our database. 
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(id.getBytes());
		byte byteData[] = messageDigest.digest();
		
		// convert new username to hex and add provider to the start. 
		StringBuffer hashedUsername = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
        	hashedUsername.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
		String username = new String("google:"+hashedUsername);
		
		// setup user in Spring Security and DB
		setupUser(username, session);

		// forward to /mylibrary/
		response.sendRedirect("/mylibrary/");

		return null;
	}
	
	/**
	 * Creates the user in Spring Security and in the database if needed and
	 * puts the userDetails in the session. 
	 * 
	 * @param id
	 * @param session
	 */
	public void setupUser(String username, HttpSession session) {
		
		// Create user in database if required, and store details in user session. 
		userDetailsService.loadUserByUsername(username);
		User user = usersDao.getActiveUserByUsername(username);
		session.setAttribute("user", user);

		// Create user in Spring security
		Authentication auth = new PreAuthenticatedAuthenticationToken(username, null,
				AuthorityUtils.createAuthorityList("ROLE_USER"));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}