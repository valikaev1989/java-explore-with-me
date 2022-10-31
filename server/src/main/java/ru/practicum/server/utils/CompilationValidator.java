package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.compilation.repositories.CompilationRepository;
import ru.practicum.server.exception.models.NotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompilationValidator {
    CompilationRepository compilationRepository;

    public Compilation validateAndReturnCompilationByCompilationId(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException(String.format("компиляции событий с id '%d' не найдено", compilationId)));
    }
}