package ulcambridge.foundations.viewer;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.dao.MockItemsJSONDao;
import ulcambridge.foundations.viewer.model.Collection;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CollectionViewControllerTest {

    @Test
    public void testHandleRequest_CollectionIdAndPageNumber() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", 1);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(1, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    @Test
    public void testHandleRequest_CollectionIdOnly() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", null);
        Map<String,Object> modelMap = modelAndView.getModel();
        Collection collection = (Collection)modelMap.get("collection");

        assertEquals(null, modelMap.get("pageNumber"));
        assertEquals("treasures", collection.getId());
    }

    private CollectionViewController createController() {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao());
        ItemFactory itemFactory = new ItemFactory(
            new MockItemsJSONDao());
        return new CollectionViewController(collectionFactory, itemFactory);
    }

}
