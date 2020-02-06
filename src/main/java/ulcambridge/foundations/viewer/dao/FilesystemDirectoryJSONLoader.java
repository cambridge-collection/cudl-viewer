package ulcambridge.foundations.viewer.dao;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesystemDirectoryJSONLoader implements JSONLoader {
    private final Path jsonDirectory;

    @Autowired
    public FilesystemDirectoryJSONLoader(Path jsonDirectory) {
        Assert.notNull(jsonDirectory, "jsonDirectory is required");

        this.jsonDirectory = jsonDirectory.normalize();
    }

    @Override
    public JSONObject loadJSON(String id) {
        Path filename = Paths.get(String.format("%s.json", id));
        Path path = this.jsonDirectory.resolve(filename);

        // Ensure the id does not contain things like ../ or multiple
        // path components - it must be a file directly under the directory
        if(!this.jsonDirectory.equals(path.getParent())) {
            throw new IllegalArgumentException(
                String.format("invalid id: %s", id));
        }

        String jsonText;
        try {
            jsonText = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        }
        catch(NoSuchFileException e) {
            throw new EmptyResultDataAccessException(
                String.format("JSON for id \"%s\" does not exist", id), 1, e);
        }
        catch(IOException e) {
            throw new DataAccessResourceFailureException(
                String.format("Failed to read JSON file: %s", path), e);
        }

        JSONObject json;
        try {
            json = new JSONObject(jsonText);
        }
        catch(JSONException e) {
            throw new DataRetrievalFailureException(
                String.format("File contents is not valid JSON: %s - %s", path, e), e);
        }

        return json;
    }
}
