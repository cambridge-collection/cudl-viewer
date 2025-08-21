package ulcambridge.foundations.viewer.forms;

import ulcambridge.foundations.viewer.model.Collection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
  //  private String facetCollection;

    // Expand facet results
    private String expandFacet = "";

    // This translates the form object into GET request parameters
    public String getQueryParams() {

        StringBuffer queryParams = new StringBuffer();
        if (keyword!=null && !keyword.isEmpty()) {
            queryParams.append("keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8) +"&");
        }
        if (fullText!=null && !fullText.isEmpty()) {
            queryParams.append("fullText=" + URLEncoder.encode(fullText, StandardCharsets.UTF_8)  + "&");
        }
        if (excludeText!=null && !excludeText.isEmpty()) {
            queryParams.append("excludeText=" + URLEncoder.encode(excludeText, StandardCharsets.UTF_8)  + "&");
        }
        if (textJoin!=null && !textJoin.isEmpty()) {
            queryParams.append("textJoin=" + URLEncoder.encode(textJoin, StandardCharsets.UTF_8)  + "&");
        }
        if (shelfLocator!=null && !shelfLocator.isEmpty()) {
            queryParams.append("shelfLocator=" + URLEncoder.encode(shelfLocator, StandardCharsets.UTF_8)  + "&");
        }
        if (fileID!=null && !fileID.isEmpty()) {
            queryParams.append("fileID=" + URLEncoder.encode(fileID, StandardCharsets.UTF_8)  + "&");
        }
        if (title!=null && !title.isEmpty()) {
            queryParams.append("title=" + URLEncoder.encode(title, StandardCharsets.UTF_8)  + "&");
        }
        if (author!=null && !author.isEmpty()) {
            queryParams.append("author=" + URLEncoder.encode(author, StandardCharsets.UTF_8)  + "&");
        }
        if (subject!=null && !subject.isEmpty()) {
            queryParams.append("subject=" + URLEncoder.encode(subject, StandardCharsets.UTF_8)  + "&");
        }
        if (language!=null && !language.isEmpty()) {
            queryParams.append("language=" + URLEncoder.encode(language, StandardCharsets.UTF_8)  + "&");
        }
        if (place!=null && !place.isEmpty()) {
            queryParams.append("place=" + URLEncoder.encode(place, StandardCharsets.UTF_8)  + "&");
        }
        if (location!=null && !location.isEmpty()) {
            queryParams.append("location=" + URLEncoder.encode(location, StandardCharsets.UTF_8)  + "&");
        }
        if (yearStart!=null) {
            queryParams.append("yearStart=" + URLEncoder.encode(yearStart.toString(), StandardCharsets.UTF_8)  + "&");
        }
        if (yearEnd!=null) {
            queryParams.append("yearEnd=" + URLEncoder.encode(yearEnd.toString(), StandardCharsets.UTF_8)  + "&");
        }
        if (expandFacet!=null && !expandFacet.isEmpty()) {
            queryParams.append("expandFacet=" + URLEncoder.encode(expandFacet, StandardCharsets.UTF_8)  + "&");
        }
        if (facets.containsKey("Collection") && !facets.get("Collection").isEmpty()) {
            queryParams.append("facetCollection=" + URLEncoder.encode(facets.get("Collection"), StandardCharsets.UTF_8) + "&");
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
        return (this.getAuthor() != "" || this.getFacetCollection() != null || this.getFullText() != "" || this.getLanguage() != "" || this.getLocation() != "" || this.getPlace() != "" || this.getShelfLocator() != "" || this.getSubject() != "" || this.getTitle() != "" || this.getYearEnd() != null || this.getYearStart() != null);
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

    public String getSubject() {
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
        return facets.get("Collection");
    }

    public void setFacetCollection(String facetCollection) {
        if (facetCollection!=null && !facetCollection.trim().isEmpty()) {
            //this.facetCollection = facetCollection;
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
