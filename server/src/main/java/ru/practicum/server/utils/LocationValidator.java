package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.repositories.LocationRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationValidator {
    private final LocationRepository locationRepository;

    public Location validateAndReturnLocationByLocationId(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(() ->
                new NotFoundException(String.format("локация с id '%d' не найдена", locationId)));
    }
}