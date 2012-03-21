package ulcambridge.foundations.viewer.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Holds a set of search facets for one field, e.g. date.
 * Holds a number of bands within that (1800s,1900s,etc)
 * 
 * @author jennie
 * 
 */
public class FacetGroup {

	private List<Facet> facets = new ArrayList<Facet>();;
	private String field;

	/**
	 * Facets are sorted after the group is created and on adding new 
	 * facets. 
	 * 
	 * @param field
	 * @param facets
	 */
	public FacetGroup(String field, List<Facet> facets) {
		
		this.facets = facets;
		this.field = field;
		Collections.sort(facets);
	}

	public String getField() {
		return field;
	}

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
	
	public Facet getFacetWithField(String field) {	
		Iterator<Facet> facetIt = this.facets.iterator();
		while (facetIt.hasNext()){
			Facet facet = facetIt.next();
			if (field!=null && field.equals(facet.getField())) {
				return facet;
			}
		}
		return null;
	}	
	
	/**
	 * Adds a new facet (band) if one does not already exist in this group
	 * else adds one to the occurrences of that facet in this group. 
	 * 
	 * @param band
	 */
	public void add(Facet newFacet) throws Exception {
		
		if (!newFacet.getField().equals(this.field)) {
			throw new Exception ("Can't add facet, it has a different field to this facet group"); 
		}
		
		Facet facet = getFacetWithBand(newFacet.getBand());
		if (facet!=null) {
			// add one to count
			facet.setOccurences(facet.getOccurences()+1);			
			
		} else {
			// add new band
			facets.add(newFacet);
			
		}
		Collections.sort(facets);

	}

}
