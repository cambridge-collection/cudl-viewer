package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Lei
 *
 */
public class GsonFactory {

    private GsonFactory() {
    }

    public static Gson create() {
        return new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateAdapter()).setPrettyPrinting().create();
    }

}
