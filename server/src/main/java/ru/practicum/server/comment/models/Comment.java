package ru.practicum.server.comment.models;

import lombok.*;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.models.User;
import ru.practicum.server.utils.CommentStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "comments")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @Column(name = "text", nullable = false, length = 5000)
    private String text;
    @ManyToOne()
    @JoinColumn(name = "event_event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;
    @ManyToOne()
    @JoinColumn(name = "author_user_id", nullable = false)
    private User author;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "edited_on")
    private LocalDateTime editedOn;
    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;
}