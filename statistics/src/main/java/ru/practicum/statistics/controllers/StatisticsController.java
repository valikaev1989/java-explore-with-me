package ru.practicum.statistics.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;
import ru.practicum.statistics.services.StatisticsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public EndpointDto addEndpointHit(@RequestBody EndpointDto endpointDto) {
        log.info("StatisticsController.addEndpointHit: endpointDto: {}", endpointDto);
        return statisticsService.addEndpointHit(endpointDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "uris", required = false, defaultValue = "List.of()") List<String> uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("StatisticsController.getViewStats:start: {}, end: {},uris: {}, unique: {}",
                start, end, uris, unique);
//        Map<String, Object> parameters = Map.of(
//                "start", URLDecoder.decode(start, StandardCharsets.UTF_8),
//                "end", URLDecoder.decode(end, StandardCharsets.UTF_8),
//                "uris", uris,
//                "unique", unique
//        );
//        parameters.forEach((key, value) -> log.info("{}: {}", key, value));
        return statisticsService.getViewStats(start, end, uris, unique);
    }
}