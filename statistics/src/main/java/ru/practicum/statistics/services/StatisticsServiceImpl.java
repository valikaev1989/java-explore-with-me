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
@Transactional
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    public EndpointDto addEndpointHit(EndpointDto endpointDto) {
        log.info("StatisticsServiceImpl.addEndpointHit start:");
        EndpointDto result = toEndpointDto(statisticsRepository.save(toEndpointHit(endpointDto)));
        log.info("StatisticsServiceImpl.addEndpointHit end: endpointDto: {}", result);
        return result;
    }

    @Override
    public List<ViewStats> getViewStats(String start, String end,List<String> uris, Boolean unique) {
        log.info("StatisticsServiceImpl.getViewStats: start: {}, end: {},uris: {}, unique: {}",
                start, end, uris, unique);
        LocalDateTime start1 = convertRangeStart(start);
        LocalDateTime end1 = convertRangeEnd(end);
        List<ViewStats> viewStats;
        if (uris.isEmpty()) {
            viewStats = (unique ? statisticsRepository.getStatsUniqueByTime(start1, end1)
                    : statisticsRepository.getAllStatsByTime(start1, end1));
        } else {
            viewStats = (unique ? statisticsRepository.getStatsUniqueByTimeAndUris(start1, end1, uris)
                    : statisticsRepository.getStatsByTimeAndUris(start1, end1, uris));
        }
        log.info("StatisticsServiceImpl.addEndpointHit end: viewStatsList:");
        viewStats.forEach(viewStat -> log.info("viewStat: {}", viewStat));
        return viewStats;
    }
}