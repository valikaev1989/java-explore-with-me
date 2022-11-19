package ru.practicum.statistics.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;
import ru.practicum.statistics.repositories.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.statistics.models.dto.StatsMapper.toEndpointDto;
import static ru.practicum.statistics.models.dto.StatsMapper.toEndpointHit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    public EndpointDto addEndpointHit(EndpointDto endpointDto) {
        log.info("addEndpointHit start: endpointDto: {}", endpointDto);
        EndpointDto result = toEndpointDto(statisticsRepository.save(toEndpointHit(endpointDto)));
        log.info("addEndpointHit end: endpointDto: {}", result);
        return result;
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("StatisticsServiceImpl.getViewStats: start: {}, end: {},uris: {}, unique: {}",
                start, end, uris, unique);
        List<ViewStats> viewStats;
        if (uris.isEmpty()) {
            viewStats = (unique ? statisticsRepository.getStatsUniqueByTime(start, end)
                    : statisticsRepository.getAllStatsByTime(start, end));
        } else {
            viewStats = (unique ? statisticsRepository.getStatsUniqueByTimeAndUris(start, end, uris)
                    : statisticsRepository.getStatsByTimeAndUris(start, end, uris));
        }
        log.info("StatisticsServiceImpl.getViewStats end: viewStatsList: {}", viewStats);
        return viewStats;
    }
}