package ulcambridge.foundations.viewer.search;

import org.json.JSONObject;
import ulcambridge.foundations.viewer.forms.SearchForm;

import java.util.List;
import java.util.Map;

public interface Search {

    public SearchResultSet makeSearch(SearchForm searchForm);
    public SearchResultSet makeSearch(SearchForm searchForm, int start, int end);
    public Map<String, String> getFacetNameMap();

    /**
     * Returns one page of a collection's item-level docs, in collection order, as
     * per-item {@code item} JSON objects ready for the collection carousel.
     */
    public List<JSONObject> getCollectionItems(String slug, int start, int rows);

    /**
     * Returns the total number of item-level docs held for a collection, or -1 if
     * that total is unavailable. Callers should fall back to their own count on -1.
     */
    public int getCollectionItemCount(String slug);

}
