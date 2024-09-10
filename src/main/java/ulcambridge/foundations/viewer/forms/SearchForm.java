package ulcambridge.foundations.viewer.forms;

import ulcambridge.foundations.viewer.model.Collection;

import java.util.*;

public class SearchForm {

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
    private String place = "";
    private String location = "";
    private Integer yearStart = null;
    private Integer yearEnd = null;
    private List<Collection> collections = new ArrayList<Collection>();

    // Search Facets
    private Map<String,String> facets = new Hashtable<>();
    private String facetCollection;

    // Expand facet results
    private String expandFacet = "";

    // This translates the form object into GET request parameters
    public String getQueryParams() {

        StringBuffer queryParams = new StringBuffer();
        if (keyword!=null && !keyword.isEmpty()) {
            queryParams.append("keyword=" + keyword +"&");
        }
        if (fullText!=null && !fullText.isEmpty()) {
            queryParams.append("fullText=" + fullText + "&");
        }
        if (excludeText!=null && !excludeText.isEmpty()) {
            queryParams.append("excludeText=" + excludeText + "&");
        }
        if (textJoin!=null && !textJoin.isEmpty()) {
            queryParams.append("textJoin=" + textJoin + "&");
        }
        if (shelfLocator!=null && !shelfLocator.isEmpty()) {
            queryParams.append("shelfLocator=" + shelfLocator + "&");
        }
        if (fileID!=null && !fileID.isEmpty()) {
            queryParams.append("fileID=" + fileID + "&");
        }
        if (title!=null && !title.isEmpty()) {
            queryParams.append("title=" + title + "&");
        }
        if (author!=null && !author.isEmpty()) {
            queryParams.append("author=" + author + "&");
        }
        if (subject!=null && !subject.isEmpty()) {
            queryParams.append("subject=" + subject + "&");
        }
        if (language!=null && !language.isEmpty()) {
            queryParams.append("language=" + language + "&");
        }
        if (place!=null && !place.isEmpty()) {
            queryParams.append("place=" + place + "&");
        }
        if (location!=null && !location.isEmpty()) {
            queryParams.append("location=" + location + "&");
        }
        if (yearStart!=null) {
            queryParams.append("yearStart=" + yearStart.toString() + "&");
        }
        if (yearEnd!=null) {
            queryParams.append("yearEnd=" + yearEnd.toString() + "&");
        }
        if (expandFacet!=null && !expandFacet.isEmpty()) {
            queryParams.append("expandFacet=" + expandFacet + "&");
        }
        if (facetCollection!=null && !facetCollection.isEmpty()) {
            queryParams.append("facetCollection=" + facetCollection + "&");
        }

        // Facets
        if (!facets.isEmpty()) {
            queryParams.append("facets=" + getFacetsAsString() + "&");
        }

        if (queryParams.toString().endsWith("&")) {
            queryParams.deleteCharAt(queryParams.toString().length() - 1);
        }

        return queryParams.toString();
    }

    public boolean hasQueryParams() {
        return (this.getKeyword() != "" || this.hasAdvancedParams());
    }

    public boolean hasAdvancedParams() {
        return (this.getAuthor() != "" || this.getFacetCollection() != null || this.getFullText() != "" || this.getLanguage() != "" || this.getLocation() != "" || this.getPlace() != "" || this.getShelfLocator() != "" || this.getSubjects() != "" || this.getTitle() != "" || this.getYearEnd() != null || this.getYearStart() != null);
    }

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

    public String getSubjects() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

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
    /* We are assuming the facets are specified in the format key:value||key:value */
    public void setFacets(String facetString) {
        Map<String,String> facets = new Hashtable<>();
        String[] pairs = facetString.split("\\|\\|");
        for (String pair : pairs) {
            String[] keyValue = pair.split("::");
            facets.put(keyValue[0],keyValue[1]);
        }
        this.facets = facets;
    }

    public Map<String, String> getFacets() {
        return this.facets;
    }

    public String getFacetsAsString() {
        StringBuilder facets = new StringBuilder();
        for (String key : this.facets.keySet()) {
            String value = this.facets.get(key);
            if (value != null && !value.isEmpty()) {
                facets.append(key + "::" + this.facets.get(key) + "||");
            }
        }
        if (facets.toString().endsWith("||")) {
            facets.delete(facets.length() - 2, facets.length());
        }
        return facets.toString();
    }

    // Hard coded collection facet - for advanced search.
    public String getFacetCollection() {
        return facetCollection;
    }

    public void setFacetCollection(String facetCollection) {
        if (facetCollection!=null && !facetCollection.trim().isEmpty()) {
            this.facetCollection = facetCollection;
            facets.put("Collection", facetCollection);
        }
    }

    /** Expand Facet **/

    public String getExpandFacet() { return expandFacet; }

    public void setExpandFacet(String expandFacet) {
        this.expandFacet = expandFacet;
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
        this.place = input.place;
        this.location = input.location;
        this.yearStart = input.yearStart;
        this.yearEnd = input.yearEnd;
        this.facets = input.facets;

        this.expandFacet = input.expandFacet;
    }



}
