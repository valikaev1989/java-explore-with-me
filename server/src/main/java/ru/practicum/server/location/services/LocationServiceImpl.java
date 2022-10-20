package ru.practicum.server.location.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.models.LocationDtos.LocationDto;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;
import ru.practicum.server.location.models.LocationDtos.LocationMapper;
import ru.practicum.server.location.repositories.LocationRepository;
import ru.practicum.server.utils.Validation;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final Validation validation;

    @Override
    public LocationDto addLocation(LocationInputDto locationInputDto) {
        log.info("LocationService.addLocation start: locationInputDto:{}", locationInputDto);
        LocationDto locationResultDto = LocationMapper.locationDto(locationRepository.save(LocationMapper.toLocation(locationInputDto)));
        log.info("LocationService.addLocation end: locationResultDto:{}", locationResultDto);
        return locationResultDto;
    }

    @Override
    public LocationDto getLocation(Long locationId) {
        log.info("LocationService.getLocation start: locationId:{}", locationId);
        LocationDto locationDto = LocationMapper.locationDto(validation.validateAndReturnLocationByLocationId(locationId));
        log.info("LocationService.getLocation end: locationDto:{}", locationDto);
        return locationDto;
    }

    @Override
    public LocationDto updateLocation(LocationDto locationDto) {
        log.info("LocationService.updateLocation start: locationDto:{}", locationDto);
        Location location = validation.validateAndReturnLocationByLocationId(locationDto.getLocationId());
        if (locationDto.getLat() != null) {
            location.setLat(locationDto.getLat());
        }
        if (locationDto.getLon() != null) {
            location.setLon(locationDto.getLon());
        }
        LocationDto locationDtoResult = LocationMapper.locationDto(locationRepository.save(location));
        log.info("LocationService.updateLocation end: locationDtoResult:{}", locationDtoResult);
        return locationDtoResult;
    }

    @Override
    public void deleteLocation(Long locationId) {
        log.info("LocationService.deleteLocation start: locationId:{}", locationId);
        validation.validateAndReturnLocationByLocationId(locationId);
        locationRepository.deleteById(locationId);
        log.info("LocationService.deleteLocation end: locationId:{}", locationId);
    }
}