package ru.practicum.statistics.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.models.dto.EndpointDto;
import ru.practicum.statistics.models.dto.ViewStats;
import ru.practicum.statistics.services.StatisticsService;

import java.util.List;

import static ru.practicum.statistics.utils.FormatDate.convertRangeEnd;
import static ru.practicum.statistics.utils.FormatDate.convertRangeStart;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public EndpointDto addEndpointHit(@RequestBody EndpointDto endpointDto) {
        log.info("addEndpointHit: endpointDto: {}", endpointDto);
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
        return statisticsService.getViewStats(convertRangeStart(start), convertRangeEnd(end), uris, unique);
    }
}