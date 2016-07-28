package ulcambridge.foundations.viewer.rdf;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.Charsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.DCTypes;
import com.hp.hpl.jena.vocabulary.RDF;

import ulcambridge.foundations.viewer.JSONReader;
import ulcambridge.foundations.viewer.crowdsourcing.model.Annotation;
import ulcambridge.foundations.viewer.crowdsourcing.model.Position;
import ulcambridge.foundations.viewer.model.Properties;
import ulcambridge.foundations.viewer.rdf.vocab.Content;
import ulcambridge.foundations.viewer.rdf.vocab.Foaf;
import ulcambridge.foundations.viewer.rdf.vocab.Oa;

/**
 *
 * @author Lei
 *
 */
public class RDFReader {

    private Model model;

    private String baseUrl;

    private String userId;

    public RDFReader(String userId, String baseUrl) {
        // initialise model
        model = ModelFactory.createDefaultModel();
        model.setNsPrefix("foaf", Foaf.NS);
        model.setNsPrefix("dctypes", DCTypes.NS);
        model.setNsPrefix("oa", Oa.NS);
        model.setNsPrefix("cnt", Content.NS);
        model.setNsPrefix("dcterms", DCTerms.NS);

        this.baseUrl = baseUrl;
        this.userId = userId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Model getModel() {
        return model;
    }

    public void addElement(Annotation annotation, String documentId) {
        if (model == null)
            return;

        String name = annotation.getName();
        String type = annotation.getType();
        String uuid = annotation.getUuid().toString();
        Date date = annotation.getDate();
        StringBuilder posn = new StringBuilder();
        Position position = annotation.getPosition();

        if (position.getCoordinates().size() <= 0) {
            posn.append("0,0,0,0");
        } else {
            posn.append(position.formatCoordinatesToFragmentSelector());
        }
        String dziUrl = getDisplayImageURL(documentId, annotation.getPage());

        //
        // add element to model
        //

        Resource annoRes = model.createResource(baseUrl + "annotation/" + uuid, Oa.Annotation);

        // Oa.motivatedBy
        annoRes.addProperty(Oa.motivatedBy, Oa.tagging);

        // Oa.hasBody
        annoRes.addProperty(Oa.hasBody,
                model.createResource(baseUrl + "annotation/tag/" + uuid, Oa.Tag).addProperty(RDF.type, Content.ContentAsText)
                        .addProperty(Content.chars, name).addProperty(Content.characterEncoding, Charsets.UTF_8.displayName())
                        .addProperty(DC.language, Locale.ENGLISH.getLanguage()).addProperty(DC.format, MediaType.TEXT_PLAIN_VALUE)
                        .addProperty(DC.type, getAnnotationTypeResource(type)));

        // Oa.hasTarget
        annoRes.addProperty(Oa.hasTarget,
                model.createResource(Oa.SpecificResource)
                        // Oa.hasSource
                        .addProperty(Oa.hasSource, model.createResource(dziUrl, DCTypes.Image).addProperty(DC.identifier, documentId))
                        // Oa.hasSelector
                        .addProperty(Oa.hasSelector, model.createResource(Oa.FragmentSelector)
                                .addProperty(DCTerms.conformsTo, "http://www.w3.org/TR/media-frags/").addProperty(RDF.value, posn.toString())));

        // Oa.annotatedBy
        annoRes.addProperty(Oa.annotatedBy, model.createResource("http://openid.com/" + userId, Foaf.Person).addProperty(Foaf.openid, userId));

        // Oa.annotatedAt
        annoRes.addProperty(Oa.annotatedAt, date.toString());
    }

    private Resource getAnnotationTypeResource(String type) {
        if (type.equals("person")) {
            return Foaf.Person;
        } else if (type.equals("about")) {
            return DC.description;
        } else if (type.equals("date")) {
            return DC.date;
        } else if (type.equals("place")) {
            return DCTerms.Location;
        } else {
            return DC.description;
        }
    }

    private String getDisplayImageURL(String documentId, int pageNo) {
        JSONReader jr = new JSONReader();
        StringBuilder dziUrl = new StringBuilder();

        JSONObject docJson;
        try {
            docJson = jr.readJsonFromUrl(Properties.getString("jsonURL") + documentId + ".json");
            JSONArray pagesJA = docJson.getJSONArray("pages");
            JSONObject pageJ = (JSONObject) pagesJA.get(pageNo - 1);
            String imageUrl = pageJ.getString("displayImageURL");
            dziUrl.append(Properties.getString("imageServer")).append(imageUrl.substring(1));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dziUrl.toString();
    }

}
