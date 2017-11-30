package ulcambridge.foundations.viewer;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import ulcambridge.foundations.viewer.dao.MockCollectionsDao;
import ulcambridge.foundations.viewer.dao.MockItemsJSONDao;

import static org.junit.Assert.assertTrue;

public class CollectionViewControllerTest {

    @Test
    public void testHandleRequest_CollectionIdAndPageNumber() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", 1);
        assertTrue(modelAndView!=null);

    }

    @Test
    public void testHandleRequest_CollectionIdOnly() throws Exception {
        ModelAndView modelAndView = createController().handleRequest("treasures", null);
        assertTrue(modelAndView!=null);

    }

    private CollectionViewController createController() {
        CollectionFactory collectionFactory = new CollectionFactory(
            new MockCollectionsDao());
        ItemFactory itemFactory = new ItemFactory(
            new MockItemsJSONDao());
        return new CollectionViewController(collectionFactory, itemFactory);
    }

}
