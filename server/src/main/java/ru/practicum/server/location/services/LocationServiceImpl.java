package ru.practicum.server.location.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.location.models.LocationDtos.LocationDto;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;
import ru.practicum.server.location.models.LocationDtos.LocationMapper;
import ru.practicum.server.location.repositories.LocationRepository;
import ru.practicum.server.utils.LocationValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationValidator validator;

    @Override
    @Transactional
    public LocationDto addLocation(LocationInputDto locationInputDto) {
        log.info("addLocation start: locationInputDto:{}", locationInputDto);
        LocationDto locationResultDto = LocationMapper.locationDto(locationRepository
                .save(LocationMapper.toLocation(locationInputDto)));
        log.info("addLocation end: locationResultDto:{}", locationResultDto);
        return locationResultDto;
    }

    @Override
    @Transactional
    public LocationDto updateLocation(LocationDto locationDto) {
        log.info("updateLocation start: locationDto:{}", locationDto);
        Location location = validator.validateAndReturnLocationByLocationId(locationDto.getLocationId());
        if (locationDto.getLat() != null) {
            location.setLat(locationDto.getLat());
        }
        if (locationDto.getLon() != null) {
            location.setLon(locationDto.getLon());
        }
        LocationDto locationDtoResult = LocationMapper.locationDto(locationRepository.save(location));
        log.info("updateLocation end: locationDtoResult:{}", locationDtoResult);
        return locationDtoResult;
    }
}