package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Holds a set of search facets for one field, e.g. collection.
 *
 * @author jennie
 *
 */
public class FacetGroup {

    private List<Facet> facets;
    private String field;
    private String fieldLabel;
    private int occurrences;
    private int totalGroups;

    /**
     * Facets are sorted after the group is created
     *
     * Facet groups MUST be all of the same field.
     *
     * @param field
     * @param facets
     */
    public FacetGroup(String field, List<Facet> facets, int occurrences, int totalGroups) {

        this.field = field;
        this.fieldLabel = field.substring(0, 1).toUpperCase() + field.substring(1);
        this.facets = facets;
        this.occurrences = occurrences;
        this.totalGroups = totalGroups;

    }

    public String getField() {
        return field;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getDisplayLabel() {
        return (field.substring(0, 1).toUpperCase() + field.substring(1).toLowerCase()).replace('_', ' ');
    }

    public int getOccurrences() {
        return this.occurrences;
    }

    public int getTotalGroups() { return totalGroups; }

    public int getNumBands() {
        return facets.size();
    }

    public List<String> getBands() {
        ArrayList<String> bands = new ArrayList<String>();
        Iterator<Facet> facetIt = this.facets.iterator();
        while (facetIt.hasNext()){
            Facet facet = facetIt.next();
            bands.add(facet.getBand());
        }
        return bands;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public Facet getFacetWithBand(String band) {
        Iterator<Facet> facetIt = this.facets.iterator();
        while (facetIt.hasNext()){
            Facet facet = facetIt.next();
            if (band!=null && band.equals(facet.getBand())) {
                return facet;
            }
        }
        return null;
    }

}
