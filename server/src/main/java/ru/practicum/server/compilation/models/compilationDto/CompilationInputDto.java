package ru.practicum.server.compilation.models.compilationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompilationInputDto {
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
    @NotNull
    private List<Long> events;
}