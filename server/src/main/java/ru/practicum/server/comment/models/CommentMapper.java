package ru.practicum.server.comment.models;

import ru.practicum.server.event.model.Event;
import ru.practicum.server.user.models.User;
import ru.practicum.server.utils.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.event.model.EventDtos.EventMapper.toDtoForComment;
import static ru.practicum.server.user.models.userDtos.UserMapper.toDto;
import static ru.practicum.server.utils.FormatDate.FORMATTER;

public class CommentMapper {
    public static Comment toComment(CommentInputDto commentInputDto, User user, Event event) {
        return Comment.builder()
                .text(commentInputDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .commentStatus(CommentStatus.NOT_EDITED)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getCommentId())
                .author(toDto(comment.getAuthor()))
                .event(toDtoForComment(comment.getEvent()))
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(FORMATTER))
                .editedOn(comment.getEditedOn() != null ? comment.getEditedOn().format(FORMATTER) : "null")
                .commentStatus(comment.getCommentStatus().name())
                .build();
    }

    public static List<CommentDto> commentDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
