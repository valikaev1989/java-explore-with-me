package ru.practicum.server.location.services;

import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.models.LocationDtos.LocationDto;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;

public interface LocationService {

    LocationDto addLocation(LocationInputDto locationInputDto);

    LocationDto getLocation(Long locationId);

    LocationDto updateLocation(LocationDto locationDto);

    void deleteLocation(Long locationId);
}