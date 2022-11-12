package ru.practicum.server.event.model.EventDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdminParamsDto {
    List<Long> userIds;
    List<String> states;
    List<Long> categoryIds;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Pageable pageable;
}