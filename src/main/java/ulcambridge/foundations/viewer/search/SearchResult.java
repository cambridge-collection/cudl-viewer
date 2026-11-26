package ulcambridge.foundations.viewer.search;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import ulcambridge.foundations.viewer.model.Item;

/**
 * Holds information for an individual search result. Item id can be used to
 * pull back more information on an item from the ItemFactory.
 *
 * @author jennie
 *
 */
public class SearchResult implements Comparable<SearchResult> {

    private final String title;
    private final String type;
    private final String fileId;
    private final int startPage;
    private final String startPageLabel;
    private final String thumbnailURL;
    private final String thumbnailOrientation;
    private final String shelfLocator;
    private final String abstractShort;
    private final String mainDisplay;
    private final boolean released;
    private final String itemStatus;
    // Places shown, each [west, south, east, north], or null if the page has none
    private final List<List<Double>> coverage;
    // Places of origin, each [lat, lon], or null if the page has none
    private final List<List<Double>> origin;

    private final List<String> snippets;
    private final int score; // how relevant is this result, used for ordering.

    // DocHits for all the matches found for this item.
    // private List<DocHit> docHits = new ArrayList<DocHit>();

    public SearchResult(String title, String fileId, int startPage,
            String startPageLabel, Iterable<String> snippets,
            int score, String type, @Nullable String thumbnailURL, @Nullable String thumbnailOrientation,
            @Nullable String shelfLocator, @Nullable String abstractShort, @Nullable String mainDisplay,
            boolean released, String itemStatus) {
        this(title, fileId, startPage, startPageLabel, snippets, score, type,
            thumbnailURL, thumbnailOrientation, shelfLocator, abstractShort,
            mainDisplay, released, itemStatus, null, null);
    }

    public SearchResult(String title, String fileId, int startPage,
            String startPageLabel, Iterable<String> snippets,
            int score, String type, @Nullable String thumbnailURL, @Nullable String thumbnailOrientation,
            @Nullable String shelfLocator, @Nullable String abstractShort, @Nullable String mainDisplay,
            boolean released, String itemStatus, @Nullable List<List<Double>> coverage,
            @Nullable List<List<Double>> origin) {

        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(fileId);
        Preconditions.checkNotNull(startPageLabel);
        Preconditions.checkNotNull(snippets);
        Preconditions.checkNotNull(type);

        this.title = title;
        this.fileId = fileId;
        this.startPage = startPage;
        this.startPageLabel = startPageLabel;
        this.snippets = ImmutableList.copyOf(snippets);
        this.snippets.forEach(Preconditions::checkNotNull);
        this.score = score;
        this.type = type;
        this.thumbnailURL = thumbnailURL;
        this.thumbnailOrientation = thumbnailOrientation;
        this.shelfLocator = shelfLocator;
        this.abstractShort = abstractShort;
        this.mainDisplay = mainDisplay;
        this.released = released;
        this.itemStatus = (itemStatus == null || itemStatus.isBlank())
            ? Item.DEFAULT_STATUS : itemStatus;
        this.coverage = copyPlaces(coverage, 4, "coverage must be [west, south, east, north]");
        this.origin = copyPlaces(origin, 2, "origin must be [lat, lon]");
    }

    @Nullable
    private static List<List<Double>> copyPlaces(@Nullable List<List<Double>> places, int size,
                                                 String message) {
        if (places == null) {
            return null;
        }
        places.forEach((place) -> Preconditions.checkArgument(place.size() == size, message));
        return places.stream().map(ImmutableList::copyOf).collect(ImmutableList.toImmutableList());
    }

    public String getTitle() {
        return this.title;
    }

    public String getShelfLocator() {
        return shelfLocator;
    }

    public String getAbstractShort() {
        return abstractShort;
    }

    public String getMainDisplay() {
        return mainDisplay;
    }

    public boolean isReleased() {
        return released;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    @Nullable
    public List<List<Double>> getCoverage() {
        return coverage;
    }

    @Nullable
    public List<List<Double>> getOrigin() {
        return origin;
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

    /**
     * The fully-resolved thumbnail URL for the matched page, built from the Solr
     * {@code IIIFImageURL} field.
     */
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getThumbnailOrientation() {
        return thumbnailOrientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchResult that = (SearchResult) o;
        return startPage == that.startPage && score == that.score && released == that.released
            && title.equals(that.title)
            && type
            .equals(that.type) && fileId.equals(that.fileId) && startPageLabel
            .equals(that.startPageLabel)
            && Objects.equals(thumbnailURL, that.thumbnailURL) && Objects
            .equals(thumbnailOrientation, that.thumbnailOrientation)
            && Objects.equals(shelfLocator, that.shelfLocator)
            && Objects.equals(abstractShort, that.abstractShort)
            && Objects.equals(mainDisplay, that.mainDisplay)
            && Objects.equals(itemStatus, that.itemStatus)
            && Objects.equals(coverage, that.coverage)
            && Objects.equals(origin, that.origin) && snippets
            .equals(that.snippets);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(title, type, fileId, startPage, startPageLabel, thumbnailURL,
                thumbnailOrientation, shelfLocator, abstractShort, mainDisplay, released,
                itemStatus, coverage, origin, snippets, score);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("title", title)
            .add("type", type)
            .add("fileId", fileId)
            .add("startPage", startPage)
            .add("startPageLabel", startPageLabel)
            .add("thumbnailURL", thumbnailURL)
            .add("thumbnailOrientation", thumbnailOrientation)
            .add("shelfLocator", shelfLocator)
            .add("abstractShort", abstractShort)
            .add("mainDisplay", mainDisplay)
            .add("released", released)
            .add("itemStatus", itemStatus)
            .add("coverage", coverage)
            .add("origin", origin)
            .add("snippets", snippets)
            .add("score", score)
            .toString();
    }
}

