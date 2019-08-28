package addr.book.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
 * This class converts the LocalDate format
 * of the dateOB to a string format.
 */

public class CalendarUtil {
	
	// Date format constant. This can be adjusted. 
	private static final String DATE_FORMAT = "MM.dd.yyyy";
	
	private static final DateTimeFormatter DATE_FORMATTER = 
			DateTimeFormatter.ofPattern(DATE_FORMAT);
	// Returns the formatted date in the DATE_FORMAT format
	public static final String applyFormat(LocalDate format) {
		if(format == null)
			return null;
		
		return DATE_FORMATTER.format(format);
	}
	
	/**
	 * Returns parsed date or null.
	 * @param dateString
	 * @return boolean
	 */
	public static LocalDate parse(String dateString) {
		try {
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		} catch (DateTimeParseException e) {
			return null;
		}
		
	}
	
	/**
	 * Checks if dateString is valid.
	 * @param dateString
	 * @return boolean
	 */
	public static boolean dateIsValid(String dateString) {
		return CalendarUtil.parse(dateString) != null;
	}
	
}
