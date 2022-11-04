package ru.practicum.server.location.models.LocationDtos;

import ru.practicum.server.location.models.Location;

public class LocationMapper {

    public static Location toLocation(LocationInputDto locationInputDto) {
        return Location.builder()
                .lat(locationInputDto.getLat())
                .lon(locationInputDto.getLon())
                .build();
    }

    public static LocationDto locationDto(Location location) {
        return LocationDto.builder()
                .locationId(location.getLocationId())
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .locationId(locationDto.getLocationId())
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}