package ru.practicum.server.event.model;

import lombok.*;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.comment.models.Comment;
import ru.practicum.server.location.models.Location;
import ru.practicum.server.user.models.User;
import ru.practicum.server.utils.State;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
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
    @Column(name = "annotation", length = 5000)
    @NotBlank
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_category_id")
    private Category category;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @NotBlank
    @Column(name = "description", length = 5000)
    private String description;
    @Column(name = "event_date", nullable = false)
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
    private Integer participantLimit;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "title", nullable = false)
    private String title;
    private Integer views = 0;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "event")
    @ToString.Exclude
    private List<Comment> comments;
}