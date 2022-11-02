package ru.practicum.server.comment.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.server.comment.models.Comment;
import ru.practicum.server.comment.models.CommentDto;
import ru.practicum.server.comment.models.CommentInputDto;
import ru.practicum.server.comment.repositories.CommentRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.event.repositories.EventRepository;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.repositories.UserRepository;
import ru.practicum.server.utils.CommentValidator;
import ru.practicum.server.utils.EventValidator;
import ru.practicum.server.utils.UserValidator;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.server.comment.models.CommentMapper.toComment;
import static ru.practicum.server.comment.models.CommentMapper.toCommentDto;
import static ru.practicum.server.utils.FormatDate.FORMATTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserValidator userValidator;
    private final EventValidator eventValidator;
    private final CommentValidator commentValidator;

    @Override
    public List<CommentDto> getOwnComments(Long userId) {
        return null;
    }

    @Override
    public CommentDto getOwnComment(Long userId, Long commentId) {
        return null;
    }

    @Override
    public List<CommentDto> getOwnCommentsInEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    public CommentDto addComment(Long userId, Long eventId, CommentInputDto commentInputDto) {
        log.info("addComment start: userId: {}, eventId:{} , commentInputDto:{}", userId, eventId, commentInputDto);
        User user = userValidator.validateAndReturnUserByUserId(userId);
        commentValidator.validateDisableCommentingByUser(user);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        CommentDto commentDto = toCommentDto(commentRepository.save(toComment(commentInputDto, user, event)));
        log.info("addComment end: commentDto: {}", commentDto);
        return commentDto;
    }

    @Override
    public CommentDto editComment(Long userId, Long commentId, CommentInputDto commentInputDto) {
        return null;
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        log.info("deleteComment start: userId: {}, commentId: {}", userId, commentId);
        Comment comment = commentValidator.validateAndReturnCommentByCommentId(commentId);
        commentValidator.validateOwnComment(userId, comment);
        commentRepository.delete(comment);
        log.info("deleteComment end: ok");
    }

    @Override
    public CommentDto editCommentAdmin(Long commentId, CommentInputDto commentInputDto) {
        return null;
    }

    @Override
    public void deleteCommentAdmin(Long commentId) {
        log.info("deleteCommentAdmin start: commentId: {}", commentId);
        Comment comment = commentValidator.validateAndReturnCommentByCommentId(commentId);
        commentRepository.delete(comment);
        log.info("deleteComment end: ok");
    }

    @Override
    public void disableUserCommenting(Long userId, String timeBlock) {
        log.info("disableUserCommenting start: userId:{}, timeBlock: {}", userId, timeBlock);
        User user = userValidator.validateAndReturnUserByUserId(userId);
        LocalDateTime localDateTime = LocalDateTime.parse(timeBlock, FORMATTER);
        if (timeBlock.equals("null")) {
            user.setPermissionToComment(false);
        } else {
            user.setBlockComments(localDateTime);
        }
        log.info("disableUserCommenting end: user: {}", userRepository.save(user));
    }

    @Override
    public void enableUserCommenting(Long userId) {
        log.info("disableUserCommenting start: userId:{}", userId);
        User user = userValidator.validateAndReturnUserByUserId(userId);
        user.setPermissionToComment(true);
        user.setBlockComments(null);
        User result = userRepository.save(user);
        log.info("disableUserCommenting end: user: {}", result);
    }
}