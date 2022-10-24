package ru.practicum.server.compilation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.compilation.models.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}