package ulcambridge.foundations.viewer.genizah;

import java.util.ArrayList;
import java.util.List;

public class AbstractReference {
	
	private final List<RefType> referenceTypes;
	
	public AbstractReference(String typeString) {
		this.referenceTypes = new ArrayList<RefType>();
		parseReferenceTypeString(typeString);
	}
	
	public AbstractReference(List<RefType> referenceTypes) {
		this.referenceTypes = referenceTypes;
	}
	
	public void parseReferenceTypeString(String typeString) {
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

}
