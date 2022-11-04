package ru.practicum.statistics.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDate {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final LocalDateTime START = LocalDateTime.now().minusYears(100);
    public static final LocalDateTime END = LocalDateTime.now();

    public static LocalDateTime convertRangeStart(String start) {
        if (start == null) {
            return START;
        } else {
            return LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMATTER);
        }
    }

    public static LocalDateTime convertRangeEnd(String end) {
        if (end == null) {
            return END;
        } else {
            return LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMATTER);
        }
    }
}