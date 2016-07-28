package ulcambridge.foundations.viewer.search;

import java.util.List;

/**
 * Holds information for an individual search result. Item id can be used to
 * pull back more information on an item from the ItemFactory.
 *
 * @author jennie
 *
 */
public class SearchResult implements Comparable<SearchResult> {

    private String title;
    private String type;
    private String fileId;
    private int startPage;
    private String startPageLabel;
    private String thumbnailURL;
    private String thumbnailOrientation;
    private List<String> snippets;
    private int score; // how relevant is this result, used for ordering.

    // DocHits for all the matches found for this item.
    // private List<DocHit> docHits = new ArrayList<DocHit>();

    public SearchResult(String title, String fileId, int startPage,
            String startPageLabel, List<String> snippets,
            int score, String type, String thumbnailURL, String thumbnailOrientation) {

        this.title = title;
        this.fileId = fileId;
        this.startPage = startPage;
        this.startPageLabel = startPageLabel;
        this.snippets = snippets;
        this.score = score;
        this.type = type;
        this.thumbnailURL = thumbnailURL;
        this.thumbnailOrientation = thumbnailOrientation;
    }

    public String getTitle() {
        return this.title;
    }


    /**
     * Highest score should appear first.
     */
    public int compareTo(SearchResult o) {

        return ((SearchResult) o).score - this.score;

    }

    public String getFileId() {
        return fileId;
    }

    public int getStartPage() {
        return startPage;
    }

    public String getStartPageLabel() {
        return startPageLabel;
    }

    public List<String> getSnippets() {
        return snippets;
    }

    public String getType() {
        return type;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getThumbnailOrientation() {
        return thumbnailOrientation;
    }

    /**
     * Search Results are considered the same if they have the same ID. So are
     * about the same book. There should be only one SearchResult object per
     * item (book).
     */
    /*
     * @Override public boolean equals(Object o) { if (o instanceof
     * SearchResult) { SearchResult r = (SearchResult) o; return this.id ==
     * r.id; } return false; }
     *
     * @Override public int hashCode(Object o) { if (o instanceof SearchResult)
     * { SearchResult r = (SearchResult) o; return this.id == r.id; } return
     * false; }
     */

}

