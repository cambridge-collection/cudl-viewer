package ulcambridge.foundations.viewer.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * A results page load must not query the search API: the client's single
 * {@code /search/JSONAdvanced} call renders the page, so a server-side
 * {@code makeSearch} here would silently double every page load's upstream traffic.
 */
public class SearchControllerPageLoadTest extends BaseCUDLApplicationContextTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private Search search;

    @Test
    public void searchResultsPageDoesNotQueryTheSearchApi() throws Exception {
        mockMvc.perform(get("/search")
                .param("keyword", "bees"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/search-results"))
            .andExpect(model().attributeExists("form", "queryString"))
            .andExpect(model().attributeDoesNotExist("results"));

        verifyNoInteractions(search);
    }

    @Test
    public void advancedSearchResultsPageDoesNotQueryTheSearchApi() throws Exception {
        mockMvc.perform(get("/search/advanced/results")
                .param("keyword", "bees")
                .param("facets", "Collection::Darwin Manuscripts"))
            .andExpect(status().isOk())
            .andExpect(view().name("jsp/search-advancedresults"))
            .andExpect(model().attributeExists("form", "queryString"))
            .andExpect(model().attributeDoesNotExist("results"));

        verifyNoInteractions(search);
    }

    @Test
    public void emptySearchStillForwardsToTheQueryForm() throws Exception {
        mockMvc.perform(get("/search"))
            .andExpect(forwardedUrl("/search/query"));

        verifyNoInteractions(search);
    }
}
