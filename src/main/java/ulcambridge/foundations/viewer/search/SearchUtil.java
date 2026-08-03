package ulcambridge.foundations.viewer.search;

import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.forms.SearchForm;

import java.util.Map;

/**
 * Used for facet refinement in search results.
 *
 * @author jennie
 *
 */
public final class SearchUtil {
    private SearchUtil() { throw new RuntimeException(); }

    private static String getQuery(UriComponentsBuilder builder) {
        return builder.build().encode().getQuery();
    }

    private static UriComponentsBuilder addBaseQueryParams(
            UriComponentsBuilder builder,
            SearchForm searchForm) {

        builder.queryParam("keyword", searchForm.getKeyword())
            .queryParam("fullText", searchForm.getFullText())
            .queryParam("excludeText", searchForm.getExcludeText())
            .queryParam("textJoin", searchForm.getTextJoin())
            .queryParam("fileID", searchForm.getFileID())
            .queryParam("shelfLocator", searchForm.getShelfLocator())
            .queryParam("title", searchForm.getTitle())
            .queryParam("author", searchForm.getAuthor())
            .queryParam("subject", searchForm.getSubject())
            .queryParam("language", searchForm.getLanguage())
            .queryParam("place", searchForm.getPlace())
            .queryParam("location", searchForm.getLocation())
            .queryParam("expandFacet", searchForm.getExpandFacet());

        if (searchForm.getYearStart() != null &&
                searchForm.getYearEnd() != null) {
            builder.queryParam("yearStart", searchForm.getYearStart())
                    .queryParam("yearEnd", searchForm.getYearEnd());
        }

        return builder;
    }

    private static UriComponentsBuilder addFacetQueryParams(
            UriComponentsBuilder builder,
            Iterable<Map.Entry<String, String>> facets) {

        StringBuilder facetString = new StringBuilder();
        for(Map.Entry<String, String> facet : facets) {

            facetString.append(getFacetString(facet.getKey(), facet.getValue()));
            facetString.append("||");
        }
        if (facetString.length() > 0) {
            builder.queryParam("facets",facetString.toString().substring(0, facetString.length() - 2));
        }

        return builder;
    }

    private static String getFacetString(String name, String value) {
        if(name == null || name.isEmpty() || value == null || value.isEmpty()) {
            throw new IllegalArgumentException("name or value was empty: " + name + ": " + value);
        }

        return name+ "::" + value;

    }

    public static String getURLParameters(SearchForm searchForm) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        addBaseQueryParams(builder, searchForm);
        addFacetQueryParams(builder, searchForm.getFacets().entrySet());
        return getQuery(builder);
    }

}
