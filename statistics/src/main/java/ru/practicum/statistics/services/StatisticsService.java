package ru.practicum.statistics.services;

import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    EndpointDto addEndpointHit(EndpointDto endpointDto);

    List<ViewStats> getViewStats(Map<String, Object> parameters);
}