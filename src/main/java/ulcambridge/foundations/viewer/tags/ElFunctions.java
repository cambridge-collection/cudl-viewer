package ulcambridge.foundations.viewer.tags;

import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.springframework.web.util.UriUtils;

import ulcambridge.foundations.viewer.CollectionFactory;
import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.forms.SearchForm;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.search.SearchUtil;

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
     * @see SearchUtil#getURLParametersWithoutFacet(SearchForm, String)
     */
    public static String urlParamsWithoutFacet(SearchForm form, String facet) {
        return SearchUtil.getURLParametersWithoutFacet(form, facet);
    }

    /**
     * @see SearchUtil#getURLParametersWithExtraFacet(SearchForm, String, String)
     */
    public static String urlParamsWithFacet(SearchForm form, String facet, String value) {
        return SearchUtil.getURLParametersWithExtraFacet(form, facet, value);
    }

    /**
     * Strip HTML tags from input, returning the plain text content.
     * @see org.jsoup.nodes.Element#text()
     */
    public static String stripTags(String html) {
        return Jsoup.parse(html).text();
    }

    private ElFunctions() { throw new RuntimeException("No instantiation"); }
}
