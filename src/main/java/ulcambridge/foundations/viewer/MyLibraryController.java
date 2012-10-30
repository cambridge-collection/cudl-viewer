package ulcambridge.foundations.viewer;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
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
		modelAndView.addObject("username", principal.getName());
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

		Bookmark bookmark = new Bookmark(principal.getName(), itemId, page, thumbnailURL, new Date());
		bookmarkDao.add(bookmark);

		return "redirect:/mylibrary/";
	}
	
	// on path /mylibrary/deletebookmark
	@RequestMapping(value = "/deletebookmark/*")
	public String handleDeleteBookmarkRequest(
			@RequestParam("itemId") String itemId,
			@RequestParam("page") int page, Principal principal) {

		bookmarkDao.delete(principal.getName(), itemId, page);

		return "redirect:/mylibrary/";
	}	

}