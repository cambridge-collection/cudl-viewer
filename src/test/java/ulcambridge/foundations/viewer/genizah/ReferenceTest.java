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
	
	public void testParseSingleRefTypeTest() {
		String singleRefType = "m";
		String refPos = "150";
		FragmentReferences ref = new FragmentReferences(singleRefType, refPos, getEntry());
		assertEquals("mention", ref.getTypeReadableForm());
	}
	
	public void testParseMultipleRefTypeTest() {
		String multipleRefType = "m tx";
		String refPos = "150";
		FragmentReferences ref = new FragmentReferences(multipleRefType, refPos, getEntry());
		assertEquals("mention|full translation, no text", ref.getTypeReadableForm());
	}
	
	public void testParseUnkRefTypeTest() {
		String unkRefType = "a";
		String refPos = "150";
		FragmentReferences ref = new FragmentReferences(unkRefType, refPos, getEntry());
		assertEquals("unknown", ref.getTypeReadableForm());
	}

}
