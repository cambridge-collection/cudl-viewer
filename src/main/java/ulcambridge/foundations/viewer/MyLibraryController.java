package ulcambridge.foundations.viewer;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.BookmarkDao;
import ulcambridge.foundations.viewer.exceptions.TooManyBookmarksException;
import ulcambridge.foundations.viewer.model.Bookmark;
import ulcambridge.foundations.viewer.model.Item;

@Controller
public class MyLibraryController {

	protected final Log logger = LogFactory.getLog(getClass());
	private ItemFactory itemFactory;
	private BookmarkDao bookmarkDao;
	private final OAuth2RestOperations userTemplate;	

	@Autowired
	public void setItemFactory(ItemFactory factory) {
		this.itemFactory = factory;
	}

	@Autowired
	public void setBookmarkDao(BookmarkDao bookmarkDao) {
		this.bookmarkDao = bookmarkDao;
	}
	
    @Autowired
    public MyLibraryController (OAuth2RestOperations userTemplate) {
        this.userTemplate = userTemplate;
    } 	

	// on path /mylibrary/
	@RequestMapping(value = "/")
	public ModelAndView handleRequest(Principal principal) {

		System.out.println("userTemplate: "+userTemplate);
        System.out.println("code: "+userTemplate.getOAuth2ClientContext().getAccessTokenRequest().getAuthorizationCode());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }
        String clientId = null;

        if (authentication.getClass().isAssignableFrom(OAuth2Authentication.class)) {
            clientId = ((OAuth2Authentication) authentication).getOAuth2Request().getClientId();
        }

        System.out.println("clientId: "+clientId);
        String result = userTemplate.getForObject(URI.create("https://www.googleapis.com/plus/v1/people/me/openIdConnect"), String.class);
	
        //https://www.googleapis.com/oauth2/v1/userinfo?alt=json
        System.out.println("RESULT IS:" +result); 
        
        
		List<Bookmark> bookmarks = bookmarkDao.getByUsername(principal
				.getName());
		Iterator<Bookmark> bookmarksIt = bookmarks.iterator();

		// Get a list of Items that represent these bookmarks.
		List<Item> items = new ArrayList<Item>();

		while (bookmarksIt.hasNext()) {
			Bookmark bookmark = bookmarksIt.next();
			Item item = itemFactory.getItemFromId(bookmark.getItemId());
			items.add(item);
		}

		ModelAndView modelAndView = new ModelAndView("jsp/mylibrary");
		modelAndView.addObject("username", principal.getName());
		modelAndView.addObject("items", items);
		modelAndView.addObject("bookmarks", bookmarks);
		return modelAndView;
	}

	// on path /mylibrary/addbookmark
	@RequestMapping(value = "/addbookmark/*")
	public String handleAddBookmarkRequest(HttpServletResponse response,
			@RequestParam("itemId") String itemId,
			@RequestParam("page") int page, Principal principal,
			@RequestParam("thumbnailURL") String thumbnailURL,
			@RequestParam(value = "redirect", required = false) boolean redirect) {

		Bookmark bookmark = new Bookmark(principal.getName(), itemId, page,
				thumbnailURL, new Date());
		String error = null;

		try {
			bookmarkDao.add(bookmark);
		} catch (TooManyBookmarksException e1) {
			error = e1.getMessage();
		}

		if (redirect) {
			return "redirect:/mylibrary/";
		}

		// Write JSON response.
		String json = "{\"bookmarkcreated\":true}";
		if (error!=null) {
			json = "{\"bookmarkcreated\":false, \"error\":\""+error+"\"}";
		}
		try {			
			response.setContentType("application/json");
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	// on path /mylibrary/deletebookmark
	@RequestMapping(value = "/deletebookmark/*")
	public String handleDeleteBookmarkRequest(HttpServletResponse response,
			@RequestParam("itemId") String itemId,
			@RequestParam("page") int page, Principal principal,
			@RequestParam(value = "redirect", required = false) boolean redirect) {

		bookmarkDao.delete(principal.getName(), itemId, page);

		if (redirect) {
			return "redirect:/mylibrary/";
		} else {
			try {
				response.getWriter().write("{\"bookmarkdeleted\":true}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}