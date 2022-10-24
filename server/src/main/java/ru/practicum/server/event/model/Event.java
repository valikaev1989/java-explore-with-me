package ru.practicum.server.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.user.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long eventId;
    @Column(name = "annotation")
    @NotBlank
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_category_id")
    private Category category;
    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;
    @NotBlank
    @Column(name = "description")
    private String description;
    @Column(name = "eventDate", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_user_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_location_id")
    private Location location;
    @Column(name = "paid", nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit = 0;
    @Column(name = "confirmed_request")
    private Integer confirmedRequest = 0;
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;
    @Column(name = "request moderation", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "title", nullable = false)
    private String title;
    private Integer views;
}