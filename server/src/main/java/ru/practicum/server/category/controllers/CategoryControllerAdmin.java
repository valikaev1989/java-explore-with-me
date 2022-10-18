package ru.practicum.server.category.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;
import ru.practicum.server.category.services.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping()
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("CategoryControllerAdmin.addCategory: categoryDto{}", categoryDto);
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("CategoryControllerAdmin.addCategory: categoryDto{}", categoryDto);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategoryById(@PathVariable @Min(0) Long catId) {
        log.info("CategoryControllerAdmin.deleteCategoryById: catId{}", catId);
        categoryService.deleteCategoryById(catId);
    }
}