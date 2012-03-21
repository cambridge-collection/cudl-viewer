package ulcambridge.foundations.viewer.search;


/**
 * Data for a single facet attached to an item.
 * 
 * @author jennie
 * 
 */
public class Facet implements Comparable<Facet> {

	private String field; // like "subject" or "date"
	private String band; // like "Algebra - Early works to 1800"
	private int occurences = 1; // amount of times this facet appears in
								// results.

	public Facet(String field, String band) {

		this.field = field;
		this.band = band;
	}

	public Facet(String field, String band, int occurances) {

		this.field = field;
		this.band = band;
		this.occurences = occurances;
	}

	public String getField() {
		return field;
	}

	public String getBand() {
		return band;
	}

	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}

	public int getOccurences() {
		return occurences;
	}

	@Override
	public int compareTo(Facet o) {
		return this.band.compareTo(o.band);
	}

}
