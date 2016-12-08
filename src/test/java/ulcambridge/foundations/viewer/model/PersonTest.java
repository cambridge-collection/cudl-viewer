package ulcambridge.foundations.viewer.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for testing individual people objects
 */
public class PersonTest {

    @Test
    public void testPerson() {

        final Person a = new Person("Test Person, 2012", "Test Person", "test authority ID",
                "test authority", "test value uri", "test type", "aut");

        final Person b = new Person("ZZZ Person, 2012", "Test Person", "test authority ID",
                "test authority", "test value uri", "test type", "aut");

        assertEquals("test authority", a.getAuthority());
        assertEquals("test authority ID", a.getAuthorityURI());
        assertEquals("Test Person", a.getDisplayForm());
        assertEquals("Test Person, 2012", a.getFullForm());
        assertEquals("aut", a.getRole());
        assertEquals("test type", a.getType());
        assertEquals("test value uri", a.getValueURI());
        assertEquals("Test Person", a.toString());
        assertTrue(a.compareTo(b)<0);
        assertTrue(b.compareTo(a)>0);
        assertFalse(a.equals(b));
        assertTrue(a.equals(a));
    }
}
