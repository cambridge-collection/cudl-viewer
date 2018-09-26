package ulcambridge.foundations.viewer.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.authentication.Urls.UrlCodecStrategy;
import ulcambridge.foundations.viewer.dao.BookmarkDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class LoginController {

    private final OAuth2RestOperations googleTemplate;
    private final OAuth2RestOperations facebookTemplate;
    private final OAuth2RestOperations linkedinTemplate;
    private final BookmarkDao bookmarkDao;
    private final UsersDao usersDao;
    private final UrlCodecStrategy urlCodecStrategy;

    @Autowired
    public LoginController(
        BookmarkDao bookmarkDao, UsersDao usersDao,
        @Qualifier("googleOauth") OAuth2RestOperations googleTemplate,
        @Qualifier("facebookOauth") OAuth2RestOperations facebookTemplate,
        @Qualifier("linkedinOauth") OAuth2RestOperations linkedinTemplate,
        UrlCodecStrategy urlCodec) {

        Assert.notNull(bookmarkDao);
        Assert.notNull(usersDao);
        Assert.notNull(googleTemplate);
        Assert.notNull(facebookTemplate);
        Assert.notNull(linkedinTemplate);
        Assert.notNull(urlCodec);

        this.bookmarkDao = bookmarkDao;
        this.usersDao = usersDao;
        this.googleTemplate = googleTemplate;
        this.facebookTemplate = facebookTemplate;
        this.linkedinTemplate = linkedinTemplate;
        this.urlCodecStrategy = urlCodec;
    }

    // on path /auth/login/
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView handleLoginRequest(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam("next") Optional<String> nextUrl,
            HttpServletRequest req) {


        ModelAndView modelAndView = new ModelAndView("jsp/login")
            .addObject("error", error);

        nextUrl.flatMap(next -> this.urlCodecStrategy
                .decodeUrl(Urls.getUrl(req), next))
            .map(url -> urlCodecStrategy.encodeUrl(Urls.getUrl(req), url))
            .ifPresent(url -> modelAndView.addObject("nextUrl", url));

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
}
