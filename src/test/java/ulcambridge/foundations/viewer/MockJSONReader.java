package ulcambridge.foundations.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Instead of connecting to a URL for the JSON data, just returns set Object.
 *
 * @author jennie
 *
 */
public class MockJSONReader extends JSONReader {

    @Override
    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {

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
