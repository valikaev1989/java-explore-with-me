package ru.practicum.server.event.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.clientStatistics.EventClient;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.model.EventDtos.EventMapper;
import ru.practicum.server.event.model.EventDtos.EventShortDto;
import ru.practicum.server.utils.*;
import ru.practicum.server.event.repositories.EventRepository;
import ru.practicum.server.exception.models.ValidationException;
import ru.practicum.server.location.models.LocationDtos.LocationMapper;
import ru.practicum.server.location.services.LocationServiceImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.server.utils.FormatDate.*;
import static ru.practicum.server.utils.FormatPage.getPage;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;
    private final LocationServiceImpl locationService;
    private final UserValidator userValidator;
    private final EventValidator eventValidator;
    private final CategoryValidator categoryValidator;
    private final EventClient eventClient;
    private final StateValidator stateValidator;

    @Override
    public List<EventFullDto> getEventsByFilterForAdmin(Map<String, Object> filter) {
        log.info("EventServiceImpl.getEventsByFilterForAdmin start filter:");
        filter.forEach((key, value) -> log.info("{}:{}", key, value));
        List<Long> userIds = userValidator.getCorrectUserIdList((List<Long>) filter.get("users"));
        List<State> states = stateValidator.getCorrectStateList((List<String>) filter.get("states"));
        List<Long> categoryIds = categoryValidator.getCorrectCategoryIdList((List<Long>) filter.get("categories"));
        LocalDateTime rangeStart = convertRangeStart(filter.get("rangeStart"));
        LocalDateTime rangeEnd = convertRangeEnd(filter.get("rangeEnd"));
        Pageable pageable = getPage((Integer) filter.get("from"), (Integer) filter.get("size"));
        List<EventFullDto> eventFullDtoList = EventMapper.toFullDtoList(eventRepository
                .getAllEventsByParametersForAdmin(
                        userIds, categoryIds, states, rangeStart, rangeEnd, pageable), eventClient);
        log.info("EventServiceImpl.getEventsByFilterForAdmin end eventFullDtoList:");
        eventFullDtoList.forEach(eventFullDto -> log.info("eventFullDto:{}", eventFullDto));
        return eventFullDtoList;
    }

    @Override
    public List<EventFullDto> getAllEventsByInitiatorId(Long userId, Pageable page) {
        log.info("EventServiceImpl.getAllEventsByInitiatorId start: userId:{}, page:{}", userId, page);
        userValidator.validateAndReturnUserByUserId(userId);
        List<EventFullDto> eventFullDtoList = EventMapper.toFullDtoList(
                eventRepository.findAllByInitiator_UserId(userId, page), eventClient);
        log.info("EventServiceImpl.getAllEventsByInitiatorId end: eventFullDtoList:");
        eventFullDtoList.forEach(eventFullDto -> log.info("eventFullDto:{}", eventFullDto));
        return eventFullDtoList;
    }

    @Override
    public List<EventShortDto> getAllEventsForPublic(Map<String, Object> filter) {
        log.info("EventServiceImpl.getAllEventsForPublic start: filter:");
        filter.forEach((key, value) -> log.info("{}:{}", key, value));
        String text = (String) filter.get("text");
        List<Long> categoryIds = categoryValidator.getCorrectCategoryIdList((List<Long>) filter.get("categories"));
        Boolean paid = (Boolean) filter.get("paid");
        LocalDateTime rangeStart = convertRangeStart(filter.get("rangeStart"));
        LocalDateTime rangeEnd = convertRangeEnd(filter.get("rangeEnd"));
        Pageable pageable = getPage((Integer) filter.get("from"), (Integer) filter.get("size"));
        List<EventShortDto> eventShortDtoList = getSortPublicEvents(
                eventRepository.getAllEventsByParametersForPublic(
                        State.PUBLISHED, categoryIds, text, paid, rangeStart, rangeEnd, pageable),
                (Boolean) filter.get("onlyAvailable"),
                (String) filter.get("sort"));
        log.info("EventServiceImpl.getAllEventsForPublic end: eventShortDtoList:");
        eventShortDtoList.forEach(eventShortDto -> log.info("eventShortDto:{}", eventShortDto));
        return eventShortDtoList;
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, EventInputDto eventInputDto) {
        log.info("EventServiceImpl.addEvent start: userId: {}, eventInputDto: {}", userId, eventInputDto);
        eventValidator.validateEventDateAddEvent(LocalDateTime.parse(eventInputDto.getEventDate(), FORMATTER));
        Event event = EventMapper.toEvent(
                userValidator.validateAndReturnUserByUserId(userId),
                categoryValidator.validateAndReturnCategoryByCategoryId(eventInputDto.getCategory()),
                LocationMapper.toLocation(locationService.addLocation(eventInputDto.getLocation())),
                eventInputDto);
        EventFullDto eventFullDto = EventMapper.toFullDto(eventRepository.save(event), eventClient);
        log.info("EventServiceImpl.addEvent end: eventFullDto: {}", eventFullDto);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEventFromAdmin(Long eventId, EventInputDto eventInputDto) {
        log.info("EventServiceImpl.updateEventFromAdmin start: eventId:{}, eventInputDto:{}", eventId, eventInputDto);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        EventFullDto eventFullDto = EventMapper.toFullDto(prepareForUpdateEvent(event, eventInputDto), eventClient);
        log.info("EventServiceImpl.updateEventFromAdmin end: eventFullDto{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEventFromInitiator(Long userId, EventInputDto eventInputDto) {
        log.info("EventServiceImpl.updateEventFromInitiator start: userId:{}, eventInputDto:{}",
                userId, eventInputDto);
        userValidator.validateAndReturnUserByUserId(userId);
        Event event = eventRepository.getByAndInitiator_UserId(userId);
        eventValidator.validateForStatusPublished(event);
        if (event.getState().equals(State.REJECTED)) {
            event.setState(State.PENDING);
        }
        prepareForUpdateEvent(event, eventInputDto);
        EventFullDto eventFullDto = EventMapper.toFullDto(event, eventClient);
        log.info("EventServiceImpl.updateEventFromInitiator end: eventFullDto{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByInitiatorId(Long userId, Long eventId) {
        log.info("EventServiceImpl.getEventByInitiatorId start: userId:{}, eventId:{}", userId, eventId);
        userValidator.validateAndReturnUserByUserId(userId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        eventValidator.validateForInitiatorEvent(userId, event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event, eventClient);
        log.info("EventServiceImpl.getEventByInitiatorId end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByIdForPublic(Long eventId) {
        log.info("EventServiceImpl.getEventByIdForPublic start: eventId:{}", eventId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        eventValidator.validateForNotStatusPublished(event);
        EventFullDto eventFullDto = EventMapper.toFullDto(event, eventClient);
        log.info("EventServiceImpl.getEventByIdForPublic end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        log.info("EventServiceImpl.publishEvent start: eventId:{}", eventId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        eventValidator.validateEventDateForPublish(event.getEventDate());
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        EventFullDto eventFullDto = EventMapper.toFullDto(eventRepository.save(event), eventClient);
        log.info("EventServiceImpl.publishEvent end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        log.info("EventServiceImpl.rejectEvent start: eventId:{}", eventId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        eventValidator.validateForStatusPending(event);
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = EventMapper.toFullDto(eventRepository.save(event), eventClient);
        log.info("EventServiceImpl.rejectEvent end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        log.info("EventServiceImpl.cancelEvent start: userId:{}, eventId:{}", userId, eventId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        eventValidator.validateForStatusPending(event);
        eventValidator.validateForInitiatorEvent(userId, event);
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = EventMapper.toFullDto(eventRepository.save(event), eventClient);
        log.info("EventServiceImpl.cancelEvent end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    private Event prepareForUpdateEvent(Event event, EventInputDto eventInputDto) {

        if (eventInputDto.getCategory() != null) {
            event.setCategory(categoryValidator.validateAndReturnCategoryByCategoryId(eventInputDto.getCategory()));
        }
        if (eventInputDto.getLocation() != null) {
            event.getLocation().setLat(eventInputDto.getLocation().getLat());
            event.getLocation().setLon(eventInputDto.getLocation().getLon());
            locationService.updateLocation(LocationMapper.locationDto(event.getLocation()));
        }
        if (eventInputDto.getEventDate() != null) {
            LocalDateTime eventDate = eventValidator.validateEventDateAddEvent(
                    LocalDateTime.parse(eventInputDto.getEventDate(), FORMATTER));
            event.setEventDate(eventDate);
        }
        if (eventInputDto.getAnnotation() != null) {
            event.setAnnotation(eventInputDto.getAnnotation());
        }
        if (eventInputDto.getDescription() != null) {
            event.setDescription(eventInputDto.getDescription());
        }

        if (eventInputDto.getPaid() != null) {
            event.setPaid(eventInputDto.getPaid());
        }
        if (eventInputDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventInputDto.getParticipantLimit());
        }
        if (eventInputDto.getRequestModeration() != null) {
            event.setRequestModeration(eventInputDto.getRequestModeration());
        }
        if (eventInputDto.getTitle() != null) {
            event.setTitle(eventInputDto.getTitle());
        }
        if (eventInputDto.getAnnotation() != null) {
            event.setAnnotation(eventInputDto.getAnnotation());
        }
        return eventRepository.save(event);
    }

    private Comparator<Event> getComparatorForPublicList(String sort) {
        switch (sort) {
            case ("EVENT_DATE"):
                return Comparator.comparing(Event::getEventDate);
            case ("VIEWS"):
                return Comparator.comparing(Event::getViews);
            default:
                throw new ValidationException("Неизвестная сортировка: " + sort + ".");
        }
    }

    private List<EventShortDto> getSortPublicEvents(List<Event> events, Boolean onlyAvailable, String sort) {
        if (onlyAvailable) {
            return events.stream()
                    .filter(event -> event.getParticipantLimit() > event.getConfirmedRequests())
                    .sorted(getComparatorForPublicList(sort))
                    .map((Event event1) -> EventMapper.toShortDto(event1, eventClient))
                    .collect(Collectors.toList());
        }
        return events.stream()
                .sorted(getComparatorForPublicList(sort))
                .map((Event event) -> EventMapper.toShortDto(event, eventClient))
                .collect(Collectors.toList());
    }
}