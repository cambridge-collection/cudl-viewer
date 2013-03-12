package ulcambridge.foundations.viewer.genizah;

import java.util.ArrayList;
import java.util.List;

/**
 * Model object representing the data in the Bibliograph table.
 * 
 * @author gilleain
 *
 */
public class BibliographyEntry {
	
	private int id;
	private String date;		// DA column
	private String doi;			// DO column
	private String edition;		// ET column
	private String number;		// M1 column
	private String publisher;	// PB column
	private String year;		// PY column
	private String title;		// TI column
	private String volume;		// VL column
	
	private List<String> authors;

	public BibliographyEntry(int id, String title) {
		this.id = id;
		this.title = title;
		this.authors = new ArrayList<String>();
	}
	
	public boolean equals(Object other) {
		if (other != null && other instanceof BibliographyEntry) {
			BibliographyEntry o = (BibliographyEntry) other;
			return this.id == o.id;	// fairly crude equality measure!
		}
		return false;
	}
	
	public List<String> getAuthors() {
		return authors;
	}
	
	public void addAuthor(String author) {
		this.authors.add(author);
	}
	
	public int getId() {
		return id;
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

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public String getVolume() {
		return volume;
	}
}
