package ulcambridge.foundations.viewer.crowdsourcing.model;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import ulcambridge.foundations.viewer.utils.Utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * @author Lei
 * 
 */
public class GsonDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(Utils.formatDate(src));
	}

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return Utils.parseDate(json.getAsString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
