package ulcambridge.foundations.viewer.search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import ulcambridge.foundations.viewer.forms.SearchForm;

/**
 * Used for facet refinement in search results.
 * 
 * @author jennie
 * 
 */
public class SearchUtil {

	public static String getURLParameters(SearchForm searchForm) {
		try {
			String params = "keyword=" + URLEncoder.encode(searchForm.getKeyword(), "UTF-8");

			if(searchForm.hasRecallScale()) {
				params += String.format("&amp;tagging=1&amp;recallScale=%s", searchForm.getRecallScale());
			}

			params += "&amp;fullText=" + URLEncoder.encode(searchForm.getFullText(), "UTF-8");
			params += "&amp;excludeText=" + URLEncoder.encode(searchForm.getExcludeText(), "UTF-8");
			params += "&amp;textJoin=" + URLEncoder.encode(searchForm.getTextJoin(), "UTF-8");
			params += "&amp;fileID=" + URLEncoder.encode(searchForm.getFileID(), "UTF-8");
			params += "&amp;shelfLocator=" + URLEncoder.encode(searchForm.getShelfLocator(), "UTF-8");
			params += "&amp;title=" + URLEncoder.encode(searchForm.getTitle(), "UTF-8");
			params += "&amp;author=" + URLEncoder.encode(searchForm.getAuthor(), "UTF-8");
			params += "&amp;subject=" + URLEncoder.encode(searchForm.getSubject(), "UTF-8");
			params += "&amp;location=" + URLEncoder.encode(searchForm.getLocation(), "UTF-8");
			
			if (searchForm.getYearStart()!=null && searchForm.getYearEnd()!=null) {
				params += "&amp;yearStart=" + searchForm.getYearStart();
				params += "&amp;yearEnd=" + searchForm.getYearEnd();
			}			
			
			Iterator<String> facetIterator = searchForm.getFacets().keySet().iterator();
			while (facetIterator.hasNext()) {
				String facet = facetIterator.next().toString();
				
				// uppercase first letter of facet
				char[] stringArray = facet.toCharArray();
				stringArray[0] = Character.toUpperCase(stringArray[0]);
				String facetUpper = new String(stringArray);
				
				params += "&amp;facet" + URLEncoder.encode(facetUpper, "UTF-8") + "="
						+ URLEncoder.encode(searchForm.getFacets().get(facet), "UTF-8");
			}
			return params;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getURLParametersWithoutFacet(SearchForm searchForm, String facetName) {
		try {

			String params = "keyword=" + URLEncoder.encode(searchForm.getKeyword(), "UTF-8");
			params += "&amp;fullText=" + URLEncoder.encode(searchForm.getFullText(), "UTF-8");
			params += "&amp;excludeText=" + URLEncoder.encode(searchForm.getExcludeText(), "UTF-8");
			params += "&amp;textJoin=" + URLEncoder.encode(searchForm.getTextJoin(), "UTF-8");
			params += "&amp;fileID=" + URLEncoder.encode(searchForm.getFileID(), "UTF-8");
			params += "&amp;shelfLocator=" + URLEncoder.encode(searchForm.getShelfLocator(), "UTF-8");
			params += "&amp;title=" + URLEncoder.encode(searchForm.getTitle(), "UTF-8");
			params += "&amp;author=" + URLEncoder.encode(searchForm.getAuthor(), "UTF-8");
			params += "&amp;subject=" + URLEncoder.encode(searchForm.getSubject(), "UTF-8");
			params += "&amp;location=" + URLEncoder.encode(searchForm.getLocation(), "UTF-8");
			
			if (searchForm.getYearStart()!=null && searchForm.getYearEnd()!=null) {
				params += "&amp;yearStart=" + searchForm.getYearStart();
				params += "&amp;yearEnd=" + searchForm.getYearEnd();
			}	
			
			Iterator<String> facetIterator = searchForm.getFacets().keySet().iterator();
			while (facetIterator.hasNext()) {
				String facet = facetIterator.next().toString();
				if (!facet.equals(facetName)) {
					
					// uppercase first letter of facet
					char[] stringArray = facet.toCharArray();
					stringArray[0] = Character.toUpperCase(stringArray[0]);
					String facetUpper = new String(stringArray);
					
					params += "&amp;facet" + URLEncoder.encode(facetUpper, "UTF-8")
							+ "="
							+ URLEncoder.encode(searchForm.getFacets().get(facet), "UTF-8");
				}
			}
			return params;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getURLParametersWithExtraFacet(SearchForm searchForm, String facetName,
			String facetValue) {
		try {
			
			// uppercase first letter of facet
			char[] stringArray = facetName.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			facetName = new String(stringArray);
			
			return getURLParameters(searchForm) + "&amp;facet"
					+ URLEncoder.encode(facetName, "UTF-8") + "="
					+ URLEncoder.encode(facetValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
