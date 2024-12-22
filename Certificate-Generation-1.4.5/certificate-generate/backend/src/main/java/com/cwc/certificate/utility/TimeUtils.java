package com.cwc.certificate.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String convertToISTIn24HourFormat(String interviewDate, String startTime, String endTime, String fromTimezone) {
        ZoneId istZoneId = ZoneId.of("Asia/Kolkata");

        ZoneId fromZoneId = ZoneId.of(fromTimezone);

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String startDateTime = interviewDate + " " + startTime;
        String endDateTime = interviewDate + " " + endTime;

        LocalDateTime localStartTime = LocalDateTime.parse(startDateTime, inputFormatter);
        ZonedDateTime zonedStartTime = localStartTime.atZone(fromZoneId);
        ZonedDateTime istStartTime = zonedStartTime.withZoneSameInstant(istZoneId);

        LocalDateTime localEndTime = LocalDateTime.parse(endDateTime, inputFormatter);
        ZonedDateTime zonedEndTime = localEndTime.atZone(fromZoneId);
        ZonedDateTime istEndTime = zonedEndTime.withZoneSameInstant(istZoneId);


        String istStart = istStartTime.format(outputFormatter);
        String istEnd = istEndTime.format(outputFormatter);

        return "IST Start Time: " + istStart + ", IST End Time: " + istEnd;
    }
}

