package ulcambridge.foundations.viewer.genizah;

/**
 * Model object representing the data in the Bibliograph table.
 * 
 * @author gilleain
 *
 */
public class BibliographyEntry {
	
	private String date;		// DA column
	private String doi;			// DO column
	private String edition;		// ET column
	private String number;		// M1 column
	private String publisher;	// PB column
	private String year;		// PY column
	private String title;		// TI column

	public BibliographyEntry(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
