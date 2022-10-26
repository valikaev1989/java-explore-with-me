package ru.practicum.server.compilation.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.compilation.models.Compilation;
import ru.practicum.server.compilation.models.compilationDto.CompilationOutputDto;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation c where c.pinned = ?1")
    List<Compilation> getAllByPinned(Boolean pinned, Pageable pageable);
}