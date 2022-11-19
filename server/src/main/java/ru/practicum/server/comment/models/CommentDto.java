package ru.practicum.server.comment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.event.model.EventDtos.EventDtoForComment;
import ru.practicum.server.user.models.userDtos.UserOutputDto;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private EventDtoForComment event;
    private UserOutputDto author;
    private String createdOn;
    private String editedOn;
    private String commentStatus;
}