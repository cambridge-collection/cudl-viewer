package ulcambridge.foundations.viewer.search;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import ulcambridge.foundations.viewer.forms.SearchForm;

/**
 * Filters for a collection page's item list: free text, facet values (keyed by
 * facet display name, e.g. "Subject") and a map area. Values are parsed the same
 * way as the search page's, so malformed input is dropped rather than rejected.
 */
public final class CollectionFilter {

    private static final CollectionFilter NONE = new CollectionFilter("", Collections.emptyMap(), "");

    private final String text;
    private final Map<String, String> facets;
    private final String bbox;

    private CollectionFilter(final String text, final Map<String, String> facets, final String bbox) {
        this.text = text;
        this.facets = Collections.unmodifiableMap(new LinkedHashMap<>(facets));
        this.bbox = bbox;
    }

    public static CollectionFilter none() {
        return NONE;
    }

    /**
     * @param text   free text, or null
     * @param facets facets as on the search page, {@code Name::value||Name::value}, or null
     */
    public static CollectionFilter of(final String text, final String facets) {
        return of(text, facets, null);
    }

    /**
     * @param text   free text, or null
     * @param facets facets as on the search page, {@code Name::value||Name::value}, or null
     * @param bbox   map area as {@code west,south,east,north}, or null
     */
    public static CollectionFilter of(final String text, final String facets, final String bbox) {
        final SearchForm form = new SearchForm();
        if (facets != null) {
            form.setFacets(facets);
        }
        form.setBbox(bbox);
        return new CollectionFilter(text == null ? "" : text.trim(), form.getFacets(), form.getBbox());
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getFacets() {
        return facets;
    }

    public String getBbox() {
        return bbox;
    }

    public boolean isEmpty() {
        return text.isEmpty() && facets.isEmpty() && bbox.isEmpty();
    }
}
