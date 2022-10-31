package ru.practicum.server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repositories.CategoryRepository;
import ru.practicum.server.exception.models.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public Category validateAndReturnCategoryByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("категория с id '%d' не найдена", categoryId)));
    }

    public List<Long> getCorrectCategoryIdList(List<Long> ids) {
        log.info("Validation.getCorrectCategoryIdList start: ids:");
        ids.forEach(id -> log.info("{}", id));
        List<Long> categoryIds = new ArrayList<>();
        for (Long id : ids) {
            try {
                Category category = validateAndReturnCategoryByCategoryId(id);
                categoryIds.add(category.getCategoryId());
            } catch (NotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
        log.info("Validation.getCorrectCategoryIdList end: CategoryIds:");
        categoryIds.forEach(id -> log.info("{}", id));
        return categoryIds;
    }
}