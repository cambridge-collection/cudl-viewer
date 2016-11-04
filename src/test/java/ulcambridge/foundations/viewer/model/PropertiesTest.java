package ulcambridge.foundations.viewer.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for testing properties object
 */
public class PropertiesTest{

    /**
     * Tests the Properties object
     */
    @Test
    public void testProperties() {
        assertEquals("UA-10976633-2", Properties.getString("GoogleAnalyticsId"));
        assertEquals("Newton Papers", Properties.getString("newton.title"));
    }
}
