package ulcambridge.foundations.viewer.crowdsourcing.model;

import com.fasterxml.jackson.databind.util.StdConverter;
import ulcambridge.foundations.viewer.utils.Utils;

import java.text.ParseException;
import java.util.Date;


public class JsonDateFormat {

    private JsonDateFormat() { throw new RuntimeException(); }

    public static class Serializer extends StdConverter<Date, String> {
        @Override
        public String convert(Date value) {
            return Utils.formatDate(value);
        }
    }

    public static class Deserializer extends StdConverter<String, Date> {
        @Override
        public Date convert(String value) {
            try {
                return Utils.parseDate(value);
            }
            catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format", e);
            }
        }
    }
}
