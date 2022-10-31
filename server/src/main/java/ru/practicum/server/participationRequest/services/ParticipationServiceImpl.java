package ru.practicum.server.participationRequest.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repositories.EventRepository;
import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestDto;
import ru.practicum.server.participationRequest.models.ParticipationDto.ParticipationRequestMapper;
import ru.practicum.server.participationRequest.models.ParticipationRequest;
import ru.practicum.server.participationRequest.repositories.ParticipationRepository;
import ru.practicum.server.utils.EventValidator;
import ru.practicum.server.utils.ParticipationRequestValidator;
import ru.practicum.server.utils.State;
import ru.practicum.server.utils.UserValidator;

import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestValidator requestValidator;
    private final UserValidator validator;
    private final EventValidator eventValidator;

    @Override
    public List<ParticipationRequestDto> getUserRequestsParticipation(Long userId) {
        log.info("ParticipationServiceImpl.getUserRequestsParticipation start: userId: {}", userId);
        validator.validateAndReturnUserByUserId(userId);
        List<ParticipationRequestDto> requestOutputDtos = ParticipationRequestMapper.requestDtoList(
                participationRepository.getAllByUser_UserId(userId));
        log.info("ParticipationServiceImpl.getUserRequestsParticipation end: requestOutputDtos:");
        requestOutputDtos.forEach(requestDto -> log.info("requestDto: {}", requestDto));
        return requestOutputDtos;
    }

    @Override
    @Transactional
    public ParticipationRequestDto addUserRequestParticipation(Long userId, Long eventId) {
        log.info("ParticipationServiceImpl.addUserRequestParticipation start: userId: {}, eventId: {}",
                userId, eventId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        requestValidator.validateAddParticipationRequest(userId, event);
        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setUser(validator.validateAndReturnUserByUserId(userId));
        request.setStatus(event.getRequestModeration() ? State.PENDING : State.CONFIRMED);
        ParticipationRequestDto requestDto = ParticipationRequestMapper
                .toDtoRequest(participationRepository.save(request));
        if (requestDto.getStatus().equals(State.CONFIRMED.toString())) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        log.info("ParticipationServiceImpl.addUserRequestParticipation end: requestDto: {}", requestDto);
        return requestDto;
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelUserRequestParticipation(Long userId, Long requestId) {
        log.info("ParticipationServiceImpl.cancelUserRequestParticipation start: userId: {}, requestId :{}",
                userId, requestId);
        ParticipationRequest request = requestValidator.validateAndReturnParticipationRequestByRequestId(requestId);
        requestValidator.validateOwnerRequest(userId, request);
        request.setStatus(State.CANCELED);
        ParticipationRequestDto requestDto = ParticipationRequestMapper
                .toDtoRequest(participationRepository.save(request));
        log.info("ParticipationServiceImpl.cancelUserRequestParticipation end: requestDto:{}", requestDto);
        return requestDto;
    }

    @Override
    public List<ParticipationRequestDto> getOwnerEventRequests(Long userId, Long eventId) {
        log.info("ParticipationServiceImpl.getOwnerEventRequests start: userId: {}, eventId :{}", userId, eventId);
        validator.validateAndReturnUserByUserId(userId);
        eventValidator.validateAndReturnEventByEventId(eventId);
        List<ParticipationRequestDto> requestDtoList = ParticipationRequestMapper
                .requestDtoList(participationRepository
                        .getOwnerEventRequests(userId, eventId));
        log.info("ParticipationServiceImpl.getOwnerEventRequests end: requestDtoList:");
        requestDtoList.forEach(requestDto -> log.info("requestDto: {}", requestDtoList));
        return requestDtoList;
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmParticipationRequest(Long userId, Long eventId, Long reqId) {
        log.info("ParticipationServiceImpl.confirmParticipationRequest start: userId: {}, eventId :{}, reqId: {}",
                userId, eventId, reqId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        ParticipationRequest request = requestValidator.validateAndReturnParticipationRequestByRequestId(reqId);
        eventValidator.validateForInitiatorEvent(userId, event);
        confirmedOrRejectedForRequest(event, request);
        addConfirmRequestInEvent(event, request);
        rejectedOtherRequests(event);
        ParticipationRequestDto requestDto = ParticipationRequestMapper.toDtoRequest(request);
        log.info("ParticipationServiceImpl.confirmParticipationRequest end: requestDto:{}", requestDto);
        return requestDto;
    }


    @Override
    @Transactional
    public ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long reqId) {
        log.info("ParticipationServiceImpl.rejectParticipationRequest start: userId: {}, eventId :{}, reqId: {}",
                userId, eventId, reqId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        eventValidator.validateForInitiatorEvent(userId, event);
        ParticipationRequest request = requestValidator.validateAndReturnParticipationRequestByRequestId(reqId);
        request.setStatus(State.REJECTED);
        ParticipationRequestDto requestDto = ParticipationRequestMapper
                .toDtoRequest(participationRepository.save(request));
        log.info("ParticipationServiceImpl.rejectParticipationRequest end: requestDto:{}", requestDto);
        return requestDto;
    }

    private void confirmedOrRejectedForRequest(Event event, ParticipationRequest request) {
        request.setStatus((event.getParticipantLimit() > event.getConfirmedRequests())
                || event.getParticipantLimit() == 0
                || !event.getRequestModeration() ? State.CONFIRMED : State.REJECTED);
        participationRepository.save(request);
    }

    private void rejectedOtherRequests(Event event) {
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            List<ParticipationRequest> requests = participationRepository
                    .getAllByEventAndAndStatusNot(event, State.CONFIRMED);
            for (ParticipationRequest req : requests) {
                req.setStatus(State.REJECTED);
                participationRepository.save(req);
            }
        }
    }

    private void addConfirmRequestInEvent(Event event, ParticipationRequest request) {
        if (request.getStatus().equals(State.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
    }
}