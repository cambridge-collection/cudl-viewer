package ulcambridge.foundations.viewer.dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import ulcambridge.foundations.viewer.model.EssayItem;
import ulcambridge.foundations.viewer.model.Item;
import ulcambridge.foundations.viewer.model.Person;
import ulcambridge.foundations.viewer.model.Properties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class DefaultItemFactory implements ItemFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultItemFactory.class);

    //private final ItemStatusOracle itemStatusOracle;

    public DefaultItemFactory() {
      //  Assert.notNull(itemStatusOracle, "itemStatusOracle is required");
      //  this.itemStatusOracle = itemStatusOracle;
    }

    public Item itemFromJSON(String itemId, JSONObject itemJson) {
        String itemType = "bookormanuscript"; // default
        // Might have type, might not.
        if (itemJson.has("itemType")) {
            itemType = itemJson.getString("itemType");
        }

        if (itemType.equals("essay")) {

            Item bookItem = getBookItem(itemId, itemJson);
            return getEssayItem(itemId, itemJson, bookItem);

        } else {
            return getBookItem(itemId, itemJson);
        }
    }

    /**
     * Parse the JSON into a Item object (default root object).
     */
    private Item getBookItem(String itemId, JSONObject itemJson) {

        String itemType = "bookormanuscript";
        String itemTitle;
        List<Person> itemAuthors = new ArrayList<>();
        String itemShelfLocator = "";
        String itemAbstract = "";
        String itemThumbnailURL = "";
        String thumbnailOrientation = "";
        List<String> pageLabels = new ArrayList<>();
        List<String> pageThumbnailURLs = new ArrayList<>();

        String imageReproPageURL = "";

        try {

            // Pull out the information we want in our Item object
            JSONObject descriptiveMetadata = itemJson.getJSONArray(
                "descriptiveMetadata").getJSONObject(0);

            // Should always have title
            itemTitle = descriptiveMetadata.getJSONObject("title").getString(
                "displayForm");

            // Might have authors, might not.
            if (descriptiveMetadata.has("authors")) {
                itemAuthors = getPeopleFromJSON(descriptiveMetadata
                        .getJSONObject("authors").getJSONArray("value"),
                    "author");
            }

            // Might have shelf locator, might not.
            if (descriptiveMetadata.has("shelfLocator")) {
                itemShelfLocator = descriptiveMetadata.getJSONObject(
                    "shelfLocator").getString("displayForm");
            }

            // Might have abstract, might not.
            if (descriptiveMetadata.has("abstract")) {
                itemAbstract = descriptiveMetadata.getJSONObject("abstract")
                    .getString("displayForm");
            }

            // if not see if we have content (used in essay objects)
            // - NOTE reads content from page 0 only.
            JSONArray pages = itemJson.getJSONArray("pages");
            JSONObject page0 = pages.getJSONObject(0);

            if (itemAbstract.equals("") && page0.has("content")) {
                itemAbstract = page0.getString("content");
            }

            for (int pageIndex = 0; pageIndex < pages.length(); pageIndex++) {
                JSONObject page = pages.getJSONObject(pageIndex);
                pageLabels.add(page.getString("label"));
                if (page.has("thumbnailImageURL")) {
                    pageThumbnailURLs.add(page.getString("thumbnailImageURL"));
                } else {
                    pageThumbnailURLs.add("");
                }
            }

            // Might have Thumbnail image
            if (descriptiveMetadata.has("thumbnailUrl")
                && descriptiveMetadata.has("thumbnailOrientation")) {
                if (itemJson.has("itemType")) { itemType = itemJson.getString("itemType"); }
                if (itemType.equals("essay")) {
                    itemThumbnailURL = descriptiveMetadata.getString("thumbnailUrl");
                } else {
                    try {
                        URL url = new URL(
                            new URL(Properties.getString("imageServer")),
                            descriptiveMetadata.getString("thumbnailUrl"));
                        itemThumbnailURL = url.toString();
                    } catch (MalformedURLException ex) {
                        LOG.warn("Cannot construct valid thumbnail URL", ex);
                    }
                }

                thumbnailOrientation = descriptiveMetadata
                    .getString("thumbnailOrientation");

            }

            // Might have image rights reproduction URL
            if (descriptiveMetadata.has("imageReproPageURL")) {
                imageReproPageURL = descriptiveMetadata.getString("imageReproPageURL");
            }


        } catch (JSONException e) {
            LOG.error("Failed to load item: %s", itemId, e);
            return null;
        }

        return new Item(itemId, itemType, itemTitle, itemAuthors, itemShelfLocator,
            itemAbstract, itemThumbnailURL, thumbnailOrientation, imageReproPageURL,
            pageLabels, pageThumbnailURLs, true,
            false, itemJson);

        // NOTE IIIFEnabled is always true
        // tagging enabled is always false
    }

    private EssayItem getEssayItem(String itemId, JSONObject itemJson, Item parent) {

        String content = "";
        List<String> relatedItems = new ArrayList<>();
        List<String> associatedPeople = new ArrayList<>();
        List<String> associatedPlaces = new ArrayList<>();
        List<String> associatedOrganisations = new ArrayList<>();
        List<String> associatedSubjects = new ArrayList<>();
        JSONObject descriptiveMetadata = null;
        List<String> pageLabels = new ArrayList<>();
        List<String> pageThumbnailURLs = new ArrayList<>();

        try {
            JSONArray pages = itemJson.getJSONArray("pages");
            JSONObject page0 = pages.getJSONObject(0);

            for (int pageIndex = 0; pageIndex < pages.length(); pageIndex++) {
                JSONObject page = pages.getJSONObject(pageIndex);
                pageLabels.add(page.getString("label"));
            }

            // Get essay content
            content = page0.getString("content");

            descriptiveMetadata = itemJson.getJSONArray(
                "descriptiveMetadata").getJSONObject(0);

            // Get list of related items
            relatedItems = new ArrayList<>();
            JSONArray itemReferences = descriptiveMetadata.getJSONArray("itemReferences");
            for (int i=0; i<itemReferences.length(); i++) {
                JSONObject itemRef = itemReferences.getJSONObject(i);
                relatedItems.add(itemRef.getString("ID"));
            }

            try {
                // Associated people
                associatedPeople = getDisplayFormFromJSON(descriptiveMetadata
                    .getJSONObject("associated").getJSONArray("value"));
            } catch (JSONException ignored) {}

            try {
                // Associated Places
                associatedPlaces = getDisplayFormFromJSON(descriptiveMetadata
                    .getJSONObject("places").getJSONArray("value"));
            } catch (JSONException ignored) {}

            try {
                // Associated Organisations
                associatedOrganisations = getDisplayFormFromJSON(descriptiveMetadata
                    .getJSONObject("associatedCorps").getJSONArray("value"));
            } catch (JSONException ignored) {}

            try {
                // Associated Subjects
                associatedSubjects = getDisplayFormFromJSON(descriptiveMetadata
                    .getJSONObject("subjects").getJSONArray("value"));
            } catch (JSONException ignored) {}

        } catch (JSONException e) {
            LOG.warn("Cannot load metadata", e);
        }

        return new EssayItem(itemId, "essay", parent.getTitle(), parent.getAuthors(), parent.getShelfLocator(),
            parent.getAbstract(), parent.getThumbnailURL(), parent.getThumbnailOrientation(), parent.getJSON(),
            parent.getImageReproPageURL(),
            content, relatedItems, associatedPeople, associatedPlaces, associatedOrganisations, associatedSubjects,
            pageLabels, pageThumbnailURLs);
    }

    /**
     * This method takes a JSONArray and creates Person objects for each JSON
     * object in it.
     */
    private List<Person> getPeopleFromJSON(JSONArray json, String role) {

        ArrayList<Person> people = new ArrayList<>();

        if (json == null) {
            return people;
        }

        String authority = null;
        String authorityURI = null;
        String valueURI = null;
        String fullForm = null;
        String type = null;
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject personJSON = json.getJSONObject(i);

                String shortForm = personJSON.getString("shortForm");
                try {
                    fullForm = personJSON.getString("fullForm");
                    authority = personJSON.getString("authority");
                    authorityURI = personJSON.getString("authorityURI");
                    valueURI = personJSON.getString("valueURI");
                    type = personJSON.getString("type");
                } catch (JSONException e) {
                    /* ignore if not present */
                }
                new ArrayList<String>();
                Person person = new Person(fullForm, shortForm, authority,
                    authorityURI, valueURI, type, role);
                people.add(person);
            }

        } catch (JSONException e) {
            LOG.error("Error processing: %s", json, e);
        }

        return people;
    }

    /**
     * This method takes a JSONArray and returns a list of the displayForm for
     * each visible item.
     */
    private List<String> getDisplayFormFromJSON(JSONArray json) {

        ArrayList<String> displayForms = new ArrayList<>();

        if (json == null) {
            return displayForms;
        }

        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject objectJSON = json.getJSONObject(i);

                if (objectJSON.getString("display")!=null
                    && objectJSON.getString("display").equals("true")) {

                    String displayForm = objectJSON.getString("displayForm");
                    displayForms.add(displayForm);
                }
            }

        } catch (JSONException e) {
            LOG.error("Error processing: %s", json, e);
        }

        return displayForms;
    }

}
