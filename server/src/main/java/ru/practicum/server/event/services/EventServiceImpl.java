package ru.practicum.server.event.services;

import ru.practicum.server.event.model.EventDtos.EventFullDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;

import java.util.List;
import java.util.Map;

public class EventServiceImpl implements EventService {
    @Override
    public List<EventFullDto> getEventByFilter(Map<String, Object> filter) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, EventInputDto eventInputDto) {
        return null;
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        return null;
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        return null;
    }
}