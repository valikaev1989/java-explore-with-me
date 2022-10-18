package ru.practicum.server.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.event.model.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "compilations")
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id", nullable = false)
    private Long compilationId;
    @Column(name = "compilation_pinned")
    private boolean pinned;
    @Column(name = "Compilation_title")
    private String title;
    Set<Event> eventSet = new HashSet<>();
}