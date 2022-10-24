package ru.practicum.server.compilation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.compilation.models.compilationDto.CompilationInputDto;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;
import ru.practicum.server.compilation.repositories.CompilationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService{
    private  final CompilationRepository compilationRepository;

    @Override
    public CompilationOutputDto addCompilation(CompilationInputDto compilationInputDto) {
        return null;
    }

    @Override
    public void deleteCompilation(Long compId) {

    }
}
