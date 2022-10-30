package ru.practicum.statistics.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDate {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final LocalDateTime START = LocalDateTime.now().minusYears(100);
    public static final LocalDateTime END = LocalDateTime.now();

    public static LocalDateTime convertRangeStart(Object object) {
        if (object == null) {
            return START;
        } else {
            return LocalDateTime.parse((String) object, FORMATTER);
        }
    }

    public static LocalDateTime convertRangeEnd(Object object) {
        if (object == null) {
            return END;
        } else {
            return LocalDateTime.parse((String) object, FORMATTER);
        }
    }
}