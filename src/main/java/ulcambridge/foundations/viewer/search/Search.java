package ulcambridge.foundations.viewer.search;

import ulcambridge.foundations.viewer.forms.SearchForm;

import java.util.Map;

public interface Search {

    public SearchResultSet makeSearch(SearchForm searchForm);
    public SearchResultSet makeSearch(SearchForm searchForm, int start, int end);
    public Map<String, String> getFacetNameMap();

}
