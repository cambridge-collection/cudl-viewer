package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author Lei
 *
 */
public class JSONConverter {

    private Gson gson;

    public JSONConverter() {
        gson = GsonFactory.create();
    }

    public Annotation toAnnotation(JsonObject json) {
        if (gson == null)
            return null;
        return (Annotation) gson.fromJson(json, Annotation.class);
    }

    public Position toPosition(JsonObject json) {
        if (gson == null)
            return null;
        return (Position) gson.fromJson(json, Position.class);
    }

    public Coordinates toCoordinates(JsonObject json) {
        if (gson == null)
            return null;
        return (Coordinates) gson.fromJson(json, Coordinates.class);
    }

    public Tag toTag(JsonObject json) {
        if (gson == null)
            return null;
        return (Tag) gson.fromJson(json, Tag.class);
    }

    public DocumentTags toDocumentTags(JsonObject json) {
        if (gson == null)
            return null;
        return (DocumentTags) gson.fromJson(json, DocumentTags.class);
    }

    public DocumentAnnotations toDocumentAnnotations(JsonObject json) {
        if (gson == null)
            return null;
        return (DocumentAnnotations) gson.fromJson(json, DocumentAnnotations.class);
    }

    public UserAnnotations toUserAnnotations(JsonObject json) {
        if (gson == null)
            return null;
        return (UserAnnotations) gson.fromJson(json, UserAnnotations.class);
    }

    public JsonObject toJsonAnnotation(Annotation annotation) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(annotation));
    }

    public JsonObject toJsonPosition(Position position) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(position));
    }

    public JsonObject toJsonCoordinates(Coordinates coordinates) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(coordinates));
    }

    public JsonObject toJsonTag(Tag tag) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(tag));
    }

    public JsonObject toJsonDocumentTags(DocumentTags tags) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(tags));
    }

    public JsonObject toJsonDocumentAnnotations(DocumentAnnotations da) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(da));
    }

    public JsonObject toJsonUserAnnotations(UserAnnotations ua) {
        if (gson == null)
            return null;
        return (JsonObject) new JsonParser().parse(gson.toJson(ua));
    }

}
