package ulcambridge.foundations.viewer;

import com.google.common.collect.ImmutableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import ulcambridge.foundations.viewer.dao.CollectionsJSONDao;
import ulcambridge.foundations.viewer.model.Collection;
import ulcambridge.foundations.viewer.model.Item;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Equivalence harness for sourcing an item's collections from its JSON instead of by
 * scanning every collection's item ids. Runs the pre-change and post-change candidate
 * gathering through the *real* private {@code getBreadcrumbCollection} over every item
 * JSON on disk, and asserts the two agree.
 *
 * <p>Skips unless {@code -Dcudl.data.dir=...} points at a release tree.
 */
public class CollectionChoiceEquivalenceTest {

    private static final String DATA_DIR = System.getProperty("cudl.data.dir", "");

    /**
     * Identical element for element and in the same order, so the breadcrumb choice cannot
     * differ whatever the selection logic does.
     */
    @Test
    public void candidateListsAreIdenticalForEveryItem() throws Exception {
        Path data = dataDir();
        CollectionFactory factory = factory("true", data);
        Map<String, List<Collection>> oldCandidates = oldCandidateLists(factory);
        DocumentViewController controller = controller(factory);
        Method newGather = newGather();

        int[] compared = {0};
        List<String> differing = new ArrayList<>();
        forEachItem(data, null, (itemId, json) -> {
            compared[0]++;
            List<String> oldIds = oldCandidates.getOrDefault(itemId, List.<Collection>of())
                .stream().map(c -> c.getId()).collect(Collectors.toList());
            @SuppressWarnings("unchecked")
            List<Collection> newList =
                (List<Collection>) newGather.invoke(controller, item(itemId, json));
            List<String> newIds = newList.stream().map(c -> c.getId())
                .collect(Collectors.toList());
            if (!oldIds.equals(newIds)) {
                differing.add(String.format("%s: old=%s new=%s", itemId, oldIds, newIds));
            }
        });

        System.out.printf("IDENTICAL LISTS: %d compared, %d differing%n",
            compared[0], differing.size());
        differing.stream().limit(20).forEach(d -> System.out.println("   " + d));
        assertEquals(List.of(), differing, "candidate list differed from the item id scan");
        assertTrue(compared[0] > 100000, "expected the full release tree, got " + compared[0]);
    }

    /**
     * The invariant the controller's parent-layout filter rests on: an item in a parent
     * collection would lose it from the candidate list.
     */
    @Test
    public void parentLayoutCollectionsHoldNoItemsDirectly() throws Exception {
        CollectionFactory factory = factory("true", dataDir());
        Map<String, Integer> counts = new HashMap<>();
        for (Collection c : factory.getCollections()) {
            if ("parent".equals(c.getType()) && !c.getItemIds().isEmpty()) {
                counts.put(c.getId(), c.getItemIds().size());
            }
        }
        assertEquals(Map.of(), counts, "a parent-layout collection lists items directly");
    }

    @Test
    public void matchesWithCachingEnabledAsInProduction() throws Exception {
        Result r = compare("true", null);
        System.out.printf("EQUIVALENCE caching=true : %d compared, %d divergences%n",
            r.compared, r.divergences.size());
        r.divergences.stream().limit(20).forEach(d -> System.out.println("   " + d));
        assertEquals(List.of(), r.divergences, "collection choice diverged");
        assertTrue(r.compared > 100000, "expected the full release tree, got " + r.compared);
    }

    /**
     * The uncached branch of {@code getCollectionFromId} prunes item ids on every call,
     * which is quadratic over Genizah, so this covers only the items whose choice is
     * non-trivial: anything with more than one candidate on either side, or where the two
     * sides disagree about membership. A single shared candidate cannot select differently.
     */
    @Test
    public void matchesWithCachingDisabledAsInLocalDev() throws Exception {
        Set<String> interesting = nonTrivialItemIds();
        System.out.printf("non-trivial items: %d%n", interesting.size());
        assertTrue(interesting.size() > 2500,
            "expected the multi-collection and hierarchical items, got " + interesting.size());

        Result r = compare("false", interesting);
        System.out.printf("EQUIVALENCE caching=false: %d compared, %d divergences%n",
            r.compared, r.divergences.size());
        r.divergences.stream().limit(20).forEach(d -> System.out.println("   " + d));
        assertEquals(List.of(), r.divergences, "collection choice diverged");
    }

    private static class Result {
        int compared;
        List<String> divergences = new ArrayList<>();
    }

    private Result compare(String caching, Set<String> only) throws Exception {
        Path data = dataDir();
        CollectionFactory factory = factory(caching, data);
        DocumentViewController controller = controller(factory);
        Method newGather = newGather();
        Method breadcrumb = DocumentViewController.class
            .getDeclaredMethod("getBreadcrumbCollection", List.class);
        breadcrumb.setAccessible(true);

        Map<String, List<Collection>> oldCandidates = oldCandidateLists(factory);
        Result result = new Result();

        forEachItem(data, only, (itemId, json) -> {
            result.compared++;

            List<Collection> oldList = oldCandidates.getOrDefault(itemId, List.of());
            @SuppressWarnings("unchecked")
            List<Collection> newList =
                (List<Collection>) newGather.invoke(controller, item(itemId, json));

            Collection oldPick = (Collection) breadcrumb.invoke(controller, oldList);
            Collection newPick = (Collection) breadcrumb.invoke(controller, newList);

            if (!same(oldPick, newPick)) {
                result.divergences.add(String.format(
                    "%s: collection old=%s new=%s (old=%s new=%s)",
                    itemId, id(oldPick), id(newPick), ids(oldList), ids(newList)));
            } else if (!same(parentOf(factory, oldPick), parentOf(factory, newPick))) {
                result.divergences.add(String.format("%s: parent old=%s new=%s", itemId,
                    id(parentOf(factory, oldPick)), id(parentOf(factory, newPick))));
            }
        });
        return result;
    }

    /** Item ids whose breadcrumb choice is not forced by having a single candidate. */
    private Set<String> nonTrivialItemIds() throws Exception {
        Path data = dataDir();
        CollectionFactory factory = factory("true", data);
        Map<String, List<Collection>> oldCandidates = oldCandidateLists(factory);

        Set<String> interesting = new HashSet<>();
        forEachItem(data, null, (itemId, json) -> {
            Set<String> oldIds = oldCandidates.getOrDefault(itemId, List.<Collection>of())
                .stream().map(c -> c.getId()).collect(Collectors.toSet());
            Set<String> newIds = slugs(json).stream()
                .filter(s -> factory.getCollectionFromId(s) != null)
                .collect(Collectors.toSet());
            if (oldIds.size() > 1 || newIds.size() > 1 || !oldIds.equals(newIds)) {
                interesting.add(itemId);
            }
        });
        return interesting;
    }

    /**
     * OLD gathering, verbatim from HEAD:
     * {@code getCollections().stream().filter(c -> c.getItemIds().contains(docId))}.
     * getCollections() is already sorted by order, so building per-item lists in that
     * same iteration order yields the identical list the filter produced.
     */
    private static Map<String, List<Collection>> oldCandidateLists(CollectionFactory factory) {
        Map<String, List<Collection>> map = new HashMap<>();
        for (Collection c : factory.getCollections()) {
            for (String itemId : c.getItemIds()) {
                map.computeIfAbsent(itemId, k -> new ArrayList<>()).add(c);
            }
        }
        return map;
    }

    private static Method newGather() throws Exception {
        Method m = DocumentViewController.class
            .getDeclaredMethod("getCollections", Item.class);
        m.setAccessible(true);
        return m;
    }

    private Path dataDir() {
        Assumptions.assumeFalse(DATA_DIR.isBlank(),
            "set -Dcudl.data.dir to a release tree to run this");
        Path data = Path.of(DATA_DIR);
        Assumptions.assumeTrue(Files.isDirectory(data.resolve("collections")),
            "no release tree at " + DATA_DIR);
        return data;
    }

    private static CollectionFactory factory(String caching, Path data) throws Exception {
        CollectionsJSONDao dao = new CollectionsJSONDao(
            new File(data.resolve("cudl.dl-dataset.json").toString()),
            data.resolve("cudl.ui.json5").toString(),
            caching,
            data.resolve("unreleased").resolve("collections"));
        return new CollectionFactory(
            dao, caching, data.resolve("json"), data.resolve("unreleased").toString());
    }

    private static DocumentViewController controller(CollectionFactory factory) {
        return new DocumentViewController(
            factory, itemId -> null,
            URI.create("http://localhost:8888"),
            URI.create("http://images.example.com/iiif/"),
            Optional.of(new HashMap<>()), Optional.of(new HashMap<>()), false);
    }

    @FunctionalInterface
    private interface ItemVisitor {
        void visit(String itemId, JSONObject json) throws Exception;
    }

    /** Parses one item at a time; holding all 162k JSONObjects at once exhausts the heap. */
    private static void forEachItem(Path data, Set<String> only, ItemVisitor visitor)
        throws Exception {

        for (Path dir : List.of(data.resolve("json"),
                                data.resolve("unreleased").resolve("json"))) {
            if (!Files.isDirectory(dir)) { continue; }
            List<Path> files;
            try (Stream<Path> stream = Files.list(dir)) {
                files = stream.filter(p -> p.toString().endsWith(".json"))
                              .collect(Collectors.toList());
            }
            for (Path file : files) {
                String itemId = file.getFileName().toString().replaceAll("\\.json$", "");
                if (only != null && !only.contains(itemId)) { continue; }
                JSONObject json;
                try {
                    json = new JSONObject(Files.readString(file));
                } catch (Exception ignored) {
                    continue; // not valid JSON; the running app would fail the same way
                }
                visitor.visit(itemId, json);
            }
        }
    }

    private static Set<String> slugs(JSONObject json) {
        Set<String> out = new LinkedHashSet<>();
        JSONArray arr = json.optJSONArray("collection");
        if (arr == null) { return out; }
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) { continue; }
            String slug = o.optString("url-slug", null);
            if (slug != null && !slug.isBlank()) { out.add(slug); }
        }
        return out;
    }

    /** Replicates the controller's parentCollection derivation. */
    private static Collection parentOf(CollectionFactory factory, Collection pick) {
        if (pick == null || pick.getParentCollectionId() == null) { return null; }
        return factory.getCollectionFromId(pick.getParentCollectionId());
    }

    private static boolean same(Collection a, Collection b) {
        return a == null ? b == null : b != null && a.getId().equals(b.getId());
    }

    private static String id(Collection c) { return c == null ? "<null>" : c.getId(); }

    private static String ids(List<Collection> cs) {
        return cs.stream().map(c -> c.getId()).collect(Collectors.toList()).toString();
    }

    private static Item item(String itemId, JSONObject json) {
        return new Item(itemId, "bookormanuscript", "title", ImmutableList.of(), "", "",
            "", "", "", ImmutableList.of(), ImmutableList.of(), true, false, json);
    }
}
