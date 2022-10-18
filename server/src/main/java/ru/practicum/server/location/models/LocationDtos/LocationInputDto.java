package ru.practicum.server.location.models.LocationDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LocationInputDto {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}