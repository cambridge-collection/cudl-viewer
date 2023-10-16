package ulcambridge.foundations.viewer.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import ulcambridge.foundations.viewer.model.UI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UIDao {

    public UI getUITheme(Path jsonFilepath) {

        // parse UI into objects
        try {
            String json = Files.readString(jsonFilepath);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, ulcambridge.foundations.viewer.model.UI.class);

        } catch (IOException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
