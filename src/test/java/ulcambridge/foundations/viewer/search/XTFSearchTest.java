//package ulcambridge.foundations.viewer.search;
//
//import static com.google.common.truth.Truth.assertThat;
//import static org.junit.Assert.assertEquals;
//import static ulcambridge.foundations.viewer.testing.XMLTesting.getTestXml;
//
//import com.google.common.collect.ImmutableList;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import ulcambridge.foundations.viewer.forms.SearchForm;
//import ulcambridge.foundations.viewer.testing.XMLTesting;
//
//public class XTFSearchTest {
//    @Test
//    public void testSearchController() {
//
//        HashMap<String, String> facetMap = new HashMap<String, String>();
//        facetMap.put("subject", "test subject");
//        facetMap.put("date", "test date");
//        facetMap.put("collection", "test collection");
//        facetMap.put("language", "test language");
//        facetMap.put("place", "test place");
//        facetMap.put("location", "test location");
//
//        SearchForm form = new SearchForm();
//        form.setKeyword("keyword");
//        form.setFacetSubject("test subject");
//        form.setFacetDate("test date");
//        form.setFacetCollection("test collection");
//        form.setFacetLanguage("test language");
//        form.setFacetPlace("test place");
//        form.setFacetLocation("test location");
//
//        SolrSearch s = new TestableSolrSearch();
//        SearchResultSet r = s.makeSearch(form);
//
//        // Expect one result
//        assertEquals(1, r.getNumberOfResults());
//        assertEquals(1, r.getResults().size());
//        assertEquals("", r.getSpellingSuggestedTerm());
//        assertEquals("", r.getError());
//        assertEquals("MS-ADD-04004", r.getResults().get(0).getFileId());
//        assertEquals(1, r.getResults().get(0).getSnippets().size());
//    }
//
//    /**
//     * Always returns the same document as a result to any query. Uses
//     * Results.xml instead of connecting to a real XTF instance.
//     *
//     * @author jennie
//     *
//     */
//    private class TestableSolrSearch extends SolrSearch {
//
//        public TestableSolrSearch() {
//            super(URI.create("http://xtf.example.com"));
//        }
//
//        @Override
//        protected Document getDocument(String url) {
//
//            // Read document from URL and put results in Document.
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            try {
//                DocumentBuilder db = dbf.newDocumentBuilder();
//                return db.parse("src/test/resources/Results.xml");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    @Test
//    public void createSearchResultParsesValidDocHit() {
//        Element docHit = (Element)getTestXml(this, "docHit.xml")
//            .getElementsByTagName("docHit").item(0);
//
//        SearchResult result = new SolrSearch(URI.create("http://example")).createSearchResult(docHit);
//
//        assertThat(XMLTesting.normaliseSpace(result.getTitle())).isEqualTo(
//            "Letter from <b>Joseph</b> <b>Dalton</b> <b>Hooker</b> to C. R. Darwin [c. April 1851]");
//        assertThat(result.getType()).isEqualTo("example-type");
//        assertThat(result.getFileId()).isEqualTo("MS-DAR-00100-00164");
//        assertThat(result.getStartPage()).isEqualTo(1);
//        assertThat(result.getStartPageLabel()).isEqualTo("164r");
//        assertThat(result.getSnippets().stream()
//            .map(XMLTesting::normaliseSpace)
//            .collect(Collectors.toList())
//        ).isEqualTo(ImmutableList.of(
//            "Letter from <b>Joseph</b> <b>Dalton</b> <b>Hooker</b> to C. R. Darwin [c. April",
//            "<b>Hooker</b> , <b>Joseph</b> <b>Dalton</b>"
//        ));
//    }
//
//    @ParameterizedTest
//    @MethodSource("emptyDocHits")
//    public void createSearchResultHandlesMissingResultData(Element docHitWithMissingData, SearchResult expected) throws ParserConfigurationException {
//        SolrSearch xtf = new SolrSearch(URI.create("http://example/"));
//        assertThat(xtf.createSearchResult(docHitWithMissingData)).isEqualTo(expected);
//    }
//
//    private static Stream<Arguments> emptyDocHits() {
//        Document doc = getTestXml(XTFSearchTest.class, "docHitsWithMissingData.xml");
//        Element docHit1 = (Element)doc.getElementsByTagName("docHit").item(0);
//        Element docHit2 = (Element)doc.getElementsByTagName("docHit").item(1);
//        Element docHit3 = (Element)doc.getElementsByTagName("docHit").item(2);
//        SearchResult empty = new SearchResult("", "", 1, "", ImmutableList.of(), -1, "bookormanuscript", null, null);
//        return Stream.of(
//            Arguments.of(docHit1, empty),
//            Arguments.of(docHit2, empty),
//            Arguments.of(docHit3, empty)
//        );
//    }
//}
