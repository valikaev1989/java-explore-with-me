package ru.practicum.server.event.model.EventDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.location.models.LocationDtos.LocationInputDto;

import javax.validation.constraints.Min;
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
    @Min(0)
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
    @Min(0)
    private Integer participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotBlank
    private String title;
    @NotBlank
    private String state;
}
// string text
//list categoties
//boolean paid
//string rangestart
//string rangeend
//boolean onlyAvailable
//sort eventdate views
//page