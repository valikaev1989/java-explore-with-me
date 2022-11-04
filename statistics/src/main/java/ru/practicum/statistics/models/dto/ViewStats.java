package ru.practicum.statistics.models.dto;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}