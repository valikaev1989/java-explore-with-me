package ru.practicum.server.event.model.EventDtos;

import lombok.RequiredArgsConstructor;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.model.categoryDtos.CategoryMapper;
import ru.practicum.server.clientStatistics.EventClient;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.models.LocationDtos.LocationMapper;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.models.userDtos.UserMapper;
import ru.practicum.server.utils.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

@RequiredArgsConstructor
public class EventMapper {

    public static Event toEvent(User user, Category category, Location location, EventInputDto eventInputDto) {
        if (eventInputDto.getState() == null) {
            eventInputDto.setState(State.PENDING.toString());
        }
        return Event.builder()
                .annotation(eventInputDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(eventInputDto.getDescription())
                .eventDate(LocalDateTime.parse(eventInputDto.getEventDate(), FORMATTER))
                .initiator(user)
                .confirmedRequests(0)
                .location(location)
                .paid(eventInputDto.getPaid())
                .participantLimit(eventInputDto.getParticipantLimit())
                .requestModeration(eventInputDto.getRequestModeration())
                .title(eventInputDto.getTitle())
                .state(State.valueOf(eventInputDto.getState()))
                .build();
    }

    public static EventFullDto toFullDto(Event event, EventClient eventClient) {
        EventFullDto eventFullDto = EventFullDto.builder()
                .id(event.getEventId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toDto(event.getInitiator()))
                .location(LocationMapper.locationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(eventClient.getViews(event.getEventId()))
                .build();
        if (event.getPublishedOn() != null) {
            eventFullDto.setPublishedOn(event.getPublishedOn().format(FORMATTER));
        }
        return eventFullDto;
    }

    public static EventShortDto toShortDto(Event event, EventClient eventClient) {
        return EventShortDto.builder()
                .id(event.getEventId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(eventClient.getViews(event.getEventId()))
                .build();
    }

    public static List<EventFullDto> toFullDtoList(List<Event> userEvents, EventClient eventClient) {
        return userEvents.stream().map((Event event) -> toFullDto(event, eventClient)).collect(Collectors.toList());
    }

    public static List<EventShortDto> toShortDtoList(Set<Event> events, EventClient eventClient) {
        return events.stream().map((Event event) -> toShortDto(event, eventClient)).collect(Collectors.toList());
    }
}