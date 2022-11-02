package ru.practicum.server.comment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.models.CommentDto;
import ru.practicum.server.comment.models.CommentInputDto;
import ru.practicum.server.comment.services.CommentService;
import ru.practicum.server.event.model.EventDtos.EventInputDto;
import ru.practicum.server.user.models.userDtos.UserOutputDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentService commentService;

    @PatchMapping("/admin/comments{commentId}")
    public CommentDto editCommentAdmin(@PathVariable(value = "commentId") @Positive Long commentId,
                                       @RequestBody @NotBlank CommentInputDto commentInputDto) {
        log.info("editComment: commentId: {}, commentInputDto: {}", commentId, commentInputDto);
        return commentService.editCommentAdmin(commentId, commentInputDto);
    }

    @DeleteMapping("/admin/comments{commentId}")
    public void deleteCommentAdmin(@PathVariable(value = "commentId") @Positive Long commentId) {
        log.info("deleteCommentAdmin: commentId: {}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }

    @PatchMapping("/users/{userId}/disable")
    public void disableUserCommenting(@PathVariable(value = "userId") @Positive Long userId,
                                      @RequestParam(value = "timeBlock", required = false) String timeBlock) {
        log.info("disableUserCommenting: userId: {}, timeBlock: {}", userId, timeBlock);
        commentService.disableUserCommenting(userId, timeBlock);
    }

    @PatchMapping("/users/{userId}/enable")
    public void enableUserCommenting(@PathVariable(value = "userId") @Positive Long userId) {
        log.info("disableUserCommenting: userId: {}", userId);
        commentService.enableUserCommenting(userId);
    }
}