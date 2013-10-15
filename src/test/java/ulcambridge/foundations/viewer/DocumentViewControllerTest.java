package ulcambridge.foundations.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import ulcambridge.foundations.viewer.dao.CollectionsDao;
import ulcambridge.foundations.viewer.dao.CollectionsMockDao;
import ulcambridge.foundations.viewer.dao.ItemsJSONDao;

/**
 * Unit test for simple App.
 */
public class DocumentViewControllerTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public DocumentViewControllerTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(DocumentViewControllerTest.class);
	}

	/**
	 * test the class DocumentViewController
	 */
	public void testDocumentViewController() throws Throwable {
		
		JSONReader reader = new MockJSONReader();
		
		ItemsJSONDao jsondao = new ItemsJSONDao();
		jsondao.setJSONReader(reader);
		
		CollectionsDao collectionsdao = new CollectionsMockDao();
				
		ItemFactory itemFactory = new ItemFactory();
		itemFactory.setItemsDao(jsondao);		
		
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setRequestURI("/view/MS-ADD-04004");
		req.setProtocol("http");
		req.setServerName("testurl.testingisthebest.com");
		req.setServerPort(8080);

		DocumentViewController c = new DocumentViewController();	
		
		// inject the mock dao 
		CollectionFactory collectionFactory = new CollectionFactory();
		collectionFactory.setCollectionsDao(collectionsdao);
		
		// inject the factories
		c.setCollectionFactory(collectionFactory);	
		c.setItemFactory(itemFactory);
		
		ModelAndView mDoc = c.handleRequest("MS-ADD-04004", req);	

		assertEquals("MS-ADD-04004", mDoc.getModelMap().get("docId"));
		assertEquals(0, mDoc.getModelMap().get("page"));
		assertEquals("http://testurl.testingisthebest.com:8080/view/MS-ADD-04004", mDoc.getModelMap().get("docURL"));
		
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

			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream file = loader.getResourceAsStream("MS-ADD-04004.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
			JSONObject json = new JSONObject(builder.toString());
			
			return json;
		}
		public boolean urlExists(String urlString) {

			return true;

		}
	}	
}
