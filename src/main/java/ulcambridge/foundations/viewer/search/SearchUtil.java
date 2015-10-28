package ulcambridge.foundations.viewer.search;

import org.springframework.util.ObjectUtils;
import org.springframework.validation.ValidationUtils;
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
				.queryParam("location", searchForm.getLocation());

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
			String value = facet.getValue();

			if(value == null) {
				builder.queryParam(facet.getKey());
			}
			else {
				builder.queryParam(getFacetName(facet.getKey()), value);
			}
		}

		return builder;
	}

	private static final String getFacetName(String name) {
		if(name == null || name.length() == 0)
			throw new IllegalArgumentException("name was empty: " + name);

		return "facet" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private static Iterable<Map.Entry<String, String>> removeFacet(
			Iterable<Map.Entry<String, String>> facets, String excludedName) {

		List<Map.Entry<String, String>> filtered =
				new ArrayList<Map.Entry<String, String>>();
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
		builder.queryParam(getFacetName(facetName), facetValue);
		return getQuery(builder);
	}
}
