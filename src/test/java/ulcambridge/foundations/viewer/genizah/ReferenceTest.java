package ulcambridge.foundations.viewer.genizah;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class ReferenceTest extends TestCase {
	
	public ReferenceTest(String testName) {
		super(testName);
	}
	
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ReferenceTest.class);
	}
	
	public BibliographyEntry getEntry() {
		BibliographyEntry entry = new BibliographyEntry(0, "title");
		return entry;
	}
	
	public void parseSingleRefTypeTest() {
		String singleRefType = "m";
		FragmentReferences ref = new FragmentReferences(singleRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}
	
	public void parseMultipleRefTypeTest() {
		String multipleRefType = "m tx";
		FragmentReferences ref = new FragmentReferences(multipleRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}
	
	public void parseUnkRefTypeTest() {
		String unkRefType = "a";
		FragmentReferences ref = new FragmentReferences(unkRefType, getEntry());
		System.out.println(ref.getTypeReadableForm());
	}

}
