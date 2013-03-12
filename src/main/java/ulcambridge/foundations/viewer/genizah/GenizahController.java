package ulcambridge.foundations.viewer.genizah;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
	
	public GenizahController(GenizahDao dataSource) {
		this.dataSource = dataSource;
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
		if (query.isAuthorQuery()) {
			List<AuthorBibliography> titles = dataSource.getTitlesByAuthor(queryString);
			modelAndView = new ModelAndView("jsp/genizah-byAuthor");
			modelAndView.addObject("titles", titles);
		} else if (query.isKeywordQuery()) {
			List<BibliographyEntry> titles = dataSource.getTitlesByKeyword(queryString);
			modelAndView = new ModelAndView("jsp/genizah-byKeyword");
			modelAndView.addObject("titles", titles);
		} else if (query.isClassmarkQuery()) {
			List<Fragment> fragments = dataSource.getFragmentsByClassmark(queryString);
			// TODO : switch to different view if there is only one fragment?
			modelAndView = new ModelAndView("jsp/genizah-byClassmark");
			modelAndView.addObject("fragments", fragments);
		} else {
			modelAndView = new ModelAndView("jsp/genizah-Landing");
		}
		
		if (modelAndView != null) {
			modelAndView.addObject("query", query);
		}
		return modelAndView;
	}
	
}
