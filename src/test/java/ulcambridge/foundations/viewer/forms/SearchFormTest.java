package ulcambridge.foundations.viewer.forms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@code facets} arrives straight off the query string, so a malformed value must
 * not throw: binding happens under {@code @Valid}, and an exception from a setter
 * turns the whole search page into a 400 constraint-violation response.
 */
public class SearchFormTest {

    @Test
    public void setFacetsParsesWellFormedPairs() {
        SearchForm form = new SearchForm();
        form.setFacets("Collection::Darwin Manuscripts||Page_Has_Transcription::No");

        assertEquals(2, form.getFacets().size());
        assertEquals("Darwin Manuscripts", form.getFacets().get("Collection"));
        assertEquals("No", form.getFacets().get("Page_Has_Transcription"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Collection::", "::", "", "Collection", "::Darwin Manuscripts"})
    public void setFacetsIgnoresPairsMissingEitherHalf(String facets) {
        SearchForm form = new SearchForm();
        form.setFacets(facets);

        assertTrue(form.getFacets().isEmpty(),
            "expected no facets from \"" + facets + "\", got " + form.getFacets());
    }

    @Test
    public void setFacetsKeepsTheGoodPairsAlongsideABadOne() {
        SearchForm form = new SearchForm();
        form.setFacets("Collection::Darwin Manuscripts||Subject::");

        assertEquals(1, form.getFacets().size());
        assertEquals("Darwin Manuscripts", form.getFacets().get("Collection"));
    }

    @Test
    public void setFacetsKeepsACollectionPathWhole() {
        SearchForm form = new SearchForm();
        form.setFacets("Collection::Royal Greenwich Observatory Archives::Papers of Nevil Maskelyne"
            + "||Place::Cambridge");

        assertEquals("Royal Greenwich Observatory Archives::Papers of Nevil Maskelyne",
            form.getFacets().get("Collection"));
        assertEquals("Cambridge", form.getFacets().get("Place"));
    }

    @Test
    public void getFacetsAsStringWritesACollectionPathBackUnchanged() {
        String facets = "Collection::Royal Greenwich Observatory Archives::Papers of Nevil Maskelyne";
        SearchForm form = new SearchForm();
        form.setFacets(facets);

        assertEquals(facets, form.getFacetsAsString());
    }

    @Test
    public void setFacetCollectionSelectsTheCollection() {
        SearchForm form = new SearchForm();
        form.setFacetCollection("Newton Papers");

        assertEquals("Newton Papers", form.getFacets().get("Collection"));
    }

    /**
     * An empty collection dropdown must not register a facet — the advanced search
     * form submits the field on every search.
     */
    @Test
    public void setFacetCollectionIgnoresAnEmptySelection() {
        SearchForm form = new SearchForm();
        form.setFacetCollection("");
        form.setFacetCollection("   ");

        assertTrue(form.getFacets().isEmpty());
        assertEquals("", form.getFacetsAsString());
    }

    @Test
    public void expandCollectionTreatsBlankAsUnset() {
        SearchForm form = new SearchForm();
        form.setExpandCollection("  ");

        assertEquals(null, form.getExpandCollection());
    }

    @Test
    public void expandCollectionIsNotPartOfTheQueryParams() {
        SearchForm form = new SearchForm();
        form.setKeyword("bees");
        form.setExpandCollection("Royal Greenwich Observatory Archives");

        assertFalse(form.getQueryParams().contains("expandCollection"));
    }

    @Test
    public void expandFacetIsNotPartOfTheQueryParams() {
        SearchForm form = new SearchForm();
        form.setKeyword("bees");
        form.setExpandFacet(List.of("Subject"));

        assertFalse(form.getQueryParams().contains("expandFacet"));
    }

    @Test
    public void setExpandFacetSplitsCommaSeparatedValues() {
        SearchForm form = new SearchForm();
        form.setExpandFacet(List.of("Subject,Place", "Languages", ""));

        assertEquals(List.of("Subject", "Place", "Languages"), form.getExpandFacet());
    }
}
