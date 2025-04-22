package de.beg.gbtec.emails.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static LocalDateTime getCurrentDateTimeUTC() {
        return ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime();
    }

}