package ru.practicum.server.clientStatistics.models;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

public class StatsMapper {

    public static EndpointDto toEndpointDto(String endpoint, HttpServletRequest request) {
        return EndpointDto.builder()
                .app(endpoint)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}