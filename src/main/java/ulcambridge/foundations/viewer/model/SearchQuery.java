package ulcambridge.foundations.viewer.model;

import java.util.Map;

/**
 * Holds information for a Search Query including facet refinement.
 * 
 * @author jennie
 * 
 */
public class SearchQuery {

	private String keyword;
	private Map<String, String> facets;
	private String sort;
	
	/**
	 * Creates a new SearchQuery from the given Node.
	 */
	public SearchQuery(String keyword, Map map, String sort) {

		this.keyword = keyword;
		this.facets = map;
		this.sort = sort;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public Map<String, String> getFacets() {
		return facets;
	}

	public String getSort() {
		return sort;
	}

}
