package ru.practicum.server.comment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.comment.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.event.eventId = ?1 group by c.commentId")
    List<Comment> getAllByEvent_EventId(Long eventId);

    @Query("select c from Comment c where c.author.userId = ?1 group by c.commentId")
    List<Comment> getAllByAuthor_UserId(Long userId);

    @Query("select c from Comment c where c.author.userId = ?1 and c.event.eventId = ?2 group by c.commentId")
    List<Comment> getAllByAuthor_UserIdAndAndEvent_EventId(Long userId, Long eventId);

    @Query("select c from Comment c where c.author.userId = ?1 and c.commentId = ?2")
    Comment getCommentByAuthor_UserIdAndAndCommentId(Long userId, Long commentId);
}