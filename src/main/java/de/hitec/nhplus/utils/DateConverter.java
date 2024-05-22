package de.hitec.nhplus.utils;

import de.hitec.nhplus.datastorage.TreatmentDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The <code>DateConverter</code> contains the entire logic for converting LocalDate objects into Strings and the other way around.
 */
public class DateConverter {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TIME_FORMAT = "HH:mm";

    /**
     * Converts from String format to LocalDate.
     * @param date The String to be parsed.
     * @return The resulting LocalDate object.
     */
    public static LocalDate convertStringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Converts from String format to LocalTime.
     * @param time The String to be parsed.
     * @return The resulting LocalTime object.
     */
    public static LocalTime convertStringToLocalTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    /**
     * Converts from LocalDate format to String.
     * @param date The LocalDate to be parsed.
     * @return The resulting String object.
     */
    public static String convertLocalDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Converts from LocalTime format to String.
     * @param time The String to be parsed.
     * @return The resulting LocalTime object.
     */
    public static String convertLocalTimeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }
}
