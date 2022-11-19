package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.EventDtos.AdminParamsDto;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.services.EventService;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.server.utils.FormatDate.convertRangeEnd;
import static ru.practicum.server.utils.FormatDate.convertRangeStart;
import static ru.practicum.server.utils.FormatPage.getPage;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> getEventsByFilter(
            @RequestParam(value = "users", required = false) List<Long> userIds,
            @RequestParam(value = "states", required = false) List<String> states,
            @RequestParam(value = "categories", required = false) List<Long> categoryIds,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        log.info("getEventsByFilter: users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd:{}, from:{}, " +
                "size:{}", userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
        AdminParamsDto paramsDto = AdminParamsDto.builder()
                .userIds(userIds)
                .states(states)
                .categoryIds(categoryIds)
                .rangeStart(convertRangeStart(rangeStart))
                .rangeEnd(convertRangeEnd(rangeEnd))
                .pageable(getPage(from, size))
                .build();
        log.info("getEventsByFilter: paramsDto: {}", paramsDto);
        return eventService.getEventsByFilterForAdmin(paramsDto);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventFromAdmin(@PathVariable(value = "eventId") @Min(0) Long eventId,
                                             @RequestBody EventInputDto eventInputDto) {
        log.info("updateEvent: eventId{}, eventInputDto{}", eventId, eventInputDto);
        return eventService.updateEventFromAdmin(eventId, eventInputDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("publishEvent: eventId:{}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("rejectEvent: eventId:{}", eventId);
        return eventService.rejectEvent(eventId);
    }
}