package ulcambridge.foundations.viewer.model;

import java.util.List;

/**
 * Object that represents a collection of items.
 *
 * @author jennie
 *
 */
public class Collection implements Comparable<Collection> {

    private static int orderCount = 1;
    private int order;
    private String id;
    private String title;
    private List<String> itemIds;
    private String url;
    private String summary;
    private String sponsors;
    private String type;
    private String parentCollectionId;
    private List<Collection> subCollections;
    private List<String> IIIFEnabledItemIds;

    /**
     * Additional column added for HTML &lt;meta name="Description" .../&gt; tag
     */
    private String metaDescription;

    /**
     * Constructor for a Collection setting the metaDescription to null.
     */
    public Collection(String collectionId, String collectionTitle,
                      List<String> collectionItemIds,
                      String collectionSummary, String collectionSponsors,
                      String collectionType, String parentCollectionId,
                      List<String> IIIFEnabledItemIds) {
        this(collectionId, collectionTitle, collectionItemIds,
            collectionSummary, collectionSponsors, collectionType,
            parentCollectionId, IIIFEnabledItemIds, null);
    }

    public Collection(String collectionId, String collectionTitle,
            List<String> collectionItemIds,
            String collectionSummary, String collectionSponsors,
            String collectionType, String parentCollectionId,
            List<String> IIIFEnabledItemIds, String metaDescription) {

        this.id = collectionId;
        this.title = collectionTitle;
        this.itemIds = collectionItemIds;
        this.url = "/collections/" + collectionId;
        this.summary = collectionSummary;
        this.sponsors = collectionSponsors;
        this.type = collectionType;
        this.parentCollectionId = parentCollectionId;
        this.metaDescription = metaDescription;
        this.IIIFEnabledItemIds = IIIFEnabledItemIds;

        orderCount++;
        this.order = orderCount;
    }

    public int getOrder() {
        return order;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public String getURL() {
        return url;
    }

    public String getSummary() {
        return summary;
    }

    public String getSponsors() {
        return sponsors;
    }

    public String getType() {
        return type;
    }

    public String getParentCollectionId() {
        return parentCollectionId;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setSubCollections(List<Collection> subCollections) {
        this.subCollections = subCollections;
    }

    public List<Collection> getSubCollections() { return subCollections; }

    public List<String> getIIIFEnabledItemIds() {
        return IIIFEnabledItemIds;
    }

    @Override
    public int compareTo(Collection o) {
        if (this.getOrder() > o.getOrder()) {
            return 1;
        } else if (this.getOrder() == o.getOrder()) {
            return 0;
        }
        return -1;

        // return getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Collection) {
            return this.id.equals(((Collection) o).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
