package ru.practicum.statistics.models.dto;

import ru.practicum.statistics.models.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.statistics.utils.FormatDate.FORMATTER;

public class StatsMapper {
    public static EndpointHit toEndpointHit(EndpointDto endpointDto) {
        return EndpointHit.builder()
                .app(endpointDto.getApp())
                .ip(endpointDto.getIp())
                .uri(endpointDto.getUri())
                .timestamp(LocalDateTime.parse(endpointDto.getTimestamp(), FORMATTER))
                .build();
    }

    public static EndpointDto toEndpointDto(EndpointHit endpointHit) {
        return EndpointDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp().format(FORMATTER))
                .build();
    }
}