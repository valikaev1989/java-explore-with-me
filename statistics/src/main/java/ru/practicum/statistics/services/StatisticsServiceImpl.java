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
import java.util.Map;

import static ru.practicum.statistics.models.dto.StatsMapper.toEndpointDto;
import static ru.practicum.statistics.models.dto.StatsMapper.toEndpointHit;
import static ru.practicum.statistics.utils.FormatDate.convertRangeEnd;
import static ru.practicum.statistics.utils.FormatDate.convertRangeStart;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Transactional
    @Override
    public EndpointDto addEndpointHit(EndpointDto endpointDto) {
        log.info("StatisticsServiceImpl.addEndpointHit start:");
        EndpointDto result = toEndpointDto(statisticsRepository.save(toEndpointHit(endpointDto)));
        log.info("StatisticsServiceImpl.addEndpointHit end: endpointDto: {}", result);
        return result;
    }

    @Override
    public List<ViewStats> getViewStats(Map<String, Object> parameters) {
        log.info("StatisticsServiceImpl.getViewStats start: parameters: {}", parameters);
        parameters.forEach((key, value) -> log.info("{}: {}", key, value));
        LocalDateTime start = convertRangeStart(parameters.get("start"));
        LocalDateTime end = convertRangeEnd(parameters.get("end"));
        List<String> uris = (List<String>) parameters.get("uris");
        Boolean unique = (Boolean) parameters.get("unique");
        List<ViewStats> viewStats;
        if (uris.isEmpty()) {
            viewStats = (unique ? statisticsRepository.getStatsUniqueByTime(start, end)
                    : statisticsRepository.getAllStatsByTime(start, end));
        } else {
            viewStats = (unique ? statisticsRepository.getStatsUniqueByTimeAndUris(start, end, uris)
                    : statisticsRepository.getStatsByTimeAndUris(start, end, uris));
        }
        log.info("StatisticsServiceImpl.addEndpointHit end: viewStatsList:");
        viewStats.forEach(viewStat -> log.info("viewStat: {}", viewStat));
        return viewStats;
    }
}