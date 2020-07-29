package ulcambridge.foundations.viewer;

import java.net.URI;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.ItemsDao;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.model.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class CollectionViewControllerTest {

    @Autowired
    @Qualifier("imageServerURL")
    protected URI imageServerURL;

    @Test
    public void testHandleRequest_CollectionIdAndPositivePageNumber() {
        ModelAndView modelAndView = createController().handleRequest("treasures", 1, imageServerURL);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdAndZeroPageNumber() {
        ModelAndView modelAndView = createController().handleRequest("treasures", 0, imageServerURL);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdAndNegativePageNumber() {
        ModelAndView modelAndView = createController().handleRequest("treasures", -1, imageServerURL);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdOnly() {
        ModelAndView modelAndView = createController().handleRequest("treasures", imageServerURL);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    private CollectionViewController createController() {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao());
        return new CollectionViewController(collectionFactory, mock(ItemsDao.class), "./html");
    }

}
