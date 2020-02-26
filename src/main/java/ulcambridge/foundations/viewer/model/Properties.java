package ulcambridge.foundations.viewer.model;

import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * @deprecated See the deprecation notice on {@link #getString(String)}.
 */
@Deprecated
public class Properties {

    private static final List<String> RESOURCE_BUNDLE_NAMES = ImmutableList.of(
        // The test defaults are highest priority, so that unit tests can rely
        // on their values. However at runtime the test properties won't be
        // available, so cudl-global.properties will be the primary source.
        "ulcambridge.foundations.viewer.test-defaults",
        "cudl-global",
        "collections",
        "application"
    );

    private static final List<ResourceBundle> RESOURCE_BUNDLES = RESOURCE_BUNDLE_NAMES.stream()
        .map(Properties::getOptionalResourceBundle)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(ImmutableList.toImmutableList());

    private static Optional<ResourceBundle> getOptionalResourceBundle(String baseName) {

        try {
            return Optional.of(ResourceBundle.getBundle(baseName));
        }
        catch (MissingResourceException e) {
            return Optional.empty();
        }
    }

    /**
     * Searches various properties files for a key and returns the value of the first occurrence.
     *
     * @return A string or null if a value is not defined.
     *
     * @deprecated Spring's dependency injection features should be used to inject configuration into beans, instead of
     * having objects configure themselves by directly accessing property files. Direct use of property files makes
     * testing hard, because dependencies are hidden in class internals, rather than constructor arguments.
     */
    @Deprecated
    public static String getString(String key) {
        return RESOURCE_BUNDLES.stream()
            .map(bundle -> bundle.containsKey(key) ? bundle.getString(key) : null)
            .filter(Objects::nonNull).findFirst().orElse(null);
    }

}
