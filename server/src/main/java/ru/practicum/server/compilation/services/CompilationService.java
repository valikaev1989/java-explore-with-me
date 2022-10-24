package ru.practicum.server.compilation.services;

import ru.practicum.server.compilation.models.compilationDto.CompilationInputDto;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;

public interface CompilationService {
    CompilationOutputDto addCompilation(CompilationInputDto compilationInputDto);

    void deleteCompilation(Long compId);
}
