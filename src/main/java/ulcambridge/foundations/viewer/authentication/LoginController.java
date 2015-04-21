package ulcambridge.foundations.viewer.authentication;

import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Autowired
	public LoginController(OAuth2RestOperations userTemplate) {
		this.userTemplate = userTemplate;
	}

	@Autowired
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
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
		
		JSONObject json = new JSONObject(result);
		String id = json.getString("sub");
		String email = json.getString("email");
		String email_verified = json.getString("email_verified");
				
		// Record email address if present and verified.
		String emailEncoded = null;
		if (email_verified !=null && email!=null && email_verified.equals("true")) {
			emailEncoded = encode(email);
		}
		
		String usernameEncoded = "google:"+encode(id);
		
		// setup user in Spring Security and DB
		setupUser(usernameEncoded, emailEncoded, session);

		// forward to /mylibrary/
		response.sendRedirect("/mylibrary/");

		return null;
	}
	
	/**
	 * Encode (SHA-256) specified input and convert to hex. 
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String encode(String input) throws NoSuchAlgorithmException  {
		
		// generate hash
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(input.getBytes());
		byte bytes[] = messageDigest.digest();
		
		// convert to hex
		StringBuffer hash = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			hash.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return new String(hash);
		
	}
	
	/**
	 * Creates the user in Spring Security and in the database if needed and
	 * puts the userDetails in the session. 
	 * 
	 * @param id
	 * @param session
	 */
	public void setupUser(String username, String email, HttpSession session) {
		
		// Create user in database if required, and store details in user session.
		User user = usersDao.createUser(username, email);
		session.setAttribute("user", user);

		// Create user in Spring security
		Authentication auth = new PreAuthenticatedAuthenticationToken(username, null,
				AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(user.getUserRoles(), ",")));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}