package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ulcambridge.foundations.viewer.model.Collection;

import java.util.*;

/**
 * This class is for outputting collection data in IIIF json-ld.
 *
 */
public class IIIFCollection {

    private String id;
    private String label;
    private String description;
    private Collection collection;
    private List<String> itemIds; /** IIIFEnabled items only **/
    private List<IIIFCollection> subCollections = new ArrayList<>();
    private String baseURL;

    public IIIFCollection(Collection collection, String baseURL) throws JSONException {

        this(collection.getId(),collection.getTitle(),collection.getMetaDescription(),
            collection, collection.getSubCollections(),
            collection.getIIIFEnabledItemIds(), baseURL);

            //"All the available IIIF items available from Cambridge University Library."),
            // baseURL);

    }

    public IIIFCollection(String collectionId, String label, String description,
        Collection collection, List<Collection> subCollections,
                          List<String> itemIds, String baseURL) throws JSONException {

        /** TODO add attribution for collections, when we have the data to do this. **/
        /** TODO add collection and item thumbnails **/
        /** TODO negotiation of which version to provide **/

        // Only include items which have iiif enabled.
        // Remember some collections also have sub-collections (e.g. board of longitude).
        this.baseURL = baseURL;

        // Find subcollections
        for (Collection c : subCollections) {
            IIIFCollection ic = new IIIFCollection(c, baseURL);
            this.subCollections.add(ic);
        }

        // Set variables
            this.id = baseURL + "/iiif/collection/" + collectionId;
            this.label = label;
            this.description = description;
            this.collection = collection;
            this.itemIds = itemIds;

    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public List<IIIFCollection> getSubCollections() {
        return subCollections;
    }

    public Collection getCollection() {
        return collection;
    }

    public JSONObject outputJSON() throws JSONException {
        return outputJSONv2();
    }

    public JSONObject outputJSONv2() throws JSONException {

        JSONObject output = new JSONObject();
        output.put("@context", "http://iiif.io/api/presentation/2/context.json");
        output.put("@type", "sc:Collection");
        output.put("@id", id);
        output.put("label", label);
        output.put("description", description);

        // We are hiding any collections with no IIIF manifests in.
        JSONArray collectJSON = new JSONArray();
        if (getSubCollections()!=null) {
            for (IIIFCollection subCollection : getSubCollections()) {
                if (subCollection.getItemIds().size()>0) {
                    JSONObject collectObj = new JSONObject();
                    collectObj.put("label", subCollection.getLabel());
                    collectObj.put("@type", "sc:Collection");
                    collectObj.put("@id", subCollection.getId());
                    collectJSON.put(collectObj);
                }
            }
            if (collectJSON.length()>0) {
                output.put("collections", collectJSON);
            }
        }

        JSONArray manifestJSON = new JSONArray();
        if (collection!=null && collection.getIIIFEnabledItemIds()!=null) {
            for (String itemId : collection.getIIIFEnabledItemIds()) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("label", itemId);
                itemObj.put("@type", "sc:Manifest");
                itemObj.put("@id", baseURL+"/iiif/"+itemId+".json");
                manifestJSON.put(itemObj);
            }
            if (manifestJSON.length()>0) {
                output.put("manifests", manifestJSON);
            }
        }


        return output;

    }

    public JSONObject outputJSONv3() throws JSONException {

        JSONObject output = new JSONObject();
        JSONArray context = new JSONArray();
        context.put("http://www.w3.org/ns/anno.jsonld");
        context.put("http://iiif.io/api/presentation/3/context.json");
        output.put("@context", context);
        output.put("type", "Collection");
        output.put("id", id);
        output.put("label", label);
        output.put("summary", description);

        // We are hiding any collections with no IIIF manifests in.
        // Also only outputting either collections (if available) or manifests.
        JSONArray itemJSON = new JSONArray();
        if (getSubCollections()!=null) {
            for (IIIFCollection subCollection : getSubCollections()) {
                if (subCollection.getItemIds().size()>0) {
                    JSONObject collectObj = new JSONObject();
                    collectObj.put("label", subCollection.getLabel());
                    collectObj.put("type", "Collection");
                    collectObj.put("id", subCollection.getId());
                    itemJSON.put(collectObj);
                }
            }
            if (itemJSON.length()>0) {
                output.put("items", itemJSON);
                return output;
            }
        }

        JSONArray manifestJSON = new JSONArray();
        if (collection!=null && collection.getIIIFEnabledItemIds()!=null) {
            for (String itemId : collection.getIIIFEnabledItemIds()) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("label", itemId);
                itemObj.put("type", "Manifest");
                itemObj.put("id", baseURL+"/iiif/"+itemId+".json");
                manifestJSON.put(itemObj);
            }
            if (manifestJSON.length()>0) {
                output.put("items", manifestJSON);
            }
        }

        return output;

    }

}
