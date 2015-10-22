package ulcambridge.foundations.viewer.tags;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;

/**
 * This class defines custom JSP Expression Language (EL) functions required to
 * interact with CUDL's domain classes.
 *
 * They're only intended to be used via JSP EL where directly calling non bean
 * getter methods is not supported.
 */
public final class ElFunctions {

    /**
     * @see CollectionFactory#getCollectionFromId(String)
     */
    public static Collection getCollection(CollectionFactory factory, String collectionId) {
        return factory.getCollectionFromId(collectionId);
    }

    /**
     * @see ItemFactory#getItemFromId(String)
     */
    public static Item getItem(ItemFactory factory, String itemId) {
        return factory.getItemFromId(itemId);
    }

    private ElFunctions() { throw new RuntimeException("No instantiation"); }
}
