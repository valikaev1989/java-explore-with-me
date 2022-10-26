package ru.practicum.server.compilation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.compilation.models.compilationDto.CompilationInputDto;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;
import ru.practicum.server.compilation.repositories.CompilationRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.utils.Validation;

import java.util.List;
import java.util.Set;

import static ru.practicum.server.compilation.models.compilationDto.CompilationMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final Validation validation;

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, Pageable page) {
        log.info("CompilationServiceImpl.getCompilations start pinned: {}, page:{}", pinned, page);
        List<CompilationOutputDto> compilationOutputDtoList = getCompilationList(compilationRepository
                .getAllByPinned(pinned, page));
        log.info("CompilationServiceImpl.getCompilations end List:");
        compilationOutputDtoList.forEach(this::logCompilationOutputDto);
        return compilationOutputDtoList;
    }

    @Override
    public CompilationOutputDto addCompilation(CompilationInputDto compilationInputDto) {
        log.info("CompilationServiceImpl.addCompilation start compilationInputDto: {}", compilationInputDto);
        Set<Event> events = validation.getCorrectEventsSet(compilationInputDto.getEvents());
        CompilationOutputDto compilationOutputDto = toCompilationDto(
                compilationRepository.save(toCompilation(compilationInputDto, events)));
        log.info("CompilationServiceImpl.addCompilation end:");
        logCompilationOutputDto(compilationOutputDto);
        return compilationOutputDto;
    }

    @Override
    public CompilationOutputDto getCompilationById(Long compId) {
        log.info("CompilationServiceImpl.getCompilationById start compId: {}", compId);
        CompilationOutputDto compilationOutputDto = toCompilationDto(validation
                .validateAndReturnCompilationByCompilationId(compId));
        log.info("CompilationServiceImpl.getCompilationById end:");
        logCompilationOutputDto(compilationOutputDto);
        return compilationOutputDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.info("CompilationServiceImpl.deleteCompilation start compId: {}", compId);
        validation.validateAndReturnCompilationByCompilationId(compId);
        compilationRepository.deleteById(compId);
        log.info("CompilationServiceImpl.deleteCompilation end compId: {}", compId);
    }

    @Override
    public void pinCompilation(Long compId) {
        log.info("CompilationServiceImpl.pinCompilation start compId: {}", compId);
        Compilation compilation = validation.validateAndReturnCompilationByCompilationId(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.pinCompilation end:");
        logCompilation(validation.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    public void unpinCompilation(Long compId) {
        log.info("CompilationServiceImpl.unpinCompilation start compId: {}", compId);
        Compilation compilation = validation.validateAndReturnCompilationByCompilationId(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.unpinCompilation end:");
        logCompilation(validation.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        log.info("CompilationServiceImpl.deleteEventFromCompilation start compId: {}, eventId: {}", compId, eventId);
        Compilation compilation = validation.validateAndReturnCompilationByCompilationId(compId);
        compilation.getEventSet().removeIf(e -> e.getEventId().equals(eventId));
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.deleteEventFromCompilation end:");
        logCompilation(validation.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        log.info("CompilationServiceImpl.unpinCompilation start compId: {}", compId);
        Compilation compilation = validation.validateAndReturnCompilationByCompilationId(compId);
        compilation.getEventSet().add(validation.validateAndReturnEventByEventId(eventId));
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.addEventToCompilation end compilation:");
        logCompilation(validation.validateAndReturnCompilationByCompilationId(compId));
    }

    private void logCompilationOutputDto(CompilationOutputDto compilationOutputDto) {
        log.info("compilation: id: {}, pinned: {}, title: {}",
                compilationOutputDto.getId(),
                compilationOutputDto.getPinned(),
                compilationOutputDto.getTitle());
        log.info("compilation: List:");
        compilationOutputDto.getEvents().forEach(eventShortDto -> log.info("eventShortDto: {}", eventShortDto));
    }

    private void logCompilation(Compilation compilation) {
        log.info("compilation: id: {}, pinned: {}, title: {}",
                compilation.getCompilationId(),
                compilation.getPinned(),
                compilation.getTitle());
        log.info("compilation: List:");
        compilation.getEventSet().forEach(event -> log.info("eventShortDto: {}", event));
    }
}