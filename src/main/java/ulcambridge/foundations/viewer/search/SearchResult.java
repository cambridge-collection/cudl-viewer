package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Holds information for an individual search result. Item id can be used to
 * pull back more information on an item from the ItemFactory.
 * 
 * @author jennie
 * 
 */
public class SearchResult implements Comparable<SearchResult> {

	private String title;
	private String id;
	private List<Facet> facets = new ArrayList<Facet>();
	private int score; // how relevant is this result, used for ordering.

	// Hashtable containing the page numbers and the matching
	// snippets that appear on that page.
	private Hashtable<Integer, List<String>> snippets = new Hashtable<Integer, List<String>>();

	public SearchResult(String title, String id, List<Facet> facets, int score,
			Hashtable<Integer, List<String>> snippets) {
		
		this.title = title;
		this.id = id;
		this.score = score;
		this.facets = facets;
		this.snippets = snippets;

	}

	public String getTitle() {
		return this.title;
	}

	public String getId() {
		return this.id;
	}

	public List<Facet> getFacets() {
		return this.facets;
	}

	/**
	 * @return Returns a Hashtable of Page numbers and a List of snippets that
	 *         occur on that page.
	 */
	public Hashtable<Integer, List<String>> getSnippets() {
		return this.snippets;
	}

	public void insertSnippets(Integer pageNumber, List<String> snippets) {
		this.snippets.put(pageNumber, snippets);
	}

	/**
	 * Highest score should appear first.
	 */
	public int compareTo(SearchResult o) {

		return ((SearchResult) o).score - this.score;

	}

	/**
	 * Search Results are considered the same if they have the same ID. So are
	 * about the same book. There should be only one SearchResult object per
	 * item (book).
	 */
	/*
	 * @Override public boolean equals(Object o) { if (o instanceof
	 * SearchResult) { SearchResult r = (SearchResult) o; return this.id ==
	 * r.id; } return false; }
	 * 
	 * @Override public int hashCode(Object o) { if (o instanceof SearchResult)
	 * { SearchResult r = (SearchResult) o; return this.id == r.id; } return
	 * false; }
	 */

}
