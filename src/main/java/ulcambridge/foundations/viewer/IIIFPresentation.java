package ulcambridge.foundations.viewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.UI;
import ulcambridge.foundations.viewer.model.UIThemeImage;

import java.net.URI;
import java.util.*;

/**
 * This class is for converting the JSON metadata into IIIF presentation 2.0 api metadata.
 *
 */
public class IIIFPresentation {

    private final static String SEP = "; ";
    private final static Logger LOG = LoggerFactory.getLogger(IIIFPresentation.class);

    private final String label;
    private String description;
    private final Map<String, String> metadata = new HashMap<>();
    private String attribution;
    private final String logoURL;
    private final String sourceDataURL;
    private String viewingDirection = "left-to-right";
    private final String id;
    private final JSONArray pages;
    private final JSONArray lsArray;
    private final String baseURL;
    private final String servicesURL;
    private final URI iiifImageServer;

    private final UI themeUI;

    public IIIFPresentation(Item item, String baseURL, String servicesURL, URI iiifImageServer, UI themeUI) throws JSONException {

        Assert.notNull(item, "item is required");
        Assert.notNull(baseURL, "baseURL is required");
        Assert.notNull(servicesURL, "servicesURL is required");
        Assert.notNull(iiifImageServer, "iiifImageServer is required");
        Assert.notNull(themeUI, "UI is required");

        id = item.getId();
        this.baseURL = baseURL;
        this.servicesURL = servicesURL;
        this.iiifImageServer = iiifImageServer;
        this.themeUI = themeUI;

        // TODO restrict image server to IIIFEnabled items
        // TODO transcriptions
        // TODO remove extra html tags in text
        JSONObject json = item.getJSON();

        /* Note only looking at descriptiveMetadata[0] as this holds metadata that applies to the whole document */
        JSONObject metadataObject = json.getJSONArray("descriptiveMetadata").getJSONObject(0);
        lsArray = json.getJSONArray("logicalStructures");
        sourceDataURL = servicesURL + json.getString("sourceData");
        pages = json.getJSONArray("pages");

        if (json.getInt("numberOfPages") != pages.length()) {
            LOG.warn("Page count mismatch in {}: expected {}, actual {}",
                id, json.getInt("numberOfPages"), pages.length());
        }

        // version of iiif presentation
        label = item.getTitle() + " (" + item.getShelfLocator() + ")";

        // Check for textDirection, default left-to-right
        if (json.has("textDirection")) {
            String td = json.getString("textDirection");
            if (td != null && td.equals("R")) {
                viewingDirection = "right-to-left";
            }
        }

        // metadataObject is an object containing key value pairs,
        // values could be strings/numbers or objects.
        //noinspection unchecked
        for (String key : (Set<String>) metadataObject.keySet()) {
            Object value = metadataObject.get(key);

            // Ignore non-JSONObjects.
            if (value instanceof JSONObject) {

                JSONObject valueObj = (JSONObject) value;
                metadata.putAll(getMetadataFromJSON(null, valueObj));
            }
        }

        description = item.getAbstract();

        // translate links in description
        description = description.replaceAll(
            "<a href='' onclick='store.loadPage\\(([0-9]+)\\);return false;'>",
            "<a href='"+baseURL+"/view/" + id + "/$1'>"
        );

        // navDate?
        // license
        attribution = "";
        String defaultAttribution = this.themeUI.getThemeUI().getAttribution();
        if (defaultAttribution!=null) { attribution += defaultAttribution+" "; }
        if (metadataObject.has("displayImageRights")) { attribution += metadataObject.get("displayImageRights")+ "  "; }
        if (metadataObject.has("downloadImageRights")) { attribution += metadataObject.get("downloadImageRights")+ "  "; }
        if (metadataObject.has("pdfRights")) { attribution += metadataObject.get("pdfRights")+ " "; }
        if (metadataObject.has("metadataRights")) { attribution += metadataObject.get("metadataRights"); }

        String logo = "";
        if (this.themeUI.getThemeUI()!=null && this.themeUI.getThemeUI().getImage("logo")!=null) {
            UIThemeImage logoImage = themeUI.getThemeUI().getImage("logo");
            logo = baseURL + logoImage.getSrc();
        }
        logoURL = logo;

        // seeAlso (source metadata)
        // within (collection?)
        // sequences
        // label

    }

    private Map<String, String> getMetadataFromJSON(String label, JSONObject json) throws JSONException {

        Map<String, String> metadata = new HashMap<>(json.length());

        // got through all JSONObjects that nested and call recursive function.
        //noinspection unchecked
        for (String key : (Set<String>) json.keySet()) {
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
                metadata = merge(metadata, getMetadataFromJSON(label, valueObj));
            }

            // nested array of values - should always be an array of JSONObjects.
            // Call this function recursively. Usually called 'values'.
            else if (value instanceof JSONArray) {
                JSONArray values = (JSONArray) value;
                for (int i = 0; i < values.length(); i++) {
                    JSONObject valueObj = values.getJSONObject(i);
                    metadata = merge(metadata, getMetadataFromJSON(label, valueObj));
                }
            }
        }

        return metadata;

    }

    private Map<String, String> merge(Map<String, String> one, Map<String, String> two) {

        Map<String, String> output = new HashMap<>(one.size() + two.size());
        // Add all from map1 and merged records
        for (Map.Entry<String, String> entry : one.entrySet()) {
            if (two.containsKey(entry.getKey())) {
                // merge
                String merged = entry.getValue() + SEP + two.get(entry.getKey());
                output.put(entry.getKey(), merged);
            } else {
                output.put(entry.getKey(), entry.getValue());
            }
        }
        // Add all remaining from map2
        for (Map.Entry<String, String> entry : two.entrySet()) {
            output.putIfAbsent(entry.getKey(), entry.getValue());
        }

        return output;

    }

    public JSONObject outputSimpleJSON() throws JSONException {

        JSONObject output = new JSONObject();
        output.put("@context", "http://iiif.io/api/presentation/2/context.json");
        output.put("@type", "sc:Manifest");
        output.put("@id", baseURL + "/iiif/" + id + "/simple");
        output.put("label", label);
        output.put("attribution", attribution);
        output.put("logo", logoURL);
        output.put("viewingDirection", viewingDirection);

        // create one default sequence through the book
        JSONArray sequences = new JSONArray();
        JSONObject seqObj = new JSONObject();
        JSONArray canvases = new JSONArray();

        seqObj.put("@id", baseURL + "/iiif/" + id + "/sequence");
        seqObj.put("@type", "sc:Sequence");
        seqObj.put("label", "Current Page Order");

        for (int i = 0; i < pages.length(); i++) {
            try {
                JSONObject page = pages.getJSONObject(i);

                JSONObject canvas = new JSONObject();
                canvas.put("@id", baseURL + "/iiif/" + id + "/canvas/" + (i + 1));
                canvas.put("@type", "sc:Canvas");
                canvas.put("label", page.getString("label"));
                canvas.put("height", page.getInt("imageHeight"));
                canvas.put("width", page.getInt("imageWidth"));

                // If transcription or translation present append annotation list to canvas
                // (This is generated by services)
                if (page.has("translationURL") || page.has("transcriptionDiplomaticURL")) {
                    JSONObject transObj = new JSONObject();
                    transObj.put("@id", servicesURL+"/v1/iiif/annotation/"+id+"/"+(i + 1));
                    transObj.put("@type", "sc:AnnotationList");

                    JSONArray otherContent = new JSONArray();
                    otherContent.put(transObj);
                    canvas.put("otherContent", otherContent);
                }

                JSONArray images = new JSONArray();
                JSONObject imageObj = new JSONObject();
                imageObj.put("@type", "oa:Annotation");
                imageObj.put("motivation", "sc:painting");
                imageObj.put("on", baseURL + "/iiif/" + id + "/canvas/" + (i + 1));

                String IIIFImagePath = page.getString("IIIFImageURL");
                String imageURL = this.iiifImageServer.resolve(IIIFImagePath).toString();
                JSONObject resource = new JSONObject();
                resource.put("@id", imageURL);
                resource.put("@type", "dctypes:Image");
                resource.put("format", "image/jpg");
                resource.put("height", page.getInt("imageHeight"));
                resource.put("width", page.getInt("imageWidth"));

                JSONObject service = new JSONObject();
                service.put("@context", "http://iiif.io/api/image/2/context.json");
                service.put("@id", imageURL);
                service.put("profile", "http://iiif.io/api/image/2/level1.json");

                resource.put("service", service);
                imageObj.put("resource", resource);
                images.put(imageObj);
                canvas.put("images", images);
                canvases.put(canvas);
            } catch (JSONException ex) {
                LOG.warn("Invalid metadata for {} page {}", id, i, ex);
            }
        }

        seqObj.put("canvases", canvases);
        sequences.put(seqObj);
        output.put("sequences", sequences);

        return output;

    }


    public JSONObject outputJSON() throws JSONException {

        JSONObject output = new JSONObject();
        output.put("@context", "http://iiif.io/api/presentation/2/context.json");
        output.put("@type", "sc:Manifest");
        output.put("@id", baseURL + "/iiif/" + id);
        output.put("label", label);
        output.put("description", description);
        output.put("attribution", attribution);
        output.put("logo", logoURL);
        output.put("viewingDirection", viewingDirection);
        output.put("seeAlso", sourceDataURL);

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

        seqObj.put("@id", baseURL + "/iiif/" + id + "/sequence");
        seqObj.put("@type", "sc:Sequence");
        seqObj.put("label", "Current Page Order");

        for (int i = 0; i < pages.length(); i++) {
            try {
                JSONObject page = pages.getJSONObject(i);

                JSONObject canvas = new JSONObject();
                canvas.put("@id", baseURL + "/iiif/" + id + "/canvas/" + (i + 1));
                canvas.put("@type", "sc:Canvas");
                canvas.put("label", page.getString("label"));
                canvas.put("height", page.getInt("imageHeight"));
                canvas.put("width", page.getInt("imageWidth"));


                // If transcription or translation present append annotation list to canvas
                // (This is generated by services)
                if (page.has("translationURL") || page.has("transcriptionDiplomaticURL")) {
                    JSONObject transObj = new JSONObject();
                    transObj.put("@id", servicesURL+"/v1/iiif/annotation/"+id+"/"+(i + 1));
                    transObj.put("@type", "sc:AnnotationList");

                    JSONArray otherContent = new JSONArray();
                    otherContent.put(transObj);
                    canvas.put("otherContent", otherContent);
                }

                JSONArray images = new JSONArray();
                JSONObject imageObj = new JSONObject();
                imageObj.put("@type", "oa:Annotation");
                imageObj.put("motivation", "sc:painting");
                imageObj.put("on", baseURL + "/iiif/" + id + "/canvas/" + (i + 1));

                String IIIFImagePath = page.getString("IIIFImageURL");
                String imageURL = this.iiifImageServer.resolve(IIIFImagePath).toString();
                JSONObject resource = new JSONObject();
                resource.put("@id", imageURL);
                resource.put("@type", "dctypes:Image");
                resource.put("format", "image/jpg");
                resource.put("height", page.getInt("imageHeight"));
                resource.put("width", page.getInt("imageWidth"));

                JSONObject service = new JSONObject();
                service.put("@context", "http://iiif.io/api/image/2/context.json");
                service.put("@id", imageURL);
                service.put("profile", "http://iiif.io/api/image/2/level1.json");

                resource.put("service", service);
                imageObj.put("resource", resource);
                images.put(imageObj);
                canvas.put("images", images);
                canvases.put(canvas);
            } catch (JSONException ex) {
                LOG.warn("Invalid metadata for {} page {}", id, i, ex);
            }
        }

        seqObj.put("canvases", canvases);
        sequences.put(seqObj);
        output.put("sequences", sequences);

        // Output structures
        JSONArray structures = new JSONArray(createRangeCanvasStructures(lsArray));
        JSONObject first = structures.getJSONObject(0);
        structures.put(0, first);

        output.put("structures", structures);

        return output;

    }

    /**
     * Generates a structure object with members array (of canvases and ranges together) as 2.1 spec.
     * Some clients do not support this yet.
     * <p>
     * FIXME: this is never used
     */
    private List<JSONObject> createMemberStructures(JSONArray lsArray) throws JSONException {

        ArrayList<JSONObject> output = new ArrayList<>();
        for (int i = 0; i < lsArray.length(); i++) {

            JSONObject structure = new JSONObject();
            JSONObject lsObject = lsArray.getJSONObject(i);
            String metaId = lsObject.getString("descriptiveMetadataID");
            structure.put("@id", baseURL + "/iiif/" + id + "/range/" + metaId);
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
                    String canvasId = baseURL + "/iiif/" + id + "/canvas/" + j;
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
     */
    private List<JSONObject> createRangeCanvasStructures(JSONArray lsArray) throws JSONException {

        List<JSONObject> output = new ArrayList<>();
        List<JSONObject> ranges = new ArrayList<>();
        for (int i = 0; i < lsArray.length(); i++) {

            JSONObject structure = new JSONObject();
            JSONObject lsObject = lsArray.getJSONObject(i);
            String metaId = lsObject.getString("descriptiveMetadataID");
            structure.put("@id", baseURL + "/iiif/" + id + "/range/" + metaId);
            structure.put("@type", "sc:Range");
            structure.put("label", lsObject.get("label"));

            JSONArray rangeIds = new JSONArray();
            JSONArray canvases = new JSONArray();

            int start = lsObject.getInt("startPagePosition");
            int end = lsObject.getInt("endPagePosition");

            // list ranges in this range
            if (lsObject.has("children")) {

                JSONArray childArray = lsObject.getJSONArray("children");
                List<JSONObject> children = createRangeCanvasStructures(childArray); // list of ranges
                for (JSONObject o : children) {
                    rangeIds.put(o.get("@id"));
                    ranges.add(o);
                }
                structure.put("ranges", rangeIds);

            } else {

              // list canvases in this range (only if no children)
              for (int j = start; j <= end; j++) {

                 String canvasId = baseURL + "/iiif/" + id + "/canvas/" + j;
                 canvases.put(canvasId);
              }
              structure.put("canvases", canvases);
            }

            output.add(structure);
        }
        output.addAll(ranges);
        return output;

    }
}
