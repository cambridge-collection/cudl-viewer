package ulcambridge.foundations.viewer.forms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test
 */
public class FeedbackFormTest{
    /**
     * Tests the FeedbackForm object
     */
    @Test
    public void testFeedbackForm() {

        FeedbackForm f = new FeedbackForm();
        f.setComment("comment");
        f.setEmail("email");
        f.setName("name");

        assertEquals("comment", f.getComment());
        assertEquals("email", f.getEmail());
        assertEquals("name", f.getName());
    }
}
