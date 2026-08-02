package ulcambridge.foundations.viewer.tags;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Unit tests for the EL functions with logic of their own. */
public class ElFunctionsTest {

    @Test
    public void capitalise_upperCasesTheFirstLetterOnly() {
        assertEquals("Draft", ElFunctions.capitalise("draft"));
        assertEquals("Released", ElFunctions.capitalise("released"));
        assertEquals("In progress", ElFunctions.capitalise("in progress"));
        assertEquals("Draft", ElFunctions.capitalise("Draft"));
    }

    @Test
    public void capitalise_returnsEmptyForNothingToDisplay() {
        assertEquals("", ElFunctions.capitalise(null));
        assertEquals("", ElFunctions.capitalise(""));
    }
}
