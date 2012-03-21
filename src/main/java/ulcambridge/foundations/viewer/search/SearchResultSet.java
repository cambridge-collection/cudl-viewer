package ulcambridge.foundations.viewer.search;

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
	private List <FacetGroup> facets;
	private String spellingSuggestedTerm;
	private float queryTime;	
	private String error;

	public SearchResultSet(int totalDocs, String spellingSuggestedTerm, float queryTime,
			List<SearchResult> results, List<FacetGroup> facets, String error) {

		this.totalDocs = totalDocs;
		this.results = results;
		this.facets = facets;
		this.error = error;

	}

	public int getNumberOfResults() {
		return totalDocs;
	}
	
	public List<SearchResult> getResults() {
		return results;
	}
	
	public void addFacetGroup(int index, FacetGroup facet) {
		if (index<0 || index>facets.size()-1) {
			index = 0;
		}
		facets.add(index, facet);
	}
	
	public List<FacetGroup> getFacets() {
		return facets;
	}

	public String getSpellingSuggestedTerm() {
		return spellingSuggestedTerm;
	}

	public float getQueryTime() {
		return queryTime;
	}	
	
	public String getError() {
		 return error;
	}

}
