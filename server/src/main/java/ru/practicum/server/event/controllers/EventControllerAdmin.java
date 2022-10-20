package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.services.EventService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping()
    List<EventFullDto> getEventsByFilter(@RequestParam(required = false) List<Long> userIds,
                                         @RequestParam(required = false) List<EventState> eventStates,
                                         @RequestParam(required = false) List<Long> categoryIds,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(required = false, defaultValue = "10") @Min(0) Integer size) {
        Map<String, Object> filter = Map.of(
                "users", userIds,
                "states", eventStates,
                "categories", categoryIds,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size);
        log.info("EventControllerAdmin.getEventsByFilter:filter:");
        filter.forEach((key, value) -> log.info("{}:{}", key, value));
        return eventService.getEventByFilter(filter);
    }

    @PutMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable(value = "eventId") @Min(0) Long eventId,
                             @RequestBody EventInputDto eventInputDto) {
        log.info("EventControllerAdmin.updateEvent: eventId{}, eventInputDto{}", eventId, eventInputDto);
        return eventService.updateEvent(eventId, eventInputDto);
    }

    @PatchMapping("/{eventId}/publish")
    EventFullDto publishEvent(@PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("EventControllerAdmin.publishEvent: eventId:{}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    EventFullDto rejectEvent(@PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("EventControllerAdmin.rejectEvent: eventId:{}", eventId);
        return eventService.rejectEvent(eventId);
    }
}