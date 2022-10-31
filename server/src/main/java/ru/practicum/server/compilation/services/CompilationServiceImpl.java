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
    private final CompilationValidator validator;
    private final EventValidator eventValidator;
    private final EventClient eventClient;

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, Pageable page) {
        log.info("CompilationServiceImpl.getCompilations start pinned: {}, page:{}", pinned, page);
        List<CompilationOutputDto> compilationOutputDtoList = getCompilationList(compilationRepository
                .getAllByPinned(pinned, page), eventClient);
        log.info("CompilationServiceImpl.getCompilations end List:");
        compilationOutputDtoList.forEach(this::logCompilationOutputDto);
        return compilationOutputDtoList;
    }

    @Override
    @Transactional
    public CompilationOutputDto addCompilation(CompilationInputDto compilationInputDto) {
        log.info("CompilationServiceImpl.addCompilation start compilationInputDto: {}", compilationInputDto);
        Set<Event> events = eventValidator.getCorrectEventsSet(compilationInputDto.getEvents());
        Compilation compilation = toCompilation(compilationInputDto, events);
        Compilation result = compilationRepository.save(compilation);
        CompilationOutputDto compilationOutputDto = toCompilationDto(result, eventClient);
        log.info("CompilationServiceImpl.addCompilation end:");
        logCompilationOutputDto(compilationOutputDto);
        return compilationOutputDto;
    }

    @Override
    public CompilationOutputDto getCompilationById(Long compId) {
        log.info("CompilationServiceImpl.getCompilationById start compId: {}", compId);
        CompilationOutputDto compilationOutputDto = toCompilationDto(validator
                .validateAndReturnCompilationByCompilationId(compId), eventClient);
        log.info("CompilationServiceImpl.getCompilationById end:");
        logCompilationOutputDto(compilationOutputDto);
        return compilationOutputDto;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("CompilationServiceImpl.deleteCompilation start compId: {}", compId);
        compilationRepository.delete(validator.validateAndReturnCompilationByCompilationId(compId));
        log.info("CompilationServiceImpl.deleteCompilation end compId: {}", compId);
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        log.info("CompilationServiceImpl.pinCompilation start compId: {}", compId);
        Compilation compilation = validator.validateAndReturnCompilationByCompilationId(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.pinCompilation end:");
        logCompilation(validator.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compId) {
        log.info("CompilationServiceImpl.unpinCompilation start compId: {}", compId);
        Compilation compilation = validator.validateAndReturnCompilationByCompilationId(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.unpinCompilation end:");
        logCompilation(validator.validateAndReturnCompilationByCompilationId(compId));
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        log.info("CompilationServiceImpl.deleteEventFromCompilation start compId: {}, eventId: {}", compId, eventId);
        Compilation compilation = validator.validateAndReturnCompilationByCompilationId(compId);
        compilation.getEventSet().removeIf(event -> event.getEventId().equals(eventId));
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.deleteEventFromCompilation end. " +
                "delete event with id: {}, in compilation with id: {}.", eventId, compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        log.info("CompilationServiceImpl.unpinCompilation start compId: {}", compId);
        Compilation compilation = validator.validateAndReturnCompilationByCompilationId(compId);
        compilation.getEventSet().add(eventValidator.validateAndReturnEventByEventId(eventId));
        compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.addEventToCompilation end compilation:");
        logCompilation(validator.validateAndReturnCompilationByCompilationId(compId));
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