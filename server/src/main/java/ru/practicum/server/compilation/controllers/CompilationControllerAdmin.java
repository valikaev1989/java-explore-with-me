package ru.practicum.server.compilation.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.models.compilationDto.CompilationInputDto;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;
import ru.practicum.server.compilation.services.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationControllerAdmin {
    private final CompilationService compilationService;

    @PostMapping()
    public CompilationOutputDto addCompilation(@RequestBody @Valid CompilationInputDto compilationInputDto) {
        log.info("addCompilation compilationInputDto: {}", compilationInputDto);
        return compilationService.addCompilation(compilationInputDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable @Min(0) Long compId) {
        log.info("deleteCompilation compId:{}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable @Min(0) Long compId) {
        log.info("toFixCompilation compId:{}", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable @Min(0) Long compId) {
        log.info("toUnfixCompilation compId:{}", compId);
        compilationService.unpinCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable @Min(0) Long compId, @PathVariable @Min(0) Long eventId) {
        log.info("deleteEventFromCompilation compId:{}, eventId:{}", compId, eventId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable @Min(0) Long compId, @PathVariable @Min(0) Long eventId) {
        log.info("CompilationControllerAdmin.addEventInCompilation compId:{}, eventId:{}", compId, eventId);
        compilationService.addEventToCompilation(compId, eventId);
    }
}