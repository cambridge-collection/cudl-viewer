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
		FragmentReferences ref = new FragmentReferences(singleRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}
	
	@Test
	public void parseMultipleRefTypeTest() {
		String multipleRefType = "m tx";
		FragmentReferences ref = new FragmentReferences(multipleRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}
	
	@Test
	public void parseUnkRefTypeTest() {
		String unkRefType = "a";
		FragmentReferences ref = new FragmentReferences(unkRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}

}
