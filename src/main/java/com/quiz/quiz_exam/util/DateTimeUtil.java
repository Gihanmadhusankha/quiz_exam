package com.quiz.quiz_exam.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateTimeUtil {

    public static LocalDateTime toUtc(LocalDateTime localDateTime, String zone) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.of(zone));
        return zdt.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime fromUtc(LocalDateTime utcDateTime, String zone) {
        ZonedDateTime zdt = utcDateTime.atZone(ZoneOffset.UTC);
        return zdt.withZoneSameInstant(ZoneId.of(zone)).toLocalDateTime();
    }
}
