package ulcambridge.foundations.viewer.tags;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    /**
     * Encode a string for inclusion as a component of a URI.
     *
     * Note that <, >, " and ' are encoded, so the result should be safe to
     * include directly in HTML.
     *
     * @param s A string to URL encode
     * @return s with reserved characters % encoded using UTF-8 encoding.
     */
    public static String uriEnc(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            // This will never happen
            throw new AssertionError("UTF-8 did not exist", e);
        }
    }

    private ElFunctions() { throw new RuntimeException("No instantiation"); }
}
