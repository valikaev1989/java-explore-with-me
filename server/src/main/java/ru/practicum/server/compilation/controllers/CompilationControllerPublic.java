package ru.practicum.server.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;
import ru.practicum.server.compilation.services.CompilationService;

import java.util.List;

@Slf4j
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPublic {
    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationOutputDto> getCompilations() {
        return List.of();
    }

    @GetMapping("/compId")
    public CompilationOutputDto getCompilationById() {
        return new CompilationOutputDto();
    }
}