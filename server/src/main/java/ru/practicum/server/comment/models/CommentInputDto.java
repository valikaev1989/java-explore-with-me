package ru.practicum.server.comment.models;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentInputDto {
    private String text;
}