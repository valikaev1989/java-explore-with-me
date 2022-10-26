package ru.practicum.server.compilation.models.compilationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.event.model.EventDtos.EventShortDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompilationOutputDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}