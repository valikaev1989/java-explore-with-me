package ru.practicum.server.compilation.models.compilationDto;

import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.EventMapper;

import java.util.Set;

public class CompilationMapper {

//    public static Compilation toCompilation(CompilationInputDto compilationInputDto, Set<Event> events) {
//        return Compilation.builder()
//                .pinned(compilationInputDto.isPinned())
//                .title(compilationInputDto.getTitle())
//                .eventSet(events)
//                .build();
//    }
//
//    public static CompilationOutputDto toCompilationDto(Compilation compilation) {
//        return CompilationOutputDto.builder()
//                .compilationId(compilation.getCompilationId())
//                .pinned(compilation.getPinned())
//                .title(compilation.getTitle())
//                .events(EventMapper.ToShortDtoList(compilation.getEventSet()))
//                .build();
//    }
}