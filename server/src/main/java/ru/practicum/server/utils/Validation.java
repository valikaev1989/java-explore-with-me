package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repositories.CategoryRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.repositories.EventRepository;
import ru.practicum.server.exception.models.AccessException;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.exception.models.ValidationException;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.repositories.LocationRepository;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validation {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    public User validateAndReturnUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "пользователь с id '%d' не найден", userId)));
    }

    public Category validateAndReturnCategoryByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(String.format(
                "категория с id '%d' не найдена", categoryId)));
    }

    public Location validateAndReturnLocationByLocationId(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(() -> new NotFoundException(String.format(
                "локация с id '%d' не найдена", locationId)));
    }

    public Event validateAndReturnEventByEventId(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format(
                "событие с id '%d' не найдено", eventId)));
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
            throw new AccessException(String.format(
                    "пользователь с userId: '%d', не владелец события: '%d'", userId, event.getEventId()));
        }
    }

    public void validateForNotStatusPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new AccessException(
                    String.format("Событие с eventId '%d' не опубликовано, доступ запрещен", event.getEventId()));
        }
    }

    public void validateForStatusPublished(EventInputDto event) {
        if (EventState.valueOf(event.getState()).equals(EventState.PUBLISHED)) {
            throw new AccessException(String.format(
                    "Событие с eventId '%d' опубликовано, редактирование запрещено", event.getEventId()));
        }
    }

    public void validateForStatusPending(Event event) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new AccessException(String.format(
                    "Событие с eventId '%d' не в статусе ожидания, доступ запрещен", event.getEventId()));
        }
    }

    public List<Long> getCorrectUserIdList(List<Long> ids) {
        log.info("Validation.getCorrectUserIdList start: ids:");
        ids.forEach(id -> log.info("{}", id));
        List<Long> userIds = new ArrayList<>();
        for (Long id : ids) {
            try {
                User user = validateAndReturnUserByUserId(id);
                userIds.add(user.getUserId());
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectUserIdList end: ids:");
        userIds.forEach(id -> log.info("{}", id));
        return userIds;
    }

    public Set<Event> getCorrectEventsList(List<Long> ids) {
        log.info("Validation.getCorrectEventsList start: ids:");
        ids.forEach(id -> log.info("{}", id));
        Set<Event> events = new HashSet<>();
        for (Long id : ids) {
            try {
                Event event = validateAndReturnEventByEventId(id);
                events.add(event);
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectEventsList end: ids:");
        events.forEach(event -> log.info("{}", event));
        return events;
    }

    public List<Long> getCorrectCategoryIdList(List<Long> ids) {
        log.info("Validation.getCorrectCategoryIdList start: ids:");
        ids.forEach(id -> log.info("{}", id));
        List<Long> CategoryIds = new ArrayList<>();
        for (Long id : ids) {
            try {
                Category category = validateAndReturnCategoryByCategoryId(id);
                CategoryIds.add(category.getCategoryId());
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectCategoryIdList end: CategoryIds:");
        CategoryIds.forEach(id -> log.info("{}", id));
        return CategoryIds;
    }

    public List<EventState> getCorrectEventStateList(List<String> states) {
        log.info("Validation.getCorrectEventStateList start: states:");
        states.forEach(state -> log.info("{}", state));
        List<EventState> stateList = new ArrayList<>();
        for (String state : states) {
            try {
                stateList.add(EventState.valueOf(state));
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectEventStateList end: stateList:");
        stateList.forEach(state -> log.info("{}", state));
        return stateList;
    }
}