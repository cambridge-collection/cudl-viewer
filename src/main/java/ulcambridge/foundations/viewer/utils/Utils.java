package ulcambridge.foundations.viewer.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Utils {

	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss z";
	private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
	private static final Locale LOCALE = Locale.UK;

	private static final ThreadLocal<DateFormat> DATE_FORMATS =
			new ThreadLocal<DateFormat>();

	/**
	 * @return a DateFormat instance safe for the current thread to use.
	 */
	private static final DateFormat getDateFormat() {
		DateFormat f = DATE_FORMATS.get();

		if(f == null) {
			f = new SimpleDateFormat(DATE_PATTERN, LOCALE);
			DATE_FORMATS.set(f);
		}

		// It's critical that we reset the state of the date format objects as
		// they change their calendar's timezone in response to parse()
		// operations. Fantastic API design. Should probably just use joda time
		// to avoid this bs.
		return resetDateFormat(f);
	}

	/**
	 * Reset state of a DateFormat which may have been changed by parse
	 * operations.
     */
	private static DateFormat resetDateFormat(DateFormat f) {
		f.setTimeZone(TIME_ZONE);
		return f;
	}

	public static final String formatDate(Date date) {
		return getDateFormat().format(date);
	}

	public static Date parseDate(String date) throws ParseException {
		return getDateFormat().parse(date);
	}

	// get current date time
	public static Date getCurrentDateTime() {
		return new Date();
	}

	// format a double to 5 digit precision
	public static String formatValue(double value) {
		value = (double) Math.round(value * 100000) / 100000;
		return new DecimalFormat("#0.00000").format(value);
	}

}
