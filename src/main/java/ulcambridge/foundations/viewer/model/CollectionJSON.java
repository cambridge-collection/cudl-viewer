package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = {"@type"}, allowGetters = true)
public class CollectionJSON implements Comparable<CollectionJSON> {
    public static final String TYPE = "https://schemas.cudl.lib.cam.ac.uk/package/v1/collection.json";

    @JsonProperty("name")
    private CollectionName name;

    @JsonProperty("description")
    private CollectionDescription description;

    @JsonProperty("credit")
    private CollectionCredit credit;

    @JsonProperty("items")
    private List<Id> ids;

    @JsonProperty("collections")
    private List<Id> subcollections;

    @JsonIgnore
    private List<String> cudlItemIds;

    @JsonIgnore
    private int order = 0;

    public void setName(CollectionName name) {
        this.name = name;
    }

    public void setDescription(CollectionDescription description) {
        this.description = description;
    }

    public void setCredit(CollectionCredit credit) {
        this.credit = credit;
    }

    public void setIds(List<Id> ids) {
        this.ids = ids;
        cudlItemIds = new ArrayList<>();

        for (Id id: ids){
            String cudlItemId = FilenameUtils.getBaseName(id.getId());
            cudlItemIds.add(cudlItemId);
        }
    }

    @JsonProperty("@type")
    protected String getType() {
        return TYPE;
    }

    public CollectionName getName() {
        return name;
    }

    public CollectionDescription getDescription() {
        return description;
    }

    public CollectionCredit getCredit() {
        return credit;
    }

    public List<Id> getItemIds() {
        return ids;
    }

    public List<String> getCudlItemIds() { return cudlItemIds; }

    public List<Id> getSubCollectionIds() {
        return subcollections;
    }

    public void setSubCollectionIds(List<Id> ids) {
        subcollections = ids;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("class Collection {\n");

        sb.append("    @type: ").append(toIndentedString(getType())).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    credit: ").append(toIndentedString(credit)).append("\n");
        sb.append("    items: ").append(toIndentedString(ids)).append("\n");
        sb.append("    collections: ").append(toIndentedString(subcollections)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    @Override
    public int compareTo(CollectionJSON CollectionJSON) {
        Preconditions.checkNotNull(CollectionJSON);
        return Integer.compare(order, CollectionJSON.order);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CollectionJSON) {
            // Ignore any collection id for equality/hash
            return toString().equals(((CollectionJSON)obj).toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
