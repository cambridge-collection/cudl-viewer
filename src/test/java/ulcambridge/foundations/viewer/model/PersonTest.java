package ulcambridge.foundations.viewer.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ulcambridge.foundations.viewer.model.Person;

/**
 * Unit test for testing individual people objects
 */
public class PersonTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PersonTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PersonTest.class );
    }

    /**
     * Tests the Person object
     */
    public void testPerson()
    {

    	Person a = new Person("Test Person, 2012", "Test Person", "test authority ID",
    			"test authority", "test value uri", "test type", "aut");
   
    	Person b = new Person("ZZZ Person, 2012", "Test Person", "test authority ID",
    			"test authority", "test value uri", "test type", "aut");
    	
        assertEquals (a.getAuthority(),"test authority");
        assertEquals (a.getAuthorityURI(),"test authority ID");
        assertEquals (a.getDisplayForm(),"Test Person");
        assertEquals (a.getFullForm(),"Test Person, 2012");
        assertEquals (a.getRole(),"aut");
        assertEquals (a.getType(),"test type");
        assertEquals (a.getValueURI(),"test value uri");
        assertEquals (a.toString(),"Test Person");
        assertEquals(a.compareTo(b)<0, true);
        assertEquals(b.compareTo(a)>0, true);
        assertEquals(a.equals(b), false);
        assertEquals(a.equals(a),true);
    }
}
