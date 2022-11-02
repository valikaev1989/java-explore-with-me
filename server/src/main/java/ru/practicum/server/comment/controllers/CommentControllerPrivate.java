package ru.practicum.server.comment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.models.CommentDto;
import ru.practicum.server.comment.models.CommentInputDto;
import ru.practicum.server.comment.services.CommentService;
import ru.practicum.server.event.model.EventDtos.EventInputDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class CommentControllerPrivate {
    private final CommentService commentService;

    @GetMapping("/comments")
    public List<CommentDto> getOwnComments(@PathVariable(value = "userId") @Positive Long userId) {
        log.info("getOwnComments: userId: {}", userId);
        return commentService.getOwnComments(userId);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getOwnComment(@PathVariable(value = "userId") @Positive Long userId,
                                    @PathVariable(value = "commentId") @Positive Long commentId) {
        log.info("getOwnComment: userId: {}, commentId: {}", userId, commentId);
        return commentService.getOwnComment(userId, commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getOwnCommentsInEvent(@PathVariable(value = "userId") @Positive Long userId,
                                                  @PathVariable(value = "eventId") @Positive Long eventId) {
        log.info("getOwnComments: userId: {}, eventId: {}", userId, eventId);
        return commentService.getOwnCommentsInEvent(userId, eventId);
    }

    @PostMapping("/events/{eventId}/comments")

    public CommentDto addComment(@PathVariable(value = "userId") @Positive Long userId,
                                 @PathVariable(value = "eventId") @Positive Long eventId,
                                 @RequestBody @NotBlank CommentInputDto commentInputDto) {
        log.info("addComment: userId: {}, eventId: {}, commentInputDto: {}", userId, eventId, commentInputDto);
        return commentService.addComment(userId, eventId, commentInputDto);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto editCommentUser(@PathVariable(value = "userId") @Positive Long userId,
                                      @PathVariable(value = "commentId") @Positive Long commentId,
                                      @RequestBody @NotBlank CommentInputDto commentInputDto) {
        log.info("editComment: userId: {}, commentId: {}, commentInputDto: {}", userId, commentId, commentInputDto);
        return commentService.editComment(userId, commentId, commentInputDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteCommentUser(@PathVariable(value = "userId") @Positive Long userId,
                                  @PathVariable(value = "commentId") @Positive Long commentId) {
        log.info("deleteComment: userId: {}, commentId: {}", userId, commentId);
        commentService.deleteComment(userId, commentId);
    }
}