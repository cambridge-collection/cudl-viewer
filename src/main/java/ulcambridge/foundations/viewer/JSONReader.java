package ulcambridge.foundations.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JSONReader {

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException,
            JSONException {
        InputStream is = new URL(url).openStream();
        try {
            is = new URL(url).openStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        return null;
    }

    /**
     * Checks to see if the specified URl exists
     *
     * @param urlString
     * @return
     */
    public boolean urlExists(String urlString) {

        try {
            URL url = new URL(urlString);

            if (((HttpURLConnection) url.openConnection()).getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            /* do nothing */
        }
        return false;

    }
}
