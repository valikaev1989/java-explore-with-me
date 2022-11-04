package ru.practicum.statistics.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EndpointDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}