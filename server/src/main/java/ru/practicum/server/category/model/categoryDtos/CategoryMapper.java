package ru.practicum.server.category.model.categoryDtos;

import org.springframework.data.domain.Page;
import ru.practicum.server.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDtoList(Page<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}