package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.services.EventService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> getEventsByFilter(
            @RequestParam(value = "users", required = false, defaultValue = "List.of()") List<Long> userIds,
            @RequestParam(value = "states", required = false, defaultValue = "List.of()") List<String> states,
            @RequestParam(value = "categories", required = false, defaultValue = "List.of()") List<Long> categoryIds,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        Map<String, Object> filter = Map.of(
                "users", userIds,
                "states", states,
                "categories", categoryIds,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size);
        log.info("EventControllerAdmin.getEventsByFilter:filter:");
        filter.forEach((key, value) -> log.info("{}:{}", key, value));
        return eventService.getEventsByFilterForAdmin(filter);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventFromAdmin(@PathVariable(value = "eventId") @Min(0) Long eventId,
                                             @RequestBody EventInputDto eventInputDto) {
        log.info("EventControllerAdmin.updateEvent: eventId{}, eventInputDto{}", eventId, eventInputDto);
        return eventService.updateEventFromAdmin(eventId, eventInputDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("EventControllerAdmin.publishEvent: eventId:{}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("EventControllerAdmin.rejectEvent: eventId:{}", eventId);
        return eventService.rejectEvent(eventId);
    }
}