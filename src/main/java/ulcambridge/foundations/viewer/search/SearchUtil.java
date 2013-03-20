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
			params += "&amp;fileID=" + URLEncoder.encode(searchForm.getFileID(), "UTF-8");
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
			params += "&amp;fileID=" + URLEncoder.encode(searchForm.getFileID(), "UTF-8");			
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
