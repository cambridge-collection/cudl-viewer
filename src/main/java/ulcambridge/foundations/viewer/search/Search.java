package ulcambridge.foundations.viewer.search;

import ulcambridge.foundations.viewer.forms.SearchForm;

public interface Search {

	public SearchResultSet makeSearch(SearchForm searchForm);
	public SearchResultSet makeSearch(SearchForm searchForm, int start, int end);

}
