package ulcambridge.foundations.viewer.search;

import org.springframework.web.util.UriComponentsBuilder;
import ulcambridge.foundations.viewer.forms.SearchForm;

import java.util.ArrayList;
import java.util.List;
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

        for(Map.Entry<String, String> facet : facets) {

            builder.queryParam("facets",getFacetString(facet.getKey(), facet.getValue()));
        }

        return builder;
    }

    private static String getFacetString(String name, String value) {
        if(name == null || name.isEmpty() || value == null || value.isEmpty()) {
            throw new IllegalArgumentException("name or value was empty: " + name + ": " + value);
        }

        return name+ "::" + value;

    }

    private static Iterable<Map.Entry<String, String>> removeFacet(
            Iterable<Map.Entry<String, String>> facets, String excludedName) {

        List<Map.Entry<String, String>> filtered =
                new ArrayList<>();
        for(Map.Entry<String, String> facet : facets) {
            if(!facet.getKey().equals(excludedName))
                filtered.add(facet);

        }
        return filtered;
    }

    public static String getURLParameters(SearchForm searchForm) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        addBaseQueryParams(builder, searchForm);
        addFacetQueryParams(builder, searchForm.getFacets().entrySet());
        return getQuery(builder);
    }

    public static String getURLParametersWithoutFacet(
            SearchForm searchForm, String facetName) {

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        addBaseQueryParams(builder, searchForm);
        addFacetQueryParams(builder,
                removeFacet(searchForm.getFacets().entrySet(), facetName));
        return getQuery(builder);
    }

    public static String getURLParametersWithExtraFacet(
            SearchForm searchForm, String facetName, String facetValue) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder = addBaseQueryParams(builder, searchForm);
        builder = addFacetQueryParams(builder,
                searchForm.getFacets().entrySet());
        builder.queryParam("facets",getFacetString(facetName,facetValue));
        return getQuery(builder);
    }

    public static String getURLParametersWithFacetExpanded(
        SearchForm searchForm, String facetName) {

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder = addBaseQueryParams(builder, searchForm);
        builder = addFacetQueryParams(builder,
            searchForm.getFacets().entrySet());
        builder.replaceQueryParam("expandFacet", facetName);
        return getQuery(builder);
    }

}
