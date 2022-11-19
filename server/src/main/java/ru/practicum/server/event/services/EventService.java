package ru.practicum.server.event.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.event.model.EventDtos.AdminParamsDto;
import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.model.EventDtos.EventShortDto;

import java.util.List;
import java.util.Map;

public interface EventService {
    List<EventFullDto> getEventsByFilterForAdmin(AdminParamsDto paramsDto);

    List<EventFullDto> getAllEventsByInitiatorId(Long userId, Pageable page);

    List<EventShortDto> getAllEventsForPublic(Map<String, Object> filter);

    EventFullDto addEvent(Long userId, EventInputDto eventInputDto);

    EventFullDto updateEventFromAdmin(Long eventId, EventInputDto eventInputDto);

    EventFullDto updateEventFromInitiator(Long userId, EventInputDto eventInputDto);

    EventFullDto getEventByInitiatorId(Long userId, Long eventId);

    EventFullDto getEventByIdForPublic(Long eventId);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);
}