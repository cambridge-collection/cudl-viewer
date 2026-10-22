package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Data for a single facet attached to an item.
 *
 * @author jennie
 *
 */
public class Facet implements Comparable<Facet> {

    public static final String HIERARCHY_SEPARATOR = "::";

    private String field; // like "subject" or "date"
    private String fieldLabel; // for display - like "Subject" or "Date".
    private String band; // like "Algebra - Early works to 1800"
    private int occurrences = 1; // amount of times this facet appears in
                                // results.
    private int rank = 0; //rank of this facet within the facetgroup.
    private final List<Facet> children = new ArrayList<>();
    private boolean hasChildren;

    public Facet(String field, String band, int occurrences, int rank) {

        this.field = field;
        this.fieldLabel = field.substring(0, 1).toUpperCase() + field.substring(1);
        this.band = band;
        this.occurrences = occurrences;
        this.rank = rank;
    }

    public String getField() {
        return field;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }


    public String getBand() {
        return band;
    }

    /** The last segment of a hierarchical band ("Parent::Child" gives "Child"). */
    public String getLabel() {
        final int separator = band.lastIndexOf(HIERARCHY_SEPARATOR);
        return separator < 0 ? band : band.substring(separator + HIERARCHY_SEPARATOR.length());
    }

    public List<Facet> getChildren() {
        return children;
    }

    public void addChild(Facet child) {
        children.add(child);
    }

    /** Whether the results hold any children of this value, listed or not. */
    public boolean hasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public int compareTo(Facet o) {
        return this.band.compareTo(o.band);
    }

    public int getRank() {
        return rank;
    }

}
