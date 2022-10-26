package ru.practicum.server.compilation.models.compilationDto;

import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.EventMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(CompilationInputDto compilationInputDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(compilationInputDto.getPinned())
                .title(compilationInputDto.getTitle())
                .eventSet(events)
                .build();
    }

    public static CompilationOutputDto toCompilationDto(Compilation compilation) {
        return CompilationOutputDto.builder()
                .id(compilation.getCompilationId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(EventMapper.ToShortDtoList(compilation.getEventSet()))
                .build();
    }

    public static List<CompilationOutputDto> getCompilationList(List<Compilation> compilationOutputDtoList) {
        return compilationOutputDtoList.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }
}