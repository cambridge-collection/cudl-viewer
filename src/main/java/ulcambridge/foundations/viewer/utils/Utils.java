package ulcambridge.foundations.viewer.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ulcambridge.foundations.viewer.model.Properties;

/**
 * 
 * @author Lei
 * 
 */
public class Utils {

	// date format
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(Properties.getString("dateformat"));

	// time zone
	private static final String timeZone = Properties.getString("timezone");

	// get current date time
	public static Date getCurrentDateTime() {
		try {
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
			return dateFormat.parse(dateFormat.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// format a double to 5 digit precision
	public static String formatValue(double value) {
		value = (double) Math.round(value * 100000) / 100000;
		return new DecimalFormat("#0.00000").format(value);
	}

}
