package ulcambridge.foundations.viewer.genizah;

import org.junit.Test;

public class ReferenceTest {
	
	public BibliographyEntry getEntry() {
		BibliographyEntry entry = new BibliographyEntry(0, "title");
		return entry;
	}
	
	@Test
	public void parseSingleRefTypeTest() {
		String singleRefType = "m";
		Reference ref = new Reference(singleRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}
	
	@Test
	public void parseMultipleRefTypeTest() {
		String multipleRefType = "m tx";
		Reference ref = new Reference(multipleRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}
	
	@Test
	public void parseUnkRefTypeTest() {
		String unkRefType = "a";
		Reference ref = new Reference(unkRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}

}
