package ulcambridge.foundations.viewer;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import ulcambridge.foundations.viewer.testing.BaseCUDLApplicationContextTest;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextHierarchy({
    @ContextConfiguration(name = "child", classes = {ErrorHandlingControllerAdviceTest.Config.class})
})
public class ErrorHandlingControllerAdviceTest
    extends BaseCUDLApplicationContextTest
{
    public static class Config {
        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    public static final class Entity {
        @Pattern(regexp = "[a-z]+")
        public String example;

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }
    }

    @RequestMapping("/error-handling-test")
    @Validated
    public static class TestController {
        @RequestMapping("/{example}")
        @ResponseBody
        public String handle(@Valid Entity entity) {
            return entity.example;
        }
    }

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAdviceBeanIsRegisteredInDispatchServletContext() {
        assertThat(wac.getBean(ErrorHandlingControllerAdvice.class)).isNotNull();
        assertThrows(NoSuchBeanDefinitionException.class, () -> wac.getParent().getBean(ErrorHandlingControllerAdvice.class));
    }

    @Test
    public void testControllerWorksWithValidInput() throws Exception {
        mockMvc.perform(get("/error-handling-test/foo"))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("foo")));
    }

    @Test
    public void invalidInputResultsInBadRequest() throws Exception {
        mockMvc.perform(get("/error-handling-test/123"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.violations", hasSize(1)))
            .andExpect(jsonPath("$.violations[0].fieldName", equalTo("example")))
            .andExpect(jsonPath("$.violations[0].message", equalTo("must match \"[a-z]+\"")));
    }
}
