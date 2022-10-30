package ru.practicum.server.compilation.models.compilationDto;

import ru.practicum.server.clientStatistics.EventClient;
import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.event.model.Event;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.server.event.model.EventDtos.EventMapper.ToShortDtoList;

public class CompilationMapper {

    public static Compilation toCompilation(CompilationInputDto compilationInputDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(compilationInputDto.getPinned())
                .title(compilationInputDto.getTitle())
                .eventSet(events)
                .build();
    }

    public static CompilationOutputDto toCompilationDto(Compilation compilation, EventClient eventClient) {
        return CompilationOutputDto.builder()
                .id(compilation.getCompilationId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(ToShortDtoList(compilation.getEventSet(), eventClient))
                .build();
    }

    public static List<CompilationOutputDto> getCompilationList(
            List<Compilation> compilationOutputDtoList, EventClient eventClient) {
        return compilationOutputDtoList.stream().map((Compilation compilation) ->
                toCompilationDto(compilation, eventClient)).collect(Collectors.toList());
    }
}