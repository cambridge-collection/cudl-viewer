package ulcambridge.foundations.viewer.model;

import java.util.List;

import org.json.JSONObject;

/**
 * This class contains a subset of the data in the JSON file for an item, which
 * we want to display at collection page level.
 *
 * @author jennie
 *
 */
public class EssayItem extends Item {

    protected String content;
    protected List<String> itemReferences;
    protected List<String> associatedPeople;
    protected List<String> associatedPlaces;
    protected List<String> associatedSubjects;
    protected List<String> associatedOrganisations;

    public EssayItem(String itemId, String itemType, String itemTitle,
            List<Person> authors, String itemShelfLocator, String itemAbstract,
            String itemThumbnailURL, String thumbnailOrientation,
            JSONObject itemJson, String content, List<String> itemReferences,
            List<String> people, List<String> places,
            List<String> organisations, List<String> subjects,
            List<String> pageLabels, List<String> pageThumbnailURLs) {

        super(itemId, itemType, itemTitle, authors, itemShelfLocator,
                itemAbstract, itemThumbnailURL, thumbnailOrientation,
                pageLabels, pageThumbnailURLs, itemJson);

        this.content = content;
        this.itemReferences = itemReferences;
        this.associatedPeople = people;
        this.associatedOrganisations = organisations;
        this.associatedPlaces = places;
        this.associatedSubjects = subjects;

    }

    public List<String> getItemReferences() {
        return itemReferences;
    }

    public String getContent() {
        return content;
    }

    public List<String> getAssociatedPeople() {
        return associatedPeople;
    }

    public List<String> getAssociatedOrganisations() {
        return associatedOrganisations;
    }

    public List<String> getAssociatedSubjects() {
        return associatedSubjects;
    }

    public List<String> getAssociatedPlaces() {
        return associatedPlaces;
    }

}
