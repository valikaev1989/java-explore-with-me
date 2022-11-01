package ru.practicum.server.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;
import ru.practicum.server.compilation.services.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.server.utils.FormatPage.getPage;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationControllerPublic {
    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationOutputDto> getCompilations(
            @RequestParam(value = "pinned", required = false, defaultValue = "false") Boolean pinned,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("getCompilations: pinned: {}, pageable: {}", pinned, getPage(from, size));
        return compilationService.getCompilations(pinned, getPage(from, size));
    }

    @GetMapping("/{compId}")
    public CompilationOutputDto getCompilationById(@PathVariable @Min(0) Long compId) {
        log.info("getCompilations compId:{}", compId);
        return compilationService.getCompilationById(compId);
    }
}