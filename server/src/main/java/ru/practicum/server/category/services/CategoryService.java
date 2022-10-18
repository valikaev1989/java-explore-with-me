package ru.practicum.server.category.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(Pageable pageable);

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long categoryId);

    void deleteCategoryById(Long categoryId);
}