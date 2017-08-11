package ulcambridge.foundations.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ulcambridge.foundations.viewer.model.Item;

public class IIIFPresentation {

    String label;
    String description;
    Map<String, String> metadata = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    String attribution;
    int numberOfPages;
    String id;
    JSONArray pages;
    JSONArray lsArray;

    // https://image02.cudl.lib.cam.ac.uk/fcgi-bin/iipsrv.fcgi?IIIF=/mnt/delivery/JPEG2000/MS/KK/MS-KK-00001-00024/JP2/MS-KK-00001-00024-000-00002.jp2/full/1000,/0/default.jpg
    String baseURL = "http://localhost:1111";
    String imageServerURL = "https://image02.cudl.lib.cam.ac.uk/fcgi-bin/iipsrv.fcgi?IIIF=";
    String imageFilePath = "/mnt/delivery/JPEG2000/";

    public IIIFPresentation(Item item, String baseURL) throws JSONException {

        id = item.getId();
        this.baseURL = baseURL;

        /** TODO check if we are allowed to display this metadata in IIIF - downloadImageRights? **/
        /** TODO transcriptions **/
        /** TODO rights **/
        /** TODO remove extra html tags in text **/
        /** TODO synchronisation **/
        JSONObject json = item.getJSON();

        /* Note only looking at descriptiveMetadata[0] as this holds metadata that applies to the whole document */
        JSONObject metadataObject = json.getJSONArray("descriptiveMetadata").getJSONObject(0);
        lsArray = json.getJSONArray("logicalStructures");
        numberOfPages = json.getInt("numberOfPages");
        String sourceData = json.getString("sourceData");
        pages = json.getJSONArray("pages");

        // version of iiif presentation
        label = item.getTitle() + " (" + item.getShelfLocator() + ")";


        // Iterator keys = metadataObject.keys();
        // while (keys.hasNext()) {

        // JSONObject obj = metadataObject.getJSONObject(key);

        // metadataObject is an object containing key value pairs,
        // values could be strings/numbers or objects.

        Iterator<String> keys = metadataObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = metadataObject.get(key);

            // Ignore non-JSONObjects.
            if (value instanceof JSONObject) {

                JSONObject valueObj = (JSONObject) value;
                metadata.putAll(getMetadataFromJSON(null, valueObj));
            }
        }

        description = item.getAbstract();
        // navDate?
        // license
        attribution = "Provided by Cambridge University Library";

        // seeAlso (source metadta)
        // within (collection?)
        // sequences
        // label
        // viewingdirection
        // canvases
        // height
        // width
        // images
        // label
        // othercontent (transcriptions?)
        // structures
        // label
        // canvases

    }

    private Map<String, String> getMetadataFromJSON(String label, JSONObject json) throws JSONException {

        Map<String, String> metadata = Collections.synchronizedMap(new LinkedHashMap<String, String>());

        // got through all JSONObjects that nested and call recursive function.
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);

            if (key.equals("display") && !value.equals("true")) {
                continue; // skip if display set to false (or rather not set to true);
            }

            else if (key.equals("label")) {
                label = value.toString();
            }

            else if (key.equals("displayForm")) {
                String displayForm = value.toString();
                String existingValue = "";
                if (metadata.containsKey(label)) {
                    existingValue = metadata.get(label) + "; ";
                }
                metadata.put(label, existingValue + displayForm);
            }

            // if JSONObject found, call this function recursively.
            else if (value instanceof JSONObject) {
                JSONObject valueObj = (JSONObject) value;
                metadata = merge(metadata, getMetadataFromJSON(label, valueObj), "; ");
            }

            // nested array of values - should always be an array of JSONObjects.
            // Call this function recursively. Usually called 'values'.
            else if (value instanceof JSONArray) {
                JSONArray values = (JSONArray) value;
                for (int i = 0; i < values.length(); i++) {
                    JSONObject valueObj = values.getJSONObject(i);
                    metadata = merge(metadata, getMetadataFromJSON(label, valueObj), "; ");
                }
            }
        }

        return metadata;

    }

    private Map<String, String> merge(Map<String, String> one, Map<String, String> two, String separator) {

        Hashtable<String, String> output = new Hashtable<String, String>();
        // Add all from map1 and merged records
        for (Entry<String, String> entry : one.entrySet()) {
            if (two.containsKey(entry.getKey())) {
                // merge
                String merged = entry.getValue() + separator + two.get(entry.getKey());
                output.put(entry.getKey(), merged);
            } else {
                output.put(entry.getKey(), entry.getValue());
            }
        }
        // Add all remaining from map2
        for (Entry<String, String> entry : two.entrySet()) {
            output.putIfAbsent(entry.getKey(), entry.getValue());
        }

        return output;

    }

    public JSONObject outputJSON() throws JSONException {

        JSONObject output = new JSONObject();
        output.put("@context", "http://iiif.io/api/presentation/2/context.json");
        output.put("@type", "sc:Manifest");
        output.put("@id", baseURL + "/view/iiif/" + id);
        output.put("label", label);
        output.put("description", description);
        output.put("attribution", attribution);

        JSONArray meta = new JSONArray();
        for (String key : metadata.keySet()) {
            String value = metadata.get(key);
            JSONObject metaObj = new JSONObject();
            metaObj.put("label", key);
            metaObj.put("value", value);
            meta.put(metaObj);
        }
        output.put("metadata", meta);

        // create one default sequence through the book
        JSONArray sequences = new JSONArray();
        JSONObject seqObj = new JSONObject();
        JSONArray canvases = new JSONArray();

        seqObj.put("@id", baseURL + "/view/iiif/" + id + "/sequence");
        seqObj.put("@type", "sc:Sequence");
        seqObj.put("label", "Current Page Order");
        for (int i = 0; i < pages.length(); i++) {

            JSONObject page = pages.getJSONObject(i);

            JSONObject canvas = new JSONObject();
            canvas.put("@id", baseURL + "/view/iiif/" + id + "/canvas/" + (i + 1));
            canvas.put("@type", "sc:Canvas");
            canvas.put("label", page.get("label"));
            canvas.put("height", 1000); // FIXME placeholder
            canvas.put("width", 750); // FIXME placeholder

            JSONArray images = new JSONArray();
            JSONObject imageObj = new JSONObject();
            imageObj.put("@type", "oa:Annotation");
            imageObj.put("motivation", "sc:painting");
            imageObj.put("on", baseURL + "/view/iiif/" + id + "/canvas/" + (i + 1));

            // MS/KK/MS-KK-00001-00024/JP2/MS-KK-00001-00024-000-00002.jp2
            // get parts needed from ID
            String[] idParts = id.split("-");

            // build filePath of the form MS/KK/MS-KK-00001-00024/JP2/MS-KK-00001-00024-000-00002.jp2
            String downloadImageUrl = page.getString("downloadImageURL");
            String imageName = downloadImageUrl.substring(downloadImageUrl.lastIndexOf("/") + 1);
            imageName = imageName.replaceAll(".jpg", ".jp2");
            String filePath = idParts[0] + "/" + idParts[1] + "/" + id + "/JP2/" + imageName;

            String imageURL = imageServerURL + imageFilePath + filePath;
            JSONObject resource = new JSONObject();
            // resource.put("@id", imageURL + "/full/,600/0/default.jpg");
            resource.put("@id", imageURL);
            resource.put("@type", "dctypes:Image");
            resource.put("format", "image/jpg");
            resource.put("height", 1000); // FIXME placeholder
            resource.put("width", 750); // FIXME placeholder

            JSONObject service = new JSONObject();
            service.put("@context", "http://iiif.io/api/image/2/context.json");
            service.put("@id", imageURL);
            service.put("profile", "http://iiif.io/api/image/2/level1.json");

            resource.put("service", service);
            imageObj.put("resource", resource);
            images.put(imageObj);
            canvas.put("images", images);
            canvases.put(canvas);

        }

        seqObj.put("canvases", canvases);
        sequences.put(seqObj);
        output.put("sequences", sequences);

        // Output structures
        JSONArray structures = new JSONArray(createRangeCanvasStructures(lsArray));
        JSONObject first = structures.getJSONObject(0);
        first.put("viewingHint", "top");
        structures.put(0, first);

        output.put("structures", structures);

        return output;

    }

    /**
     * Generates a structure object with members array (of canvases and ranges together) as 2.1 spec.
     * Some clients do not support this yet.
     * 
     * @param lsArray
     * @return
     * @throws JSONException
     */
    private List<JSONObject> createMemberStructures(JSONArray lsArray) throws JSONException {

        ArrayList<JSONObject> output = new ArrayList<JSONObject>();
        for (int i = 0; i < lsArray.length(); i++) {

            JSONObject structure = new JSONObject();
            JSONObject lsObject = lsArray.getJSONObject(i);
            String metaId = lsObject.getString("descriptiveMetadataID");
            structure.put("@id", baseURL + "/view/iiif/" + id + "/range/" + metaId);
            structure.put("@type", "sc:Range");
            structure.put("label", lsObject.get("label"));

            JSONArray members = new JSONArray();
            int start = lsObject.getInt("startPagePosition");
            int end = lsObject.getInt("endPagePosition");

            // list ranges in this range
            if (lsObject.has("children")) {

                JSONArray childArray = lsObject.getJSONArray("children");
                List<JSONObject> children = createMemberStructures(childArray); // list of ranges
                members.put(children);

            } else {

                // list canvases in this range (only if no children)
                for (int j = start; j <= end; j++) {
                    JSONObject member = new JSONObject();
                    String canvasId = baseURL + "/view/iiif/" + id + "/canvas/" + j;
                    member.put("@id", canvasId);
                    member.put("@type", "sc:Canvas");
                    member.put("label", pages.getJSONObject(j - 1).getString("label"));
                    members.put(member);
                }
            }

            if (members.length() > 0) {
                structure.put("members", members);
            }
            output.add(structure);
        }
        return output;

    }

    /**
     * Generates a structure object with canvases and ranges listed separately (as 2.0 spec)
     * 
     * @param lsArray
     * @return
     * @throws JSONException
     */
    private List<JSONObject> createRangeCanvasStructures(JSONArray lsArray) throws JSONException {

        ArrayList<JSONObject> output = new ArrayList<JSONObject>();
        for (int i = 0; i < lsArray.length(); i++) {

            JSONObject structure = new JSONObject();
            JSONObject lsObject = lsArray.getJSONObject(i);
            String metaId = lsObject.getString("descriptiveMetadataID");
            structure.put("@id", baseURL + "/view/iiif/" + id + "/range/" + metaId);
            structure.put("@type", "sc:Range");
            structure.put("label", lsObject.get("label"));

            JSONArray ranges = new JSONArray();
            JSONArray canvases = new JSONArray();

            int start = lsObject.getInt("startPagePosition");
            int end = lsObject.getInt("endPagePosition");

            // list ranges in this range
            if (lsObject.has("children")) {

                JSONArray childArray = lsObject.getJSONArray("children");
                List<JSONObject> children = createRangeCanvasStructures(childArray); // list of ranges
                for (JSONObject o : children) {
                    ranges.put(o.get("@id"));
                }
                structure.put("ranges", ranges);

            } else {

                // list canvases in this range (only if no children)
                for (int j = start; j <= end; j++) {

                    String canvasId = baseURL + "/view/iiif/" + id + "/canvas/" + j;
                    canvases.put(canvasId);
                }
                structure.put("canvases", canvases);
            }

            output.add(structure);
        }
        return output;

    }
}
