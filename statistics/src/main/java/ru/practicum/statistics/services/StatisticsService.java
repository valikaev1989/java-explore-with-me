package ru.practicum.statistics.services;

import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;

import java.util.List;

public interface StatisticsService {
    EndpointDto addEndpointHit(EndpointDto endpointDto);

    List<ViewStats> getViewStats(String start, String end,List<String> uris, Boolean unique);
}