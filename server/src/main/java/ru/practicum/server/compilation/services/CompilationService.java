package ru.practicum.server.compilation.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.compilation.models.compilationDto.CompilationInputDto;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;

import java.util.List;

public interface CompilationService {
    List<CompilationOutputDto> getCompilations(Boolean pinned, Pageable page);

    CompilationOutputDto addCompilation(CompilationInputDto compilationInputDto);

    CompilationOutputDto getCompilationById(Long compId);

    void deleteCompilation(Long compId);

    void pinCompilation(Long compId);

    void unpinCompilation(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);
}