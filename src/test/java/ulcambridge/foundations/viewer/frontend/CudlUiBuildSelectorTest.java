package ulcambridge.foundations.viewer.frontend;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import ulcambridge.foundations.frontend.FrontEndBuild.LinkedResource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CudlUiBuildSelectorTest {
    private static final String DEVELOPMENT_OVERRIDE_PROPERTY =
            "cudl.ui.dev.override";

    @Test
    void usesConfiguredDevelopmentModeWhenOverrideIsAbsent() {
        List<URI> resources = getStandardPageResources(
                null, "cudl.ui.dev=true");

        assertResourcesUseBase(resources, "http://localhost:8080/");
    }

    @Test
    void ignoresDevelopmentOverrideFromSpringEnvironment() {
        List<URI> resources = getStandardPageResources(
                null, "cudl.ui.dev=false", "cudl.ui.dev.override=true");

        assertResourcesUseBase(resources, "/ui/");
    }

    @Test
    void developmentOverrideWinsOverConfiguredFalseValue() {
        List<URI> resources = getStandardPageResources(
                "true", "cudl.ui.dev=false");

        assertResourcesUseBase(resources, "http://localhost:8080/");
    }

    @Test
    void falseDevelopmentOverrideWinsOverConfiguredTrueValue() {
        List<URI> resources = getStandardPageResources(
                "false", "cudl.ui.dev=true");

        assertResourcesUseBase(resources, "/ui/");
    }

    @Test
    void invalidDevelopmentOverrideUsesConfiguredValue() {
        List<URI> resources = getStandardPageResources(
                "enabled", "cudl.ui.dev=true");

        assertResourcesUseBase(resources, "http://localhost:8080/");
    }

    @Test
    void nonLowercaseDevelopmentOverrideIsInvalid() {
        List<URI> resources = getStandardPageResources(
                "TRUE", "cudl.ui.dev=false");

        assertResourcesUseBase(resources, "/ui/");
    }

    @Test
    void invalidDevelopmentOverrideDefaultsToFalse() {
        List<URI> resources = getStandardPageResources(
                "enabled");

        assertResourcesUseBase(resources, "/ui/");
    }

    private void assertResourcesUseBase(List<URI> resources, String base) {
        assertFalse(resources.isEmpty());
        assertTrue(resources.stream()
                .allMatch(uri -> uri.toString().startsWith(base)));
    }

    private List<URI> getStandardPageResources(
            String developmentOverride, String... properties) {

        String originalDevelopmentOverride =
                System.getProperty(DEVELOPMENT_OVERRIDE_PROPERTY);
        try {
            if(developmentOverride == null) {
                System.clearProperty(DEVELOPMENT_OVERRIDE_PROPERTY);
            }
            else {
                System.setProperty(
                        DEVELOPMENT_OVERRIDE_PROPERTY, developmentOverride);
            }

            try (AnnotationConfigApplicationContext context =
                         new AnnotationConfigApplicationContext()) {
                TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                        context, properties);
                context.register(CudlUiBuildSelector.class);
                context.refresh();

                return context.getBean(BuildFactory.class)
                        .getBuild(PageType.STANDARD)
                        .resources()
                        .stream()
                        .map(resource -> ((LinkedResource) resource).getUri())
                        .collect(Collectors.toList());
            }
        }
        finally {
            if(originalDevelopmentOverride == null) {
                System.clearProperty(DEVELOPMENT_OVERRIDE_PROPERTY);
            }
            else {
                System.setProperty(
                        DEVELOPMENT_OVERRIDE_PROPERTY,
                        originalDevelopmentOverride);
            }
        }
    }
}
