package ulcambridge.foundations.viewer.search;

import ulcambridge.foundations.viewer.forms.SearchForm;

import java.util.Map;

public interface Search {

    public SearchResultSet makeSearch(SearchForm searchForm);
    public SearchResultSet makeSearch(SearchForm searchForm, int start, int end);
    public Map<String, String> getFacetNameMap();

    /**
     * Returns one page of a collection's item-level docs, in collection order, as
     * per-item {@code item} JSON objects ready for the collection carousel, along
     * with the total Solr holds for the collection so the client can paginate
     * without a second query.
     */
    public CollectionItemsPage getCollectionItems(String slug, int start, int rows);

}
