package ulcambridge.foundations.viewer.forms;

import ulcambridge.foundations.viewer.model.Collection;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SearchForm {

    public static final double MIN_RECALL_SCALE = 0, MAX_RECALL_SCALE = 1;

    // Keyword information
    private String keyword = "";
    private String fullText = "";
    private String excludeText = "";
    private String textJoin = "and";
    private String shelfLocator = "";
    private String fileID = "";

    // Metadata
    private String title = "";
    private String author = "";
    private String subject = "";
    private String language = "";
    private String location = "";
    private Integer yearStart = null;
    private Integer yearEnd = null;
    private List<Collection> collections = new ArrayList<Collection>();

    // Search Facets
    private Map<String, String> facets = new Hashtable<String,String>();
    private String facetDate;
    private String facetSubject;
    private String facetCollection;
    private String facetLanguage;
    private String facetPlace;

    // Variable recall
    /**
     * The recallScale controls the variable recall in keyword searches
     * against tagging data.
     */
    // Note that Double is used over double as we need to be able to represent
    // the absence of a recallScale value as null.
    private Double recallScale = MIN_RECALL_SCALE;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getExcludeText() {
        return excludeText;
    }

    public void setExcludeText(String excludeText) {
        this.excludeText = excludeText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }


    public String getTextJoin() {
        return textJoin;
    }

    public void setTextJoin(String textJoin) {
        this.textJoin = textJoin;
    }

    public String getShelfLocator() {
        return shelfLocator;
    }

    public void setShelfLocator(String shelfLocator) {
        this.shelfLocator = shelfLocator;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    /** Metadata **/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getYearStart() {
        return yearStart;
    }

    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }

    public Integer getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections=collections;

    }

    /** Facets **/

    public String getFacetDate() {
        return facetDate;
    }

    public void setFacetDate(String facetDate) {
        this.facetDate = facetDate;
        facets.put("date", facetDate);
    }

    public String getFacetSubject() {
        return facetSubject;
    }

    public void setFacetSubject(String facetSubject) {
        this.facetSubject = facetSubject;
        facets.put("subject", facetSubject);
    }

    public String getFacetLanguage() {
        return facetLanguage;
    }

    public void setFacetLanguage(String facetLanguage) {
        this.facetLanguage = facetLanguage;
        facets.put("language", facetLanguage);
    }

    public String getFacetCollection() {
        return facetCollection;
    }

    public void setFacetCollection(String facetCollection) {
        if (facetCollection!=null && !facetCollection.trim().equals("")) {
          this.facetCollection = facetCollection;
          facets.put("collection", facetCollection);
        }
    }

    public String getFacetPlace() {
        return facetPlace;
    }

    public void setFacetPlace(String facetPlace) {
        this.facetPlace = facetPlace;
        facets.put("place", facetPlace);
    }

    public Map<String, String> getFacets() {

        return facets;
    }

    public boolean hasRecallScale() {
        return recallScale != null;
    }

    /**
     * @return The recall scale, always between {@link #MIN_RECALL_SCALE} and
     *             {@link #MAX_RECALL_SCALE}.
     */
    public Double getRecallScale() {
        assert recallScale == null || recallScale >= MIN_RECALL_SCALE && recallScale < MAX_RECALL_SCALE;
        return recallScale;
    }

    public void setRecallScale(Double recallScale) {
        this.recallScale = Math.min(MAX_RECALL_SCALE,
                Math.max(MIN_RECALL_SCALE, recallScale));
    }

    /**
     * Sets the values in this form to the values in the form passed in.
     *
     * @return
     */
    public void setValuesFrom(SearchForm input) {

        this.keyword = input.keyword;
        this.textJoin = input.textJoin;
        this.fullText = input.fullText;
        this.excludeText = input.excludeText;
        this.fileID = input.fileID;
        this.shelfLocator = input.shelfLocator;

        this.title = input.title;
        this.author = input.author;
        this.subject = input.subject;
        this.language = input.language;
        this.location = input.location;
        this.yearStart = input.yearStart;
        this.yearEnd = input.yearEnd;

        this.facetCollection = input.facetCollection;
        this.facetDate = input.facetDate;
        this.facetSubject = input.facetSubject;
        this.facetLanguage = input.facetLanguage;
        this.facetPlace = input.facetPlace;
        Hashtable<String, String> facets = new Hashtable<String, String>();
        facets.putAll(input.facets);
        this.facets = facets;
    }



}
