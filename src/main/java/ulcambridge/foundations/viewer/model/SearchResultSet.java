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
	private List<SearchResult> results;
	private List <SearchResultFacet> facets;
	private String spellingSuggestedTerm;
	private float queryTime;	

	public SearchResultSet(int totalDocs, String spellingSuggestedTerm, float queryTime,
			List<SearchResult> results, List<SearchResultFacet> facets) {

		this.totalDocs = totalDocs;
		this.results = results;
		this.facets = facets;

	}

	public int getNumberOfResults() {
		return totalDocs;
	}
	
	public List<SearchResult> getResults() {
		return results;
	}
	
	public List<SearchResultFacet> getFacets() {
		return facets;
	}

	public String getSpellingSuggestedTerm() {
		return spellingSuggestedTerm;
	}

	public float getQueryTime() {
		return queryTime;
	}	

}
