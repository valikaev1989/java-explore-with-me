package ru.practicum.server.event.model.EventDtos;

import lombok.RequiredArgsConstructor;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.model.categoryDtos.CategoryMapper;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.models.LocationDtos.LocationMapper;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.models.userDtos.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.server.utils.FormatDate.FORMATTER;

@RequiredArgsConstructor
public class EventMapper {

    public static Event toEvent(User user, Category category, Location location, EventInputDto eventInputDto) {
        return Event.builder()
                .annotation(eventInputDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(eventInputDto.getDescription())
                .eventDate(LocalDateTime.parse(eventInputDto.getEventDate(), FORMATTER))
                .initiator(user)
                .confirmedRequest(0)
                .location(location)
                .paid(eventInputDto.getPaid())
                .participantLimit(eventInputDto.getParticipantLimit())
                .requestModeration(eventInputDto.getRequestModeration())
                .title(eventInputDto.getTitle())
                .state(EventState.valueOf(eventInputDto.getState()))
                .build();
    }

    public static EventFullDto ToFullDto(Event event) {
        return EventFullDto.builder()
                .eventId(event.getEventId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toDto(event.getInitiator()))
                .location(LocationMapper.locationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequest(event.getConfirmedRequest())
                .publishedOn(event.getPublishedOn().format(FORMATTER))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(0)
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .eventId(event.getEventId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequest())
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(UserMapper.toDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventFullDto> ToFullDtoList(List<Event> userEvents) {
        return userEvents.stream().map(EventMapper::ToFullDto).collect(Collectors.toList());
    }

    public static List<EventShortDto> ToShortDtoList(Set<Event> events) {
        return events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }
}