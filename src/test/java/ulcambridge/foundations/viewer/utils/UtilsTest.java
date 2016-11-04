package ulcambridge.foundations.viewer.utils;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by philip on 04/11/16.
 *
 * Unit test for Utils class.
 */
public class UtilsTest{

    @Test
    public void testIsSimpleIntFormat(){
        assertTrue(Utils.isSimpleIntFormat("-123"));
        assertTrue(Utils.isSimpleIntFormat("-0"));

        assertTrue(Utils.isSimpleIntFormat("123"));
        assertTrue(Utils.isSimpleIntFormat("0"));

        assertTrue(Utils.isSimpleIntFormat("+123"));
        assertTrue(Utils.isSimpleIntFormat("+0"));

        assertFalse(Utils.isSimpleIntFormat("72 72"));
        assertFalse(Utils.isSimpleIntFormat("bob"));
        assertFalse(Utils.isSimpleIntFormat("0.123"));
    }


}
