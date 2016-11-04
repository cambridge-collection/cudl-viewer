package ulcambridge.foundations.viewer.forms;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test
 */
public class MailingListFormTest{
    /**
     * Tests the MailingListForm object
     */
    @Test
    public void testMailingListForm() {

        MailingListForm f = new MailingListForm();

        f.setEmail("email");
        f.setName("name");

        assertEquals("email", f.getEmail());
        assertEquals("name", f.getName());
    }
}
