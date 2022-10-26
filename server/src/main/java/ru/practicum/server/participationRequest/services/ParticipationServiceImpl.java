package ru.practicum.server.participationRequest.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestDto;
import ru.practicum.server.participationRequest.models.ParticipationRequest;
import ru.practicum.server.participationRequest.repositories.ParticipationRepository;
import ru.practicum.server.user.models.User;
import ru.practicum.server.utils.State;
import ru.practicum.server.utils.Validation;

import java.util.List;

import static ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestMapper.requestDtoList;
import static ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestMapper.toDtoRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final Validation validation;

    @Override
    public List<ParticipationRequestDto> getUserRequestsParticipation(Long userId) {
        List<ParticipationRequestDto> requestOutputDtos = requestDtoList(participationRepository.getAllByUser(userId));
        return null;
    }

    @Override
    public ParticipationRequestDto addUserRequestParticipation(Long userId, Long eventId) {
        validation.validateAndReturnUserByUserId(userId);
        validation.validateAndReturnEventByEventId(eventId);
        return null;
    }

    @Override
    public ParticipationRequestDto cancelUserRequestParticipation(Long userId, Long requestId) {
        validation.validateAndReturnUserByUserId(userId);
        ParticipationRequest request = validation.validateAndReturnParticipationRequestByRequestId(requestId);
        validation.validateOwnerRequest(userId, request);
        request.setState(State.CANCELED);
        ParticipationRequestDto requestDto = toDtoRequest(participationRepository.save(request));
        return requestDto;
    }
}