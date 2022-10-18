package ru.practicum.server.category.model.categoryDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long categoryId;
    @NotBlank
    private String name;
}
