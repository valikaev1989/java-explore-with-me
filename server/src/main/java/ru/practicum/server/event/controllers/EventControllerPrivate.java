package ru.practicum.server.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.services.EventService;
import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestDto;
import ru.practicum.server.participationRequest.services.ParticipationService;
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
    private final ParticipationService participationService;

    @GetMapping
    public List<EventFullDto> getAllEventsByInitiatorId(
            @PathVariable(value = "userId") @Min(0) Long userId,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        log.info("getAllEventsByUserId: userId:{}, from:{}, size:{}", userId, from, size);
        return eventService.getAllEventsByInitiatorId(userId, FormatPage.getPage(from, size));
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable(value = "userId") Long userId,
                                 @RequestBody EventInputDto eventInputDto) {
        log.info("addEvent: addEvent:{},eventInputDto:{}", userId, eventInputDto);
        return eventService.addEvent(userId, eventInputDto);
    }


    @PatchMapping
    public EventFullDto updateEventFromInitiator(@PathVariable(value = "userId") @Min(0) Long userId,
                                                 @RequestBody @Valid EventInputDto eventInputDto) {
        log.info("updateEventFromInitiator: userId{}, eventInputDto{}", userId, eventInputDto);
        return eventService.updateEventFromInitiator(userId, eventInputDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByInitiatorId(@PathVariable(value = "userId") @Min(0) Long userId,
                                              @PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("getEventByInitiatorId: userId:{}, eventId:{}", userId, eventId);
        return eventService.getEventByInitiatorId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable(value = "userId") @Min(0) Long userId,
                                    @PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("cancelEvent: userId:{}, eventId:{}", userId, eventId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping(("/{eventId}/requests"))
    public List<ParticipationRequestDto> getOwnerEventRequests(
            @PathVariable(value = "userId") Long userId, @PathVariable(value = "eventId") @Min(0) Long eventId) {
        log.info("getOwnerEventRequest: userId:{}, eventId:{}", userId, eventId);
        return participationService.getOwnerEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequest(@PathVariable(value = "userId") @Min(0) Long userId,
                                                               @PathVariable(value = "eventId") @Min(0) Long eventId,
                                                               @PathVariable(value = "reqId") @Min(0) Long reqId) {
        log.info("confirmParticipationRequest: userId:{}, eventId:{}, reqId: {}", userId, eventId, reqId);
        return participationService.confirmParticipationRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationRequest(@PathVariable(value = "userId") @Min(0) Long userId,
                                                              @PathVariable(value = "eventId") @Min(0) Long eventId,
                                                              @PathVariable(value = "reqId") @Min(0) Long reqId) {
        log.info("confirmParticipationRequest: userId:{}, eventId:{}, reqId: {}", userId, eventId, reqId);
        return participationService.rejectParticipationRequest(userId, eventId, reqId);
    }
}