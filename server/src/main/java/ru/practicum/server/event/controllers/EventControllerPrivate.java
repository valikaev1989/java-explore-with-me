package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.services.EventService;
import ru.practicum.server.utils.FormatPage;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEventsByInitiatorId(
            @PathVariable(value = "userId") @Min(0) Long userId,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        System.out.println("");
        log.info("EventControllerPrivate.getAllEventsByUserId: userId:{}, from:{}, size:{}", userId, from, size);
        return eventService.getAllEventsByInitiatorId(userId, FormatPage.getPage(from, size));
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable(value = "userId") Long userId,
                                 @RequestBody EventInputDto eventInputDto) {
        System.out.println("");
        log.info("EventControllerPrivate.addEvent: addEvent:{},eventInputDto:{}", userId, eventInputDto);
        return eventService.addEvent(userId, eventInputDto);
    }


    @PatchMapping
    public EventFullDto updateEventFromInitiator(@PathVariable(value = "userId") @Min(0) Long userId,
                                                 @RequestBody @Valid EventInputDto eventInputDto) {
        System.out.println("");
        log.info("EventControllerPrivate.updateEventFromInitiator: userId{}, eventInputDto{}", userId, eventInputDto);
        return eventService.updateEventFromInitiator(userId, eventInputDto);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventByInitiatorId(@PathVariable(value = "userId") @Min(0) Long userId,
                                              @PathVariable(value = "eventId") @Min(0) Long eventId) {
        System.out.println("");
        log.info("EventControllerPrivate.getEventByInitiatorId: userId:{}, eventId:{}", userId, eventId);
        return eventService.getEventByInitiatorId(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable(value = "userId") @Min(0) Long userId,
                                    @PathVariable(value = "eventId") @Min(0) Long eventId) {
        System.out.println("");
        log.info("EventControllerPrivate.cancelEvent: userId:{}, eventId:{}", userId, eventId);
        return eventService.cancelEvent(userId, eventId);
    }

//    @GetMapping(("/events/{eventId}/requests"))
//    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
//    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
}