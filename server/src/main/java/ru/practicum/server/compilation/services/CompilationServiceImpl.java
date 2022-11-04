package ru.practicum.server.compilation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.clientStatistics.EventClient;
import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.compilation.models.compilationDto.CompilationInputDto;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;
import ru.practicum.server.compilation.repositories.CompilationRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.utils.CompilationValidator;
import ru.practicum.server.utils.EventValidator;

import java.util.List;
import java.util.Set;

import static ru.practicum.server.compilation.models.compilationDto.CompilationMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationValidator compilationValidator;
    private final EventValidator eventValidator;
    private final EventClient eventClient;

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, Pageable page) {
        log.info("getCompilations start: pinned: {}, page:{}", pinned, page);
        List<CompilationOutputDto> compilationOutputDtoList = getCompilationList(compilationRepository
                .getAllByPinned(pinned, page), eventClient);
        log.info("getCompilations end List:");
        compilationOutputDtoList.forEach(this::logCompilationOutputDto);
        return compilationOutputDtoList;
    }

    @Override
    @Transactional
    public CompilationOutputDto addCompilation(CompilationInputDto compilationInputDto) {
        log.info("addCompilation start: compilationInputDto: {}", compilationInputDto);
        Set<Event> events = eventValidator.getCorrectEventsSet(compilationInputDto.getEvents());
        Compilation compilation = toCompilation(compilationInputDto, events);
        CompilationOutputDto compilationOutputDto = toCompilationDto(compilationRepository
                .save(compilation), eventClient);
        log.info("addCompilation end:");
        logCompilationOutputDto(compilationOutputDto);
        return compilationOutputDto;
    }

    @Override
    public CompilationOutputDto getCompilationById(Long compId) {
        log.info("getCompilationById start: compId: {}", compId);
        Compilation compilation = compilationValidator
                .validateAndReturnCompilationByCompilationId(compId);
        CompilationOutputDto compilationOutputDto = toCompilationDto(compilation, eventClient);
        log.info("getCompilationById end:");
        logCompilationOutputDto(compilationOutputDto);
        return compilationOutputDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("deleteCompilation start: compId: {}", compId);
        compilationRepository.delete(compilationValidator.validateAndReturnCompilationByCompilationId(compId));
        log.info("deleteCompilation end: compId: {}", compId);
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        log.info("pinCompilation start: compId: {}", compId);
        Compilation compilation = compilationValidator.validateAndReturnCompilationByCompilationId(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("pinCompilation end:");
        logCompilation(compilationValidator.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compId) {
        log.info("unpinCompilation start: compId: {}", compId);
        Compilation compilation = compilationValidator.validateAndReturnCompilationByCompilationId(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("unpinCompilation end:");
        logCompilation(compilationValidator.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        log.info("deleteEventFromCompilation start: compId: {}, eventId: {}", compId, eventId);
        Compilation compilation = compilationValidator.validateAndReturnCompilationByCompilationId(compId);
        compilation.getEventSet().removeIf(event -> event.getEventId().equals(eventId));
        compilationRepository.save(compilation);
        log.info("deleteEventFromCompilation end: " +
                "delete event with id: {}, in compilation with id: {}.", eventId, compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        log.info("unpinCompilation start: compId: {}", compId);
        Compilation compilation = compilationValidator.validateAndReturnCompilationByCompilationId(compId);
        compilation.getEventSet().add(eventValidator.validateAndReturnEventByEventId(eventId));
        compilationRepository.save(compilation);
        log.info("addEventToCompilation end: compilation:");
        logCompilation(compilationValidator.validateAndReturnCompilationByCompilationId(compId));
    }

    private void logCompilationOutputDto(CompilationOutputDto compilationOutputDto) {
        log.info("compilation: id: {}, pinned: {}, title: {}",
                compilationOutputDto.getId(),
                compilationOutputDto.getPinned(),
                compilationOutputDto.getTitle());
        log.info("compilation: List: eventsSize: {}", compilationOutputDto.getEvents().size());
    }

    private void logCompilation(Compilation compilation) {
        log.info("compilation: id: {}, pinned: {}, title: {}",
                compilation.getCompilationId(),
                compilation.getPinned(),
                compilation.getTitle());
        log.info("compilation: List:eventsSize: {}", compilation.getEventSet().size());
    }
}