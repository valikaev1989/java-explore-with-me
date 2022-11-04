package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repositories.EventRepository;
import ru.practicum.server.exception.models.AccessException;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.exception.models.ValidationException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventValidator {
    private final EventRepository eventRepository;

    public Event validateAndReturnEventByEventId(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("событие с id '%d' не найдено", eventId)));
    }

    public Set<Event> getCorrectEventsSet(List<Long> ids) {
        log.info("Validation.getCorrectEventsList start: ids: {}", ids);
        Set<Event> events = new HashSet<>();
        for (Long id : ids) {
            try {
                Event event = validateAndReturnEventByEventId(id);
                events.add(event);
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectEventsList end: ids: {}", events);
        return events;
    }

    public LocalDateTime validateEventDateAddEvent(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            log.warn("Событие не должно быть ранее,чем через 2 часа от текущего времени");
            throw new ValidationException("Событие не должно быть ранее,чем через 2 часа от текущего времени");
        }
        return localDateTime;
    }

    public void validateEventDateForPublish(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(LocalDateTime.now().plusHours(1))) {
            log.warn("Дата начала события должна быть не ранее чем за час от даты публикации");
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }

    public void validateForInitiatorEvent(Long userId, Event event) {
        if (!event.getInitiator().getUserId().equals(userId)) {
            throw new AccessException(String.format("пользователь с userId: '%d', не владелец события: '%d'",
                    userId, event.getEventId()));
        }
    }

    public void validateForNotStatusPublished(Event event) {
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new AccessException(String.format("Событие с eventId '%d' не опубликовано, доступ запрещен",
                    event.getEventId()));
        }
    }

    public void validateForStatusPublished(Event event) {
        if (State.PUBLISHED.equals(event.getState())) {
            throw new AccessException(String.format("Событие с eventId '%d' опубликовано, редактирование запрещено",
                    event.getEventId()));
        }
    }

    public void validateForStatusPending(Event event) {
        if (!event.getState().equals(State.PENDING)) {
            throw new AccessException(String.format("Событие с eventId '%d' не в статусе ожидания, доступ запрещен",
                    event.getEventId()));
        }
    }
}