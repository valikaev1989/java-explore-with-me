package ru.practicum.statistics.services;

import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    EndpointDto addEndpointHit(EndpointDto endpointDto);

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}