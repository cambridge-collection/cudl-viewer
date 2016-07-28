package ulcambridge.foundations.viewer.search;


/**
 * Data for a single facet attached to an item.
 *
 * @author jennie
 *
 */
public class Facet implements Comparable<Facet> {

    private String field; // like "subject" or "date"
    private String fieldLabel; // for display - like "Subject" or "Date".
    private String band; // like "Algebra - Early works to 1800"
    private int occurences = 1; // amount of times this facet appears in
                                // results.
    private int rank = 0; //rank of this facet within the facetgroup.

    public Facet(String field, String band, int occurances, int rank) {

        this.field = field;
        this.fieldLabel = field.substring(0, 1).toUpperCase() + field.substring(1);
        this.band = band;
        this.occurences = occurances;
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

    public int getOccurences() {
        return occurences;
    }

    @Override
    public int compareTo(Facet o) {
        return this.band.compareTo(o.band);
    }

    public int getRank() {
        return rank;
    }

}
