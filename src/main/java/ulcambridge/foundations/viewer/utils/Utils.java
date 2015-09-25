package ulcambridge.foundations.viewer.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Lei
 * 
 */
public class Utils {

	public static final String dateformat = "YYYY-MM-dd HH:mm:ss z";
	public static final String timezone = "BST";
	
	// date format
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);

	// get current date time
	public static Date getCurrentDateTime() {
		try {
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
