package ulcambridge.foundations.viewer.model;

import java.util.List;

/**
 * Holds information for a set of results generated from a user 
 * search.  Includes individual SearchResults and information about
 * the search itself. 
 * 
 * @author jennie
 *
 */
public class SearchResultSet {

	private int totalDocs;
	private float queryTime;
	private String query;
	private List<SearchResult> results;

	public SearchResultSet(int totalDocs, float queryTime,
			String query, List<SearchResult> results) {

		this.totalDocs = totalDocs;
		this.queryTime = queryTime;
		this.query = query;
		this.results = results;

	}

	public String getSearchQuery() {
		return query;
	}

	public int getNumberOfResults() {
		return totalDocs;
	}

	public float getSearchTime() {
		return queryTime;
	}
	
	public List<SearchResult> getResults() {
		return results;
	}

}
