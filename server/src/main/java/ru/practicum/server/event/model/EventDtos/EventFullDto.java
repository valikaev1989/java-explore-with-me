package ru.practicum.server.event.model.EventDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;
import ru.practicum.server.location.models.LocationDtos.LocationDto;
import ru.practicum.server.user.models.userDtos.UserOutputDto;
import ru.practicum.server.utils.State;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserOutputDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Integer confirmedRequests;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}