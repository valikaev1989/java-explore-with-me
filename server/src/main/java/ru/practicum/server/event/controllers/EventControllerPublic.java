package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.clientStatistics.EventClient;
import ru.practicum.server.clientStatistics.models.StatsMapper;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventShortDto;
import ru.practicum.server.event.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    private final EventService eventService;
    private final EventClient eventClient;

    @GetMapping()
    public List<EventShortDto> getAllEventForPublic(
            @RequestParam(value = "text", required = false, defaultValue = "") String text,
            @RequestParam(value = "categories", required = false, defaultValue = "List.of()") List<Long> categories,
            @RequestParam(value = "paid", required = false, defaultValue = "false") Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false, defaultValue = "EVENT_DATE") String sort,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Map<String, Object> filter = Map.of(
                "text", text,
                "categories", categories,
                "paid", paid,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "onlyAvailable", onlyAvailable,
                "sort", sort,
                "from", from,
                "size", size
        );
        System.out.println("");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("EventControllerPublic.getAllEventForPublic filter:");
        filter.forEach((key, value) -> log.info("{}: {}", key, value));
        eventClient.postStats(StatsMapper.toEndpointDto("getAllEventForPublic", request));
        return eventService.getAllEventsForPublic(filter);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdForPublic(@PathVariable(value = "eventId") @Min(0) Long eventId,
                                              HttpServletRequest request) {
        System.out.println("");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        log.info("EventControllerPublic.getEventByIdForPublic: eventId:{}", eventId);
        eventClient.postStats(StatsMapper.toEndpointDto("getEventByIdForPublic", request));
        return eventService.getEventByIdForPublic(eventId);
    }
}