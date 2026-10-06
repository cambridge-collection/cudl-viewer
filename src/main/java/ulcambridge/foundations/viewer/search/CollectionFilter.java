package ulcambridge.foundations.viewer.search;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import ulcambridge.foundations.viewer.forms.SearchForm;

/**
 * Filters for a collection page's item list: free text and facet values (keyed
 * by facet display name, e.g. "Subject"). Facets are parsed the same way as the
 * search page's, so malformed input is dropped rather than rejected.
 */
public final class CollectionFilter {

    private static final CollectionFilter NONE = new CollectionFilter("", Collections.emptyMap());

    private final String text;
    private final Map<String, String> facets;

    private CollectionFilter(final String text, final Map<String, String> facets) {
        this.text = text;
        this.facets = Collections.unmodifiableMap(new LinkedHashMap<>(facets));
    }

    public static CollectionFilter none() {
        return NONE;
    }

    /**
     * @param text   free text, or null
     * @param facets facets as on the search page, {@code Name::value||Name::value}, or null
     */
    public static CollectionFilter of(final String text, final String facets) {
        final SearchForm form = new SearchForm();
        if (facets != null) {
            form.setFacets(facets);
        }
        return new CollectionFilter(text == null ? "" : text.trim(), form.getFacets());
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getFacets() {
        return facets;
    }

    public boolean isEmpty() {
        return text.isEmpty() && facets.isEmpty();
    }
}
