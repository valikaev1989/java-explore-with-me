package ru.practicum.statistics.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistics.models.EndpointHit;
import ru.practicum.statistics.models.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.statistics.models.dto.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "and e.uri in ?3 " +
            "group by e.uri, e.app")
    List<ViewStats> getStatsByTimeAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.statistics.models.dto.ViewStats(e.app, e.uri, count(e.ip))" +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "group by e.uri, e.app")
    List<ViewStats> getAllStatsByTime(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statistics.models.dto.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "and e.uri in ?3 " +
            "group by e.uri, e.app")
    List<ViewStats> getStatsUniqueByTimeAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.statistics.models.dto.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "group by e.uri, e.app")
    List<ViewStats> getStatsUniqueByTime(LocalDateTime start, LocalDateTime end);
}