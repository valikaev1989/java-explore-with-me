package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.comment.models.Comment;
import ru.practicum.server.comment.repositories.CommentRepository;
import ru.practicum.server.exception.models.AccessException;
import ru.practicum.server.exception.models.NotFoundException;
import ru.practicum.server.user.models.User;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentValidator {
    private final UserValidator userValidator;
    private final EventValidator eventValidator;
    private final CommentRepository commentRepository;

    public Comment validateAndReturnCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("комментарий с id '%d' не найден", commentId)));
    }

    public void validateOwnComment(Long userId, Comment comment) {
        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new AccessException(String.format("пользователь с id: '%d', не владелец комментария с id: '%d'",
                    userId, comment.getCommentId()));
        }
    }

    public void validateDisableCommentingByUser(User user) {
        if (!user.getPermissionToComment()) {
            throw new AccessException(String.format("пользователю с id: '%d' запрещено комментировать события",
                    user.getUserId()));
        }
        if (user.getBlockComments().isAfter(LocalDateTime.now()) & user.getBlockComments() != null) {
            throw new AccessException(String.format("пользователю с id: '%d' запрещено комментировать события до:" +
                            " %d-%d-%d %d:%d:%d",
                    user.getUserId(),
                    user.getBlockComments().getYear(),
                    user.getBlockComments().getMonthValue(),
                    user.getBlockComments().getDayOfMonth(),
                    user.getBlockComments().getHour(),
                    user.getBlockComments().getMinute(),
                    user.getBlockComments().getSecond()));
        }
    }
}