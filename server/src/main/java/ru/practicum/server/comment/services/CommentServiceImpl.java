package ru.practicum.server.comment.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.comment.models.Comment;
import ru.practicum.server.comment.models.CommentDto;
import ru.practicum.server.comment.models.CommentInputDto;
import ru.practicum.server.comment.repositories.CommentRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.models.User;
import ru.practicum.server.user.repositories.UserRepository;
import ru.practicum.server.utils.CommentStatus;
import ru.practicum.server.utils.CommentValidator;
import ru.practicum.server.utils.EventValidator;
import ru.practicum.server.utils.UserValidator;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.server.comment.models.CommentMapper.*;
import static ru.practicum.server.utils.FormatDate.FORMATTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserValidator userValidator;
    private final EventValidator eventValidator;
    private final CommentValidator commentValidator;

    @Override
    public List<CommentDto> getOwnComments(Long userId) {
        log.info("getOwnComment start: userId: {}", userId);
        List<CommentDto> commentDtos = logicForGetCommentByUserId(userId);
        log.info("getOwnComment end: commentDtos.size(): {}", commentDtos.size());
        return commentDtos;
    }

    @Override
    public CommentDto getOwnComment(Long userId, Long commentId) {
        log.info("getOwnComment start: userId: {}, commentId: {}", userId, commentId);
        userValidator.validateAndReturnUserByUserId(userId);
        commentValidator.validateAndReturnCommentByCommentId(commentId);
        Comment comment = commentRepository.getCommentByAuthor_UserIdAndAndCommentId(userId, commentId);
        CommentDto commentDto = toCommentDto(comment);
        log.info("getOwnComment end: commentDto: {}", commentDto);
        return commentDto;
    }

    @Override
    public List<CommentDto> getOwnCommentsInEvent(Long userId, Long eventId) {
        log.info("addComment start: userId: {}, eventId:{}", userId, eventId);
        List<CommentDto> commentDtos = logicForGetCommentsByUserIdAndEventId(userId, eventId);
        log.info("getOwnComment end: commentDtos.size(): {}", commentDtos.size());
        return commentDtos;
    }

    @Override
    public CommentDto addComment(Long userId, Long eventId, CommentInputDto commentInputDto) {
        log.info("addComment start: userId: {}, eventId:{} , commentInputDto:{}", userId, eventId, commentInputDto);
        User user = getUserAfterCheckBlockParameters(userId);
        Event event = eventValidator.validateAndReturnEventByEventId(eventId);
        Comment comment = toComment(commentInputDto, user, event);
//        event.getComments().add(comment);
        CommentDto commentDto = toCommentDto(commentRepository.save(comment));
        log.info("addComment end: commentDto: {}", commentDto);
        return commentDto;
    }


    @Override
    public CommentDto editComment(Long userId, Long commentId, CommentInputDto commentInputDto) {
        log.info("editComment start: commentId: {}, commentInputDto: {}", commentId, commentInputDto);
        getUserAfterCheckBlockParameters(userId);
        Comment comment = commentValidator.validateAndReturnCommentByCommentId(commentId);
        commentValidator.validateOwnComment(userId, comment);
        commentValidator.validateAccessEditComment(comment);
        comment.setText(commentInputDto.getText());
        comment.setEditedOn(LocalDateTime.now());
        comment.setCommentStatus(CommentStatus.EditedByUser);
        CommentDto commentDto = toCommentDto(commentRepository.save(comment));
        log.info("editComment end: commentDto: {}", commentDto);
        return commentDto;
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
        log.info("editCommentAdmin start: commentId: {}, commentInputDto: {}", commentId, commentInputDto);
        Comment comment = commentValidator.validateAndReturnCommentByCommentId(commentId);
        comment.setText(commentInputDto.getText());
        comment.setCommentStatus(CommentStatus.EditedByAdmin);
        comment.setEditedOn(LocalDateTime.now());
        CommentDto commentDto = toCommentDto(commentRepository.save(comment));
        log.info("editCommentAdmin end: commentDto: {}", commentDto);
        return commentDto;
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
        if (timeBlock.equals("null")) {
            user.setPermissionToComment(false);
        } else {
            LocalDateTime localDateTime = LocalDateTime.parse(timeBlock, FORMATTER);
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

    @Override
    public CommentDto getCommentForAdmin(Long commentId) {
        log.info("getCommentForAdmin start: commentId: {}", commentId);
        commentValidator.validateAndReturnCommentByCommentId(commentId);
        CommentDto commentDto = toCommentDto(commentValidator.validateAndReturnCommentByCommentId(commentId));
        log.info("getCommentForAdmin end: commentDto: {}", commentDto);
        return commentDto;
    }

    @Override
    public List<CommentDto> getCommentsForAdminByUserId(Long userId) {
        log.info("getCommentsForAdminByUserId start: userId: {}", userId);
        List<CommentDto> commentDtos = logicForGetCommentByUserId(userId);
        log.info("getCommentsForAdminByUserId end: commentDtos.size(): {}", commentDtos.size());
        return commentDtos;
    }

    @Override
    public List<CommentDto> getCommentsForAdminInEvent(Long eventId) {
        log.info("getCommentsForAdminInEvent start: eventId:{}", eventId);
        eventValidator.validateAndReturnEventByEventId(eventId);
        List<CommentDto> commentDtos = commentDtoList(commentRepository.getAllByEvent_EventId(eventId));
        log.info("getCommentsForAdminInEventByUserId end: commentDtos.size(): {}", commentDtos.size());
        return commentDtos;
    }

    @Override
    public List<CommentDto> getCommentsForAdminInEventByUserId(Long userId, Long eventId) {
        log.info("getCommentsForAdminInEventByUserId start: userId: {}, eventId:{}", userId, eventId);
        List<CommentDto> commentDtos = logicForGetCommentsByUserIdAndEventId(userId, eventId);
        log.info("getCommentsForAdminInEventByUserId end: commentDtos.size(): {}", commentDtos.size());
        return commentDtos;
    }

    private List<CommentDto> logicForGetCommentByUserId(Long userId) {
        userValidator.validateAndReturnUserByUserId(userId);
        List<Comment> comments = commentRepository.getAllByAuthor_UserId(userId);
        return commentDtoList(comments);
    }

    private List<CommentDto> logicForGetCommentsByUserIdAndEventId(Long userId, Long eventId) {
        userValidator.validateAndReturnUserByUserId(userId);
        eventValidator.validateAndReturnEventByEventId(eventId);
        List<Comment> comments = commentRepository.getAllByAuthor_UserIdAndAndEvent_EventId(userId, eventId);
        return commentDtoList(comments);
    }

    private User getUserAfterCheckBlockParameters(Long userId) {
        User user = userValidator.validateAndReturnUserByUserId(userId);
        user.setPermissionToComment(user.getPermissionToComment() == null || user.getPermissionToComment());
        if (user.getBlockComments() != null) {
            if (user.getBlockComments().isBefore(LocalDateTime.now())) {
                user.setBlockComments(null);
            }
        }
        userRepository.save(user);
        commentValidator.validateDisableCommentingByUser(user);
        return user;
    }
}