package ulcambridge.foundations.viewer.search;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Items;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SearchControllerTest extends BaseCUDLApplicationContextTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Search search;

    @Autowired
    private ItemsDao itemsDao;

    @Autowired @Qualifier("imageServerURL")
    private URI imageServerURL;

    /**
     * Tests the SearchController object
     */
    @Test
    public void testSearchController() throws Throwable {

        CollectionsDao collectionsdao = new MockCollectionsDao();

        SearchForm form = new SearchForm();
        form.setKeyword("Elementary Mathematics");
        form.setFacetCollection("Newton Papers");
        form.setFacetSubject("Algebra - Early works to 1800");
        form.setFacetLanguage("English, Latin and Greek");

        CollectionFactory collectionFactory = new CollectionFactory(collectionsdao);

        SearchController c = new SearchController(
            collectionFactory, mock(ItemsDao.class), new MockSearch(), imageServerURL);

        ModelAndView m = c.processSearch(form);

        SearchResultSet r = (SearchResultSet) m.getModelMap().get("results");

        // Expect one result
        assertEquals(1, r.getNumberOfResults());
        assertEquals(1, r.getResults().size());
        assertEquals("spellingSuggestedTerm", r.getSpellingSuggestedTerm());
        assertEquals(2.3f, r.getQueryTime(), 0.01);
        assertEquals("error", r.getError());
        assertEquals("Elementary Mathematics", form.getKeyword());

    }

    private void configureMockSearch(int start, int end) {
        doReturn(Items.getExampleItem("MS-ADD-04004")).when(itemsDao).getItem("MS-ADD-04004");
        doAnswer(answer((SearchForm searchForm, Integer _start, Integer _end) ->
            new MockSearch().makeSearch(searchForm, _start, _end)))
            .when(search).makeSearch(any(), eq(start), eq(end));
    }

    @ParameterizedTest(name = "/JSON?{0} uses start={1} end={2}")
    @CsvSource({
        "start=0&end=8,0,8",
        "'',0,8",
        "start=1&end=12,1,12",
    })
    public void jsonSearchUsesStartEndQueryParameters(String query, int start, int end) throws Exception {
        configureMockSearch(start, end);

        this.mockMvc.perform(get("/search/JSON?" + query))
            .andDo(MockMvcResultHandlers.log())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(search).makeSearch(Mockito.any(), Mockito.eq(start), Mockito.eq(end));
    }

    @Test
    public void jsonSearchRejectsNegativeStartValues() throws Exception {
        this.mockMvc.perform(get("/search/JSON?start=-1&end=8"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.violations[0].fieldName", Matchers.equalTo("start")))
            .andExpect(jsonPath("$.violations[0].message", Matchers.equalTo("must be greater than or equal to 0")));
    }

    @Test
    public void jsonSearchProducesExpectedJSONData() throws Exception {
        configureMockSearch(0, 8);
        String expectedJSON = new String(Files.readAllBytes(Paths.get(getClass().getResource("regular-ajax-response.json").toURI())), StandardCharsets.UTF_8);

        this.mockMvc.perform(get("/search/JSON"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJSON));
    }

    /**
     * Always returns the same result set to any query. Uses Results.xml instead
     * of connecting to a real search service.
     *
     * @author jennie
     *
     */
    private static class MockSearch implements Search {

        @Override
        public SearchResultSet makeSearch(SearchForm searchForm) {
            return makeSearch(searchForm, 1, 1);
        }
        @Override
        public SearchResultSet makeSearch(SearchForm searchForm, int start, int end) {

            // Read document from File
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Element dom = null;
            try {

                DocumentBuilder db = dbf.newDocumentBuilder();

                dom = (Element) db.parse("src/test/resources/Results.xml")
                        .getDocumentElement();

            } catch (Exception e) {
                e.printStackTrace();

            }

            // Get first result
            NodeList docHits = dom.getElementsByTagName("docHit");
            Element node = (Element) docHits.item(0);

            XTFSearch xtfSearch = new XTFSearch(URI.create("http://xtf.example.com"));

            SearchResult result = xtfSearch.createSearchResult(node);
            ArrayList<SearchResult> results = new ArrayList<SearchResult>();
            results.add(result);

            // Build facet list
            Facet f = new Facet("field", "band", 3, 1);
            Facet f2 = new Facet("field", "band2", 2, 2);

            ArrayList<Facet> facets = new ArrayList<Facet>();
            facets.add(f);
            facets.add(f2);

            FacetGroup g = new FacetGroup("field", facets, 5);

            ArrayList<FacetGroup> facetGroups = new ArrayList<FacetGroup>();
            facetGroups.add(g);

            return new SearchResultSet(1, "spellingSuggestedTerm", 2.3f,
                    results, facetGroups, "error");
        }

    }
}
