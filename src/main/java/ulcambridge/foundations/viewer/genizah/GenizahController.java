package ulcambridge.foundations.viewer.genizah;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.ItemFactory;

/**
 * Main controller for the Genizah fragment bibliography.
 * 
 * @author gilleain
 *
 */
@Controller
public class GenizahController {

	protected final Log logger = LogFactory.getLog(getClass());	
	private GenizahDao dataSource;
	private ItemFactory itemFactory;
	
	public GenizahController(GenizahDao dataSource) {
		this.dataSource = dataSource;
	}

	@Autowired
	public void setItemFactory(ItemFactory factory) {
		this.itemFactory = factory;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/genizah")
	public ModelAndView processSearch(HttpServletRequest request,
									  HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		Enumeration<String> paramNames = request.getParameterNames();
		String queryString = "";
		String queryType = "";
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (paramName != null && paramName.equals("query")) {
				queryString = request.getParameter(paramName);
			} else if (paramName != null && paramName.equals("queryType")) {
				queryType = request.getParameter(paramName);
			}
		}
		GenizahQuery query = new GenizahQuery(queryString, queryType);
		
		ModelAndView modelAndView = null;
		if (query.isClassmarkQuery()) {
			List<FragmentSearchResult> fragmentResults = dataSource.classmarkSearch(queryString);
			modelAndView = new ModelAndView("jsp/genizah-fragmentResults");
			modelAndView.addObject("fragmentResults", fragmentResults);
			modelAndView.addObject("itemFactory", itemFactory);
		} else if (query.isAuthor()) {
			List<BibliographySearchResult> titles = dataSource.authorSearch(queryString);
			modelAndView = new ModelAndView("jsp/genizah-bibliographyResults");
			modelAndView.addObject("titles", titles);
		} else if (query.isKeyword()) {
			List<BibliographySearchResult> titles = dataSource.keywordSearch(queryString);
			modelAndView = new ModelAndView("jsp/genizah-bibliographyResults");
			modelAndView.addObject("titles", titles);
		} else if (query.isClassmarkIdQuery()) {
			FragmentReferenceList fragmentReferences = dataSource.getFragmentReferencesByClassmark(queryString);
			modelAndView = new ModelAndView("jsp/genizah-fragmentReferences");
			modelAndView.addObject("fragmentReferences", fragmentReferences);
		} else if (query.isTitleIdQuery()) {
			int id = Integer.parseInt(queryString);	// TODO : error handling!
			BibliographyReferenceList bibliographyReferences = dataSource.getBibliographyReferencesByTitleId(id);
			modelAndView = new ModelAndView("jsp/genizah-bibliographyReferences");
			modelAndView.addObject("bibliographyReferences", bibliographyReferences);
			modelAndView.addObject("itemFactory", itemFactory);
		} else {
			modelAndView = new ModelAndView("jsp/genizah-Landing");
		}
		
		if (modelAndView != null) {
			modelAndView.addObject("query", query);
		}
		return modelAndView;
	}
	
}
