package ulcambridge.foundations.viewer.search;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ulcambridge.foundations.viewer.ItemFactory;
import ulcambridge.foundations.viewer.JSONReader;

/**
 * Unit test
 * 
 */
public class SearchControllerTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SearchControllerTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SearchControllerTest.class);
	}

	/**
	 * Tests the SearchController object
	 */
	public void testSearchController() throws Throwable {

		JSONReader reader = new MockJSONReader();
		junitx.util.PrivateAccessor.setField(ItemFactory.class, "reader",
				reader);

		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addParameter("keyword", "Elementary Mathematics");
		req.addParameter("facet-collection", "Newton Papers");
		req.addParameter("facet-subject", "Algebra - Early works to 1800");

		MockHttpServletResponse res = new MockHttpServletResponse();

		SearchController c = new SearchController(new MockSearch());
		ModelAndView m = null;
		try {
			m = c.processSearch(req, res);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SearchQuery q = (SearchQuery) m.getModelMap().get("query");
		SearchResultSet r = (SearchResultSet) m.getModelMap().get("results");

		// Expect one result
		assertEquals(r.getNumberOfResults(), 1);
		assertEquals(r.getResults().size(), 1);
		assertEquals(r.getSpellingSuggestedTerm(), "spellingSuggestedTerm");
		assertEquals(r.getFacets().size(), 3);
		assertEquals(r.getQueryTime(), 2.3f);
		assertEquals(r.getError(), "error");
		assertEquals(q.getKeyword(), "Elementary Mathematics");
		assertEquals(q.getFacets().size(), 2);
		assertEquals(
				q.getURLParameters(),
				"keyword=Elementary+Mathematics&amp;facet-subject=Algebra+-+Early+works+to+1800&amp;facet-collection=Newton+Papers");
		assertEquals(
				q.getURLParametersWithExtraFacet("bob", "bobvalue"),
				"keyword=Elementary+Mathematics&amp;facet-subject=Algebra+-+Early+works+to+1800&amp;facet-collection=Newton+Papers&amp;facet-bob=bobvalue");
		assertEquals(q.getURLParametersWithoutFacet("subject"),
				"keyword=Elementary+Mathematics&amp;facet-collection=Newton+Papers");

	}

	/**
	 * Always returns the same result set to any query. Uses Results.xml instead
	 * of connecting to a real search service.
	 * 
	 * @author jennie
	 * 
	 */
	private class MockSearch implements Search {

		@Override
		public SearchResultSet makeSearch(SearchQuery searchQuery) {

			// Read document from File
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Element dom = null;
			try {

				DocumentBuilder db = dbf.newDocumentBuilder();

				dom = (Element) db.parse("src/test/resources/Results.xml")
						.getDocumentElement();
				;

			} catch (Exception e) {
				e.printStackTrace();

			}

			// Get first result
			NodeList docHits = dom.getElementsByTagName("docHit");
			Node node = docHits.item(0);

			SearchResult result = new SearchResult(node);
			ArrayList<SearchResult> results = new ArrayList<SearchResult>();
			results.add(result);

			// Build facet list
			Facet f = new Facet("field", "band");
			Facet f2 = new Facet("field", "band2", 10);

			ArrayList<Facet> facets = new ArrayList<Facet>();
			facets.add(f);
			facets.add(f2);

			FacetGroup g = new FacetGroup("field", "fieldLabel", facets);

			ArrayList<FacetGroup> facetGroups = new ArrayList<FacetGroup>();
			facetGroups.add(g);

			return new SearchResultSet(1, "spellingSuggestedTerm", 2.3f,
					results, facetGroups, "error");
		}

	}

	/**
	 * Instead of connecting to a URL for the JSON data, just returns set
	 * Object.
	 * 
	 * @author jennie
	 * 
	 */
	private class MockJSONReader extends JSONReader {

		@Override
		public JSONObject readJsonFromUrl(String url) throws IOException,
				JSONException {

			String jsonText = "{\"descriptiveMetadata\":[{\"ID\":\"DMD1\",\"title\":\"Legal document: ketubba\",\"uniformTitle\":\"\",\"alternativeTitles\":[\"\"], \"names\":[{\"fullForm\":\"Nāsiyya bat Moses ha-Kohen b. Aaron ha-Kohen\",\"displayForm\":\"Nāsiyya bat Moses ha-Kohen b. Aaron ha-Kohen\",\"authority\":\"\",\"authorityURI\":\"\",\"valueURI\":\"\",\"type\":\"personal\",\"role\":\"oth\"},{\"fullForm\":\"Taylor, Charles, 1840-1908\",\"displayForm\":\"Charles Taylor\",\"authority\":\"viaf\",\"authorityURI\":\"http://viaf.org\",\"valueURI\":\"http://viaf.org/viaf/51729236\",\"type\":\"personal\",\"role\":\"dnr\"}],\"abstract\":\"<p>Part of a <em>ketubba</em> for Nāsiyya bat Moses ha-Kohen b. Aaron ha-Kohen (bride) and David <em>ha-Nasi</em> b. Daniel <em>ha-Nasi Roš ha-Yešiva Geʾon Yaʿaqov</em> (groom). Dated 23rd Ševaṭ 1393 (= 1082 CE), probably from Cairo. The dowry is over 1100 gold dinars. As the bride is from a prominent Karaite family, and the groom is a high-ranking Rabbanite, there are special clauses in the contract stating that the groom will not force the bride to compromise her Karaite principles, and the bride will join her husband in observing the Rabbanite feasts. Attested by the Bet Din and witnessed by Yequtiʾel b. Moses, Nissim b. Maḥbūb, Solomon b. Isaac, Ezekiel ha-Kohen <em>he-Ḥaver</em> b. ʿEli <em>he-Ḥaver</em>, Hodaya b. Josiah, Joseph b. Samuel, Joseph b. Elʿazar, Ṣedaqa b. Muvḥar, Aaron b. Abraham, Nathaniel b. Yefet he-Ḥaver, Aaron the Cantor b. Abraham, Hillel the Cantor b. [...], Abraham b. Isaac.</p>\",\"subject\":[\"Cairo Genizah\"],\"publisher\":[\"\"],\"originPlace\":[\"Cairo\"],\"mediaUrl\":\"\",\"thumbnailUrl\":\"/content/images/MS-TS-00024-00001-000-00001_files/8/0_0.jpg\",\"thumbnailOrientation\":\"landscape\",\"dateCreatedStart\":\"1082-01-01\",\"dateCreatedEnd\":\"1082-12-31\",\"dateCreatedDisplay\":\"23rd Ševaṭ 1393 (= 1082 C.E.)\",\"dateIssuedStart\":\"\",\"dateIssuedEnd\":\"\",\"dateIssuedDisplay\":\"\",\"languageCodes\":[\"heb\",\"arc\",\"jrb\"],\"notes\":[\"Interlinear corrections are noted in the postscript to the deed\"],\"ownership\":[\"Donated by Dr Solomon Schechter and his patron Dr Charles Taylor in 1898 as part of the Taylor-Schechter Genizah Collection\"],\"binding\":[\"\"],\"support\":[\"Vellum\"],\"script\":[\"\"],\"decoration\":[\"\"],\"layout\":[\"43.5 x 50; 18 lines (recto; verso is blank)\"],\"funding\":[\"The digitisation of the Taylor-Schechter Cairo Genizah Collection has been sponsored by the <a href='http://www.jewishmanuscripts.org/' target='_blank' class='externalLink'>Jewish Manuscript Preservation Society</a>, the <a href='http://www.genizah.org/' target='_blank' class='externalLink'>Friedberg Genizah Project Inc.</a>, and the <a href='http://www.ahrc.ac.uk/Pages/default.aspx' target='_blank' class='externalLink'>Arts and Humanities Research Council, UK</a>.\"],\"languageString\":[\"Hebrew; Aramaic; Judaeo-Arabic\"],\"physicalLocation\":\"Cambridge University Library\",\"shelfLocator\":\"T-S 24.1\",\"displayImageRights\":\"Zooming image © Cambridge University Library, All rights reserved.\",\"downloadImageRights\":\"This image may be used in accord with fair use and fair dealing provisions, including teaching and research. If you wish to reproduce it within publications or on the public web, please contact <a href='mailto:genizah@lib.cam.ac.uk'>genizah@lib.cam.ac.uk</a>.\",\"metadataRights\":\"This metadata is licensed under a Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.\",\"type\":\"text\",\"manuscript\":\"yes\",\"virtualCollections\":[\"\"],\"extent\":\"1 leaf\"}], \"organisationCollection\":\"genizah\", \"numberOfPages\":\"2\", \"useTranscriptions\":\"N\", \"pages\":[{\"name\":\"1r\",\"physID\":\"PHYS-1\",\"displayImageURL\":\"/content/images/MS-TS-00024-00001-000-00001.dzi\",\"downloadImageURL\":\"/content/images/MS-TS-00024-00001-000-00001.jpg\",\"transcriptionNormalisedURL\":\"\",\"transcriptionDiplomaticURL\":\"\",\"pageType\":\"text\"},{\"name\":\"1v\",\"physID\":\"PHYS-2\",\"displayImageURL\":\"/content/images/MS-TS-00024-00001-000-00002.dzi\",\"downloadImageURL\":\"/content/images/MS-TS-00024-00001-000-00002.jpg\",\"transcriptionNormalisedURL\":\"\",\"transcriptionDiplomaticURL\":\"\",\"pageType\":\"text\"}], \"logicalStructures\":[ {\"label\":\"Genizah Manuscript\",\"descriptiveMetadataID\":\"DMD1\",\"startPage\":\"1r\",\"startPageID\":\"LOGICAL-PAGE-1\",\"startPagePosition\":\"1\",\"children\":[]}] }";
			JSONObject json = new JSONObject(jsonText);
			return json;
		}

	}

}
