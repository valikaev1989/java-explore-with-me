package ru.practicum.server.participationRequest.services;

import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getUserRequestsParticipation(Long userId);

    ParticipationRequestDto addUserRequestParticipation(Long userId, Long eventId);

    ParticipationRequestDto cancelUserRequestParticipation(Long userId, Long requestId);
}