package ulcambridge.foundations.viewer;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.BookmarkDao;
import ulcambridge.foundations.viewer.model.Bookmark;
import ulcambridge.foundations.viewer.model.Item;

@Controller
public class MyLibraryController {

	protected final Log logger = LogFactory.getLog(getClass());
	private ItemFactory itemFactory;
	private BookmarkDao bookmarkDao;

	@Autowired
	public void setItemFactory(ItemFactory factory) {
		this.itemFactory = factory;
	}

	@Autowired
	public void setBookmarkDao(BookmarkDao bookmarkDao) {
		this.bookmarkDao = bookmarkDao;
	}

	// on path /mylibrary/
	@RequestMapping(value = "/")
	public ModelAndView handleRequest(Principal principal) {

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
		modelAndView.addObject("items", items);
		modelAndView.addObject("bookmarks", bookmarks);
		return modelAndView;
	}

	// on path /mylibrary/addbookmark
	@RequestMapping(value = "/addbookmark/*")
	public String handleAddBookmarkRequest(
			@RequestParam("itemId") String itemId,
			@RequestParam("page") int page, Principal principal, 
			@RequestParam("thumbnailURL") String thumbnailURL) {

		// User user =
		// (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// String username = user.getUsername(); //get logged in username

		// OpenIDAuthenticationToken token =
		// (OpenIDAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
		// token.get
		// List<OpenIDAttribute> attributes = token.getAttributes();

		Bookmark bookmark = new Bookmark(principal.getName(), itemId, page, thumbnailURL);
		bookmarkDao.add(bookmark);

		return "redirect:/mylibrary/";
	}

}