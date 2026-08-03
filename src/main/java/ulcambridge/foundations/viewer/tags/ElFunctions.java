package ulcambridge.foundations.viewer.tags;

import org.jsoup.Jsoup;
import org.springframework.web.util.UriUtils;
import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.dao.ItemsDao;
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

    public static Item getItem(ItemsDao itemDAO, String itemId) {
        return itemDAO.getItem(itemId);
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
        return UriUtils.encode(s, "UTF-8");
    }

    public static String join(Iterable<Object> items, String separator) {
        if(items == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for(Object item : items) {
            if(first)
                first = false;
            else
                sb.append(separator);
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * Strip HTML tags from input, returning the plain text content.
     * @see org.jsoup.nodes.Element#text()
     */
    public static String stripTags(String html) {
        return Jsoup.parse(html).text();
    }

    /** Upper-case the first letter, for displaying the lower-case release status values. */
    public static String capitalise(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private ElFunctions() { throw new RuntimeException("No instantiation"); }
}
