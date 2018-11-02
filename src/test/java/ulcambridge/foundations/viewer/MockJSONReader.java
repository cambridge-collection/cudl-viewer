package ulcambridge.foundations.viewer;

import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

/**
 * Instead of connecting to a URL for the JSON data, just returns set Object.
 *
 * @author jmh205
 *
 */
public class MockJSONReader extends JSONReader {

    @Override
    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        String path = "cudl-data/MS-ADD-04004.json";
        try (InputStreamReader in = new InputStreamReader(new ClassPathResource(path).getInputStream())) {
            return new JSONObject(new JSONTokener(in));
        }
    }

    public boolean urlExists(String urlString) {
        return true;
    }
}
