package ru.practicum.server.location.services;

import ru.practicum.server.location.models.LocationDtos.LocationDto;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;

public interface LocationService {
    LocationDto addLocation(LocationInputDto locationInputDto);

    LocationDto updateLocation(LocationDto locationDto);
}