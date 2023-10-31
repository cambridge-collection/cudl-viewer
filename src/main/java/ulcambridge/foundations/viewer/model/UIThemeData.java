package ulcambridge.foundations.viewer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.io.FilenameUtils;

import java.beans.ConstructorProperties;
import java.util.Hashtable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UIThemeData {

    @JsonProperty("collections")
    private final List<UICollection> collections;

    @JsonIgnore
    private final Hashtable<String,UICollection> collectionMap;

    @ConstructorProperties({"collections"})
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UIThemeData(@JsonProperty("collections") List<UICollection> collections) {

        this.collections = collections;
        this.collectionMap = new Hashtable<>();
        for (UICollection uiCollection: collections) {

            // Transform Id e.g. 'collections/example.collection.json' to urlSLug 'example'
            String urlSlug = FilenameUtils.getBaseName(uiCollection.getCollection().getId());
            if (urlSlug.endsWith(".collection")) {
                urlSlug = urlSlug.replace(".collection", "");
            }
            this.collectionMap.put (urlSlug, uiCollection);
        }


    }

    @JsonProperty("collections")
    public List<UICollection> getCollections() {
        return collections;
    }

    @JsonIgnore
    public UICollection getCollectionByCollectionId(String collectionId) {
        return collectionMap.get(collectionId);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        sb.append("    \"collections\": ").append(toIndentedString(collections)).append("\n");
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
}
