package ru.practicum.server.event.model.EventDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventInputDto {
    @NotBlank
    private String annotation;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private LocationInputDto location;
    @NotNull
    private Boolean paid;
    @NotNull
    private Integer participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotBlank
    private String title;
    @NotBlank
    private String state;
}