package ru.practicum.server.location.models.LocationDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private Long locationId;
    private Float lat;
    private Float lon;
}