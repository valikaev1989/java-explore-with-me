package ru.practicum.server.participationRequest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestDto;
import ru.practicum.server.participationRequest.services.ParticipationService;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationControllerPrivate {
    private final ParticipationService participationService;

    @PostMapping
    public ParticipationRequestDto addUserRequestParticipation(
            @PathVariable(value = "userId") @Min(0) Long userId, @RequestParam @Min(0) Long eventId) {
        log.info("addUserRequestParticipation: userId:{}, eventId:{}",
                userId, eventId);
        return participationService.addUserRequestParticipation(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequestParticipation(
            @PathVariable(value = "userId") @Min(0) Long userId,
            @PathVariable(value = "requestId") @Min(0) Long requestId) {
        log.info("cancelUserRequestParticipation: userId:{}, eventId:{}",
                userId, requestId);
        return participationService.cancelUserRequestParticipation(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getUserRequestsParticipation(
            @PathVariable(value = "userId") @Min(0) Long userId) {
        log.info("getUserRequestsParticipation: userId:{}", userId);
        return participationService.getUserRequestsParticipation(userId);
    }
}