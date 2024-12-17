package org.finra.rmcs.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateTimeUtil {
    public static String convertToEST(ZonedDateTime zonedDateTime) {
        if (Objects.isNull(zonedDateTime)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return zonedDateTime
                .withZoneSameInstant(ZoneId.of("America/New_York"))
                .toLocalDateTime()
                .format(formatter);
    }

    public static LocalDateTime convertToUtc(LocalDateTime dateTime) {
        if (Objects.isNull(dateTime)) {
            return null;
        }
        ZonedDateTime dateTimeInMyZone = ZonedDateTime.of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

}
