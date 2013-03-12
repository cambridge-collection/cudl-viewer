package ulcambridge.foundations.viewer.genizah;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference to a fragment in a bibliography entry.
 * 
 * @author gilleain
 *
 */
public class Reference {
	
	public enum RefType {
		m("mention"),
		y("partial text"),
		x("full text"),
		yt("partial text with translation"),
		xt("full text with translation"),
		ty("partial translation, no text"),
		tx("full translation, no text"),
		u("unknown");
		
		private String readableForm;
		
		private RefType(String readableForm) {
			this.readableForm = readableForm;
		}
		
		public String getReadableForm() {
			return readableForm;
		}
	}
	
	private final List<RefType> referenceTypes;
	
	private final BibliographyEntry entry;
	
	public Reference(String typeString, BibliographyEntry entry) {
		this.entry = entry;
		this.referenceTypes = new ArrayList<RefType>();
		if (typeString.contains(" ")) {	// multiple types
			String[] parts = typeString.split(" ");
			for (String part : parts) {
				addRefType(part);
			}
		} else {
			addRefType(typeString);
		}
	}
	
	private void addRefType(String typeString) {
		// XXX this swallows errors, and converts them to the 'u' (unknown) type
		try {
			referenceTypes.add(RefType.valueOf(typeString));
		} catch (IllegalArgumentException iae) {
			referenceTypes.add(RefType.u);
		}
	}

	public String getTypeReadableForm() {
		String readableForm = "";
		int index = 0;
		for (RefType refType : referenceTypes) {
			readableForm += refType.getReadableForm();
			if (index < referenceTypes.size() - 1) {
				readableForm += "|";
			}
			index++;
		}
		return readableForm;
	}
	
	public List<RefType> getTypeList() {
		return referenceTypes;
	}

	public BibliographyEntry getEntry() {
		return entry;
	}
	
}
