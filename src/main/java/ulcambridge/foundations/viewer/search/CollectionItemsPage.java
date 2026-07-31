package ulcambridge.foundations.viewer.search;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * One page of a collection's items plus the total number of items Solr holds for
 * that collection. Solr reports the total on the same response as the page, so
 * the carousel can paginate without a separate count query.
 */
public final class CollectionItemsPage {

    private static final CollectionItemsPage EMPTY =
        new CollectionItemsPage(Collections.emptyList(), 0, false);

    private final List<JSONObject> items;
    private final int total;
    private final boolean available;

    public CollectionItemsPage(final List<JSONObject> items, final int total) {
        this(items, total, true);
    }

    private CollectionItemsPage(final List<JSONObject> items, final int total,
                                final boolean available) {
        this.items = Collections.unmodifiableList(items);
        this.total = total;
        this.available = available;
    }

    /**
     * An empty page, used when Solr could not be reached. The zero total means the
     * carousel renders no pagination at all. This deliberately replaces an earlier
     * fallback to the collection file's item count: that count can include items
     * that were never indexed, so falling back to it paginated against pages Solr
     * had no items to fill.
     */
    public static CollectionItemsPage empty() {
        return EMPTY;
    }

    public List<JSONObject> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }

    /**
     * False only when Solr could not be reached. A page that is available but empty
     * means the collection has no indexed items — callers that render items
     * server-side need to tell those two apart to report an outage without also
     * reporting one for a genuinely empty collection.
     */
    public boolean isAvailable() {
        return available;
    }
}
