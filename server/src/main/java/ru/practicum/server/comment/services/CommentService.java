package ru.practicum.server.comment.services;

import ru.practicum.server.comment.models.CommentDto;
import ru.practicum.server.comment.models.CommentInputDto;
import ru.practicum.server.event.model.EventDtos.EventInputDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getOwnComments(Long userId);

    CommentDto getOwnComment(Long userId, Long commentId);

    List<CommentDto> getOwnCommentsInEvent(Long userId, Long eventId);

    CommentDto addComment(Long userId, Long eventId, CommentInputDto commentInputDto);

    CommentDto editComment(Long userId, Long commentId, CommentInputDto commentInputDto);

    void deleteComment(Long userId, Long commentId);

    CommentDto editCommentAdmin(Long commentId, CommentInputDto commentInputDto);

    void deleteCommentAdmin(Long commentId);

    void disableUserCommenting(Long userId, String timeBlock);

    void enableUserCommenting(Long userId);
}