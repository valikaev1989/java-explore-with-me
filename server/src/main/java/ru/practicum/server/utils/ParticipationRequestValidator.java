package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.exception.models.AccessException;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.exception.models.ValidationException;
import ru.practicum.server.participationRequest.models.ParticipationRequest;
import ru.practicum.server.participationRequest.repositories.ParticipationRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationRequestValidator {
    private final ParticipationRepository participationRepository;
    private final UserValidator userValidator;

    public ParticipationRequest validateAndReturnParticipationRequestByRequestId(Long requestId) {
        return participationRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("запроса на участие в событие с id '%d' не найдено", requestId)));
    }

    public void validateOwnerRequest(Long userId, ParticipationRequest request) {
        userValidator.validateAndReturnUserByUserId(userId);
        if (!request.getUser().getUserId().equals(userId)) {
            throw new AccessException(String.format("пользователь с userId: '%d', не владелец запроса: '%d'",
                    userId, request.getRequestId()));
        }
    }

    public void validateAddParticipationRequest(Long userId, Event event) {
        if (event.getInitiator().getUserId().equals(userId)) {
            log.warn("Инициатор события с id: {} не может отправлять заявки на участие в это событие с id: {}",
                    userId, event.getEventId());
            throw new ValidationException(String.format("Инициатор события с id: ''%d не может отправлять заявки" +
                    " на участие в это событие с id: '%d'", userId, event.getEventId()));
        }
        if (participationRepository.checkDuplicateRequest(
                event.getEventId(), userId, State.PENDING, State.CONFIRMED)) {
            log.warn("Заявка пользователя с id: {} на участие в событие с id: {} уже существует",
                    userId, event.getEventId());
            throw new ValidationException(String.format("Заявка пользователя с id: '%d' на участие в событие с id:" +
                    " '%d' уже существует", userId, event.getEventId()));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.warn("Участие в событии с id: {} невозможно, так как его не опубликовали", event.getEventId());
            throw new ValidationException(String.format("Участие в событии с id: '%d' невозможно, " +
                    "так как его не опубликовали", event.getEventId()));
        }
        validateParticipantLimit(event);
    }

    public void validateParticipantLimit(Event event) {
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            log.warn("Лимит участников в событии с id: {} заполнен", event.getEventId());
            throw new ValidationException(
                    String.format("Лимит участников в событии с id: '%d' заполнен", event.getEventId()));
        }
    }
}