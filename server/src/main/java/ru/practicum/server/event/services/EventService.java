package ru.practicum.server.event.services;

import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;

import java.util.List;
import java.util.Map;

public interface EventService {
    List<EventFullDto> getEventByFilter(Map<String, Object> filter);

    EventFullDto updateEvent(Long eventId, EventInputDto eventInputDto);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);
}