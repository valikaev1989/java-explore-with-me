package ru.practicum.server.clientStatistics.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EndpointDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}