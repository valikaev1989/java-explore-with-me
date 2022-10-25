package ru.practicum.server.category.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;
import ru.practicum.server.category.services.CategoryService;
import ru.practicum.server.utils.FormatPage;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getAllCategory(
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        System.out.println("");
        log.info("CategoryControllerPublic.getAllCategory from={}, size={}", from, size);
        return categoryService.getAllCategories(FormatPage.getPage(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @Min(0) Long catId) {
        System.out.println("");
        log.info("CategoryControllerPublic.getCategoryById catId={}", catId);
        return categoryService.getCategoryById(catId);
    }
}