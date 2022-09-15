package ulcambridge.foundations.viewer;

import java.nio.file.Path;
import java.util.Map;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
            new MockCollectionsDao(), "true", Path.of("cudl-data/"));
        return new CollectionViewController(collectionFactory, mock(ItemsDao.class), "./html");
    }

}
