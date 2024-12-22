package com.cwc.certificate.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
public class DateUtils {


    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, dateFormatter);
    }

    public static String convertDateFormat(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }
}
