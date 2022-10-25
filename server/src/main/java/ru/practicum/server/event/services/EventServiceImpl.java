package ru.practicum.server.event.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.model.EventDtos.EventMapper;
import ru.practicum.server.event.model.EventDtos.EventShortDto;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.repositories.EventRepository;
import ru.practicum.server.exception.models.ValidationException;
import ru.practicum.server.location.models.LocationDtos.LocationMapper;
import ru.practicum.server.location.services.LocationServiceImpl;
import ru.practicum.server.utils.Validation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.server.utils.FormatDate.*;
import static ru.practicum.server.utils.FormatPage.getPage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationServiceImpl locationService;
    private final Validation validation;

    @Override
    public List<EventFullDto> getEventsByFilterForAdmin(Map<String, Object> filter) {
        log.info("EventServiceImpl.getEventsByFilterForAdmin start");
        List<Long> userIds = validation.getCorrectUserIdList((List<Long>) filter.get("users"));
        List<EventState> states = validation.getCorrectEventStateList((List<String>) filter.get("states"));
        List<Long> categoryIds = validation.getCorrectCategoryIdList((List<Long>) filter.get("categories"));
        LocalDateTime rangeStart = convertRangeStart(filter.get("rangeStart"));
        LocalDateTime rangeEnd = convertRangeEnd(filter.get("rangeEnd"));
        Pageable pageable = getPage((Integer) filter.get("from"), (Integer) filter.get("size"));
        List<EventFullDto> eventFullDtoList = EventMapper.ToFullDtoList(eventRepository
                .getAllEventsByParametersForAdmin(userIds, categoryIds, states, rangeStart, rangeEnd, pageable));
        log.info("EventServiceImpl.getEventsByFilterForAdmin end eventFullDtoList:");
        eventFullDtoList.forEach(eventFullDto -> log.info("eventFullDto:{}", eventFullDto));
        return eventFullDtoList;
    }

    @Override
    public List<EventFullDto> getAllEventsByInitiatorId(Long userId, Pageable page) {
        log.info("EventServiceImpl.getAllEventsByInitiatorId start: userId:{}, page:{}", userId, page);
        validation.validateAndReturnUserByUserId(userId);
        List<EventFullDto> eventFullDtoList = EventMapper.ToFullDtoList(
                eventRepository.findAllByInitiator_UserId(userId, page));
        log.info("EventServiceImpl.getAllEventsByInitiatorId end: eventFullDtoList:");
        if (eventFullDtoList != null) {
            eventFullDtoList.forEach(eventFullDto -> log.info("eventFullDto:{}", eventFullDto));
        }
        return eventFullDtoList;
    }

    @Override
    public List<EventShortDto> getAllEventsForPublic(Map<String, Object> filter) {
        log.info("EventServiceImpl.getAllEventsForPublic start");
        String text = (String) filter.get("text");
        List<Long> categoryIds = validation.getCorrectCategoryIdList((List<Long>) filter.get("categories"));
        Boolean paid = (Boolean) filter.get("paid");
        LocalDateTime rangeStart = convertRangeStart(filter.get("rangeStart"));
        LocalDateTime rangeEnd = convertRangeEnd(filter.get("rangeEnd"));
        Pageable pageable = getPage((Integer) filter.get("from"), (Integer) filter.get("size"));
        List<EventShortDto> eventShortDtoList = getSortPublicEvents(
                eventRepository.getAllEventsByParametersForPublic(
                        EventState.PUBLISHED, categoryIds, text, paid, rangeStart, rangeEnd, pageable),
                (Boolean) filter.get("onlyAvailable"),
                (String) filter.get("sort"));
        log.info("EventServiceImpl.getAllEventsForPublic end: eventShortDtoList:");
        eventShortDtoList.forEach(eventShortDto -> log.info("eventShortDto:{}", eventShortDto));
        return eventShortDtoList;
    }

    @Override
    public EventFullDto addEvent(Long userId, EventInputDto eventInputDto) {
        log.info("EventServiceImpl.addEvent start: userId:{}, eventInputDto:{}", userId, eventInputDto);
        validation.validateEventDateAddEvent(LocalDateTime.parse(eventInputDto.getEventDate(), FORMATTER));
        Event event = EventMapper.toEvent(
                validation.validateAndReturnUserByUserId(userId),
                validation.validateAndReturnCategoryByCategoryId(eventInputDto.getCategory()),
                LocationMapper.toLocation(locationService.addLocation(eventInputDto.getLocation())),
                eventInputDto);
        System.out.println("");
        System.out.println(event);
        System.out.println("");
        EventFullDto eventFullDto = EventMapper.ToFullDto(eventRepository.save(event));
        log.info("EventServiceImpl.addEvent end: eventFullDto{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEventFromAdmin(Long eventId, EventInputDto eventInputDto) {
        log.info("EventServiceImpl.updateEventFromAdmin start: eventId:{}, eventInputDto:{}", eventId, eventInputDto);
        Event event = validation.validateAndReturnEventByEventId(eventId);
        EventFullDto eventFullDto = EventMapper.ToFullDto(prepareForUpdateEvent(event, eventInputDto));
        log.info("EventServiceImpl.updateEventFromAdmin end: eventFullDto{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEventFromInitiator(Long userId, EventInputDto eventInputDto) {
        log.info("EventServiceImpl.updateEventFromInitiator start: userId:{}, eventInputDto:{}", userId, eventInputDto);
        validation.validateAndReturnUserByUserId(userId);
        Event event = validation.validateAndReturnEventByEventId(eventInputDto.getId());
        validation.validateForInitiatorEvent(userId, event);
        validation.validateForStatusPublished(eventInputDto);
        EventFullDto eventFullDto = EventMapper.ToFullDto(prepareForUpdateEvent(event, eventInputDto));
        log.info("EventServiceImpl.updateEventFromInitiator end: eventFullDto{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByInitiatorId(Long userId, Long eventId) {
        log.info("EventServiceImpl.getEventByInitiatorId start: userId:{}, eventId:{}", userId, eventId);
        validation.validateAndReturnUserByUserId(userId);
        Event event = validation.validateAndReturnEventByEventId(eventId);
        validation.validateForInitiatorEvent(userId, event);
        EventFullDto eventFullDto = EventMapper.ToFullDto(event);
        log.info("EventServiceImpl.getEventByInitiatorId end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventByIdForPublic(Long eventId) {
        log.info("EventServiceImpl.getEventByIdForPublic start: eventId:{}", eventId);
        Event event = validation.validateAndReturnEventByEventId(eventId);
        validation.validateForNotStatusPublished(event);
        EventFullDto eventFullDto = EventMapper.ToFullDto(event);
        log.info("EventServiceImpl.getEventByIdForPublic end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        log.info("EventServiceImpl.publishEvent start: eventId:{}", eventId);
        Event event = validation.validateAndReturnEventByEventId(eventId);
        validation.validateEventDateForPublish(event.getEventDate());
        event.setState(EventState.PUBLISHED);
        EventFullDto eventFullDto = EventMapper.ToFullDto(eventRepository.save(event));
        log.info("EventServiceImpl.publishEvent end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        log.info("EventServiceImpl.rejectEvent start: eventId:{}", eventId);
        Event event = validation.validateAndReturnEventByEventId(eventId);
        validation.validateForStatusPending(event);
        event.setState(EventState.CANCELED);
        EventFullDto eventFullDto = EventMapper.ToFullDto(eventRepository.save(event));
        log.info("EventServiceImpl.rejectEvent end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        log.info("EventServiceImpl.cancelEvent start: userId:{}, eventId:{}", userId, eventId);
        Event event = validation.validateAndReturnEventByEventId(eventId);
        validation.validateForStatusPending(event);
        validation.validateForInitiatorEvent(userId, event);
        event.setState(EventState.CANCELED);
        EventFullDto eventFullDto = EventMapper.ToFullDto(eventRepository.save(event));
        log.info("EventServiceImpl.cancelEvent end: eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    private Event prepareForUpdateEvent(Event event, EventInputDto eventInputDto) {
        LocalDateTime eventDate = validation.validateEventDateAddEvent(
                LocalDateTime.parse(eventInputDto.getEventDate(), FORMATTER));
        if (eventInputDto.getCategory() != null) {
            event.setCategory(validation.validateAndReturnCategoryByCategoryId(eventInputDto.getCategory()));
        }
        if (eventInputDto.getLocation() != null) {
            event.getLocation().setLat(eventInputDto.getLocation().getLat());
            event.getLocation().setLon(eventInputDto.getLocation().getLon());
            locationService.updateLocation(LocationMapper.locationDto(event.getLocation()));
        }
        if (eventInputDto.getAnnotation() != null) {
            event.setAnnotation(eventInputDto.getAnnotation());
        }
        if (eventInputDto.getDescription() != null) {
            event.setDescription(eventInputDto.getDescription());
        }
        if (eventDate != null) {
            event.setEventDate(eventDate);
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
                    .map(EventMapper::toShortDto)
                    .collect(Collectors.toList());
        }
        return events.stream()
                .sorted(getComparatorForPublicList(sort))
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}