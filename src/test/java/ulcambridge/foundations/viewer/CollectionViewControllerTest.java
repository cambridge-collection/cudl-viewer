package ulcambridge.foundations.viewer;

import java.nio.file.Path;
import java.util.Map;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.search.CollectionItemsPage;
import ulcambridge.foundations.viewer.search.Search;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionViewControllerTest {

    @Test
    public void testHandleRequest_CollectionIdAndPositivePageNumber() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", 1);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdAndZeroPageNumber() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", 0);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdAndNegativePageNumber() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", -1);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdOnly() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures");
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    private CollectionViewController createController() {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao(), "true", Path.of("cudl-data/"), "");
        // MockCollectionsDao's collection is virtual, so rendering it fetches its
        // first batch of tiles from Solr.
        Search search = mock(Search.class);
        when(search.getCollectionItems(anyString(), anyInt(), anyInt()))
            .thenReturn(CollectionItemsPage.empty());
        return new CollectionViewController(collectionFactory, search, "./html", false, "");
    }

}
