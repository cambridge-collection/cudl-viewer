package ulcambridge.foundations.viewer.dao;

import org.json.JSONObject;

public interface JSONLoader {
    /**
     * Read and parse a JSON document.
     *
     * @param id The identifier of the document to load.
     * @return The parsed document.
     * @throws org.springframework.dao.DataAccessException
     */
    JSONObject loadJSON(String id);
}
