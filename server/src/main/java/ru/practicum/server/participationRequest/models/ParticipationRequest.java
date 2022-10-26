package ru.practicum.server.participationRequest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.models.User;
import ru.practicum.server.utils.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "participation_requests")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    @ManyToOne
    @JoinColumn(name = "event_event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private State state;
}