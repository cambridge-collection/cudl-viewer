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
	private String placePublished;		// CY column
	private String date;				// DA column
	private String doi;					// DO column
	private String edition;				// ET column
	private String number;				// M1 column
	private String numberVols;			// NV column
	private String originalPublisher;	// OP column
	private String publisher;			// PB column
	private String year;				// PY column
	private String researchNotes;		// RN column
	private String reprintEdition;		// RP column
	private String isbn;				// SN column
	private String startPage;			// SP column
	private String shortTitle;			// ST column
	private String svCol;				// SV column    XXX ???
	private String journal;				// T2 column    XXX ???
	private String title;				// TI column
	private String translatedTitle;		// TT column
	private String type;				// TY column	XXX : could be an enum?
	private String volume;				// VL column
	
	private List<String> authors;
	
	private List<String> editors;		// a special type of author

	public BibliographyEntry(int id, String title) {
		this.id = id;
		this.title = title;
		this.authors = new ArrayList<String>();
		this.editors = new ArrayList<String>();
	}
	
	public boolean equals(Object other) {
		if (other != null && other instanceof BibliographyEntry) {
			BibliographyEntry o = (BibliographyEntry) other;
			return this.id == o.id;	// fairly crude equality measure!
		}
		return false;
	}
	
	/**
	 * Convert an ID (like 'CY', 'DA', etc) into a readable name.
	 * 
	 * @param twoLetterID
	 * @return
	 */
	public static String getName(String twoLetterID) {
		RisTag tag = RisTag.valueOf(RisTag.class, twoLetterID);
		if (tag != null) {
			return tag.getReadableForm();
		} else {
			return "Unknown";
		}
	}
	
	public String[] getFieldNames() {
		int numberOfNames = RisTag.values().length;
		String[] tagNames = new String[numberOfNames];
		for (int index = 0; index < numberOfNames; index++) {
			tagNames[index] = RisTag.values()[index].getReadableForm();
		}
		return tagNames;
	}
	
	public String getValue(String twoLetterID) {
		RisTag tag = RisTag.valueOf(RisTag.class, twoLetterID);
		if (tag == null) return "?";
		switch (tag) {
			case CY : return getPlacePublished();
			case DA : return getDate();
			case DO : return getDoi();
			case ET : return getEdition();
			case M1 : return getNumber();
			case NV : return getNumberVols();
			case OP : return getOriginalPublisher();
			case PB : return getPublisher();
			case PY : return getYear();
			case RN : return getResearchNotes();
			case RP : return getReprintEdition();
			case SN : return getIsbn();
			case SP : return getStartPage();
			case ST : return getShortTitle();
			case SV : return getSvCol();
			case T2 : return getJournal();
			case TI : return getTitle();
			case TT : return getTranslatedTitle();
			case TY : return getType();
			case VL : return getVolume();
			default : return "Unknown";
		}
	}
	
	public List<String> getAuthors() {
		return authors;
	}
	
	public void addAuthor(String author) {
		this.authors.add(author);
	}
	
	public List<String> getEditors() {
		return editors;
	}

	public void addEditor(String editor) {
		this.editors.add(editor);
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

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}
	
	public String getStartPage() {
		return this.startPage;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlacePublished() {
		return placePublished;
	}

	public void setPlacePublished(String placePublished) {
		this.placePublished = placePublished;
	}

	public String getNumberVols() {
		return numberVols;
	}

	public void setNumberVols(String numberVols) {
		this.numberVols = numberVols;
	}

	public String getOriginalPublisher() {
		return originalPublisher;
	}

	public void setOriginalPublisher(String originalPublisher) {
		this.originalPublisher = originalPublisher;
	}

	public String getResearchNotes() {
		return researchNotes;
	}

	public void setResearchNotes(String researchNotes) {
		this.researchNotes = researchNotes;
	}

	public String getReprintEdition() {
		return reprintEdition;
	}

	public void setReprintEdition(String reprintEdition) {
		this.reprintEdition = reprintEdition;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getSvCol() {
		return svCol;
	}

	public void setSvCol(String svCol) {
		this.svCol = svCol;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getTranslatedTitle() {
		return translatedTitle;
	}

	public void setTranslatedTitle(String translatedTitle) {
		this.translatedTitle = translatedTitle;
	}

}
