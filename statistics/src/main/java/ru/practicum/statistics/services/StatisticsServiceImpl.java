package ru.practicum.statistics.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;
import ru.practicum.statistics.repositories.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        log.info("addEndpointHit start: endpointDto: {}", endpointDto);
        EndpointDto result = toEndpointDto(statisticsRepository.save(toEndpointHit(endpointDto)));
        log.info("addEndpointHit end: endpointDto: {}", result);
        return result;
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("StatisticsServiceImpl.getViewStats: start: {}, end: {},uris: {}, unique: {}",
                start, end, uris, unique);
        List<ViewStats> viewStats = new ArrayList<>();
        if (uris.isEmpty() & unique) {
            viewStats = statisticsRepository.getStatsUniqueByTime(start, end);
            log.info("statisticsRepository.getStatsUniqueByTime(start1, end1), viewStats:");
            viewStats.forEach(viewStat -> log.info("viewStat: {}", viewStat));
        }
        if (uris.isEmpty() & !unique) {
            viewStats = statisticsRepository.getAllStatsByTime(start, end);
            log.info("statisticsRepository.getAllStatsByTime(start1, end1), viewStats:");
            viewStats.forEach(viewStat -> log.info("viewStat: {}", viewStat));
        }
        if (!uris.isEmpty() & unique) {
            viewStats = statisticsRepository.getStatsUniqueByTimeAndUris(start, end, uris);
            log.info("statisticsRepository.getStatsUniqueByTimeAndUris(start1, end1, uris), viewStats:");
            viewStats.forEach(viewStat -> log.info("viewStat: {}", viewStat));
        }
        if (!uris.isEmpty() & !unique) {
            viewStats = statisticsRepository.getStatsByTimeAndUris(start, end, uris);
            log.info("statisticsRepository.getStatsByTimeAndUris(start1, end1, uris), viewStats:");
            viewStats.forEach(viewStat -> log.info("viewStat: {}", viewStat));
        }
//        if (uris.isEmpty()) {
//            viewStats = (unique ? statisticsRepository.getStatsUniqueByTime(start1, end1)
//                    : statisticsRepository.getAllStatsByTime(start1, end1));
//        } else {
//            viewStats = (unique ? statisticsRepository.getStatsUniqueByTimeAndUris(start1, end1, uris)
//                    : statisticsRepository.getStatsByTimeAndUris(start1, end1, uris));
//        }
        log.info("StatisticsServiceImpl.getViewStats end: viewStatsList: {}", viewStats);
        return viewStats;
    }
}