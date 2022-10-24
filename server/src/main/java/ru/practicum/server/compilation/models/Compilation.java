package ru.practicum.server.compilation.models;

import lombok.*;
import ru.practicum.server.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder(toBuilder = true)
@Table(name = "compilations")
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id", nullable = false)
    private Long compilationId;
    @Column(name = "compilation_pinned",nullable = false)
    private Boolean pinned;
    @Column(name = "Compilation_title")
    @NotBlank
    private String title;
//    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @ToString.Exclude
//    @JoinTable(
//            name = "event_compilations",
//            joinColumns = @JoinColumn(name = "compilation_id"),
//            inverseJoinColumns = @JoinColumn(name = "event_id"))
//    Set<Event> eventSet = new HashSet<>();
}