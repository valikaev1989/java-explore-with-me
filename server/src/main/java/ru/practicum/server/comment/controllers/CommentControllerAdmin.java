package ru.practicum.server.comment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.models.CommentDto;
import ru.practicum.server.comment.models.CommentInputDto;
import ru.practicum.server.comment.services.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentService commentService;

    @GetMapping("/users/{userId}")
    public List<CommentDto> getCommentsForAdminByUserId(@PathVariable(value = "userId") @Positive Long userId) {
        log.info("getCommentsForAdminByUserId: userId: {}", userId);
        return commentService.getCommentsForAdminByUserId(userId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentForAdmin(@PathVariable(value = "commentId") @Positive Long commentId) {
        log.info("getCommentForAdmin: commentId: {}", commentId);
        return commentService.getCommentForAdmin(commentId);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getCommentsForAdminInEvent(@PathVariable(value = "eventId") @Positive Long eventId) {
        log.info("getCommentsForAdminInEvent: eventId: {}", eventId);
        return commentService.getCommentsForAdminInEvent(eventId);
    }

    @GetMapping("/events/{eventId}/users/{userId}")
    public List<CommentDto> getCommentsForAdminInEventByUserId(
            @PathVariable(value = "userId") @Positive Long userId,
            @PathVariable(value = "eventId") @Positive Long eventId) {
        log.info("getCommentsForAdminInEventByUserId: userId: {}, eventId: {}", userId, eventId);
        return commentService.getCommentsForAdminInEventByUserId(userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto editCommentAdmin(@PathVariable(value = "commentId") @Positive Long commentId,
                                       @RequestBody @Valid CommentInputDto commentInputDto) {
        log.info("editCommentAdmin: commentId: {}, commentInputDto: {}", commentId, commentInputDto);
        return commentService.editCommentAdmin(commentId, commentInputDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentAdmin(@PathVariable(value = "commentId") @Positive Long commentId) {
        log.info("deleteCommentAdmin: commentId: {}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }

    @PatchMapping("/users/{userId}/disable")
    public void disableUserCommenting(
            @PathVariable(value = "userId") @Positive Long userId,
            @RequestParam(value = "timeBlock", required = false, defaultValue = "null") String timeBlock) {
        log.info("disableUserCommenting: userId: {}, timeBlock: {}", userId, timeBlock);
        commentService.disableUserCommenting(userId, timeBlock);
    }

    @PatchMapping("/users/{userId}/enable")
    public void enableUserCommenting(@PathVariable(value = "userId") @Positive Long userId) {
        log.info("disableUserCommenting: userId: {}", userId);
        commentService.enableUserCommenting(userId);
    }
}