package ulcambridge.foundations.viewer.search;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** expandFacet binding comes from the MVC configuration, so needs the application context. */
public class SearchControllerExpandFacetBindingTest extends BaseCUDLApplicationContextTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private Search search;

    private List<String> boundExpandFacet(MockHttpServletRequestBuilder request) throws Exception {
        SearchResultSet empty = new SearchResultSet(0, "", 0f, new ArrayList<>(), new ArrayList<>(), "");
        doReturn(empty).when(search).makeFacetSearch(any(SearchForm.class));

        mockMvc.perform(request.param("keyword", "bees"))
            .andExpect(status().isOk());

        ArgumentCaptor<SearchForm> form = ArgumentCaptor.forClass(SearchForm.class);
        verify(search).makeFacetSearch(form.capture());
        return form.getValue().getExpandFacet();
    }

    @Test
    public void bindsCommaSeparatedExpandFacet() throws Exception {
        assertEquals(List.of("Subject", "Place"), boundExpandFacet(
            get("/search/JSONFacets").param("expandFacet", "Subject,Place")));
    }

    @Test
    public void bindsRepeatedExpandFacet() throws Exception {
        assertEquals(List.of("Subject", "Place"), boundExpandFacet(
            get("/search/JSONFacets").param("expandFacet", "Subject", "Place")));
    }
}
