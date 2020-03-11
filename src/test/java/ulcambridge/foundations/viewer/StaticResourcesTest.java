package ulcambridge.foundations.viewer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class StaticResourcesTest extends BaseCUDLApplicationContextTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void staticResourcesAreRegistered() throws Exception {
        mockMvc.perform(get("/favicon.ico"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
