package ru.practicum.server.category.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.model.categoryDtos.CategoryMapper;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;
import ru.practicum.server.category.repositories.CategoryRepository;
import ru.practicum.server.utils.CategoryValidator;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator validator;

    @Override
    public List<CategoryDto> getAllCategories(Pageable pageable) {
        log.info("getAllCategories start: pageable: {}", pageable);
        List<CategoryDto> categoryDtoList = CategoryMapper.toCategoryDtoList(categoryRepository.findAll(pageable));
        log.info("getAllCategories end: categoryDtoList{}", categoryDtoList);
        return categoryDtoList;
    }

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("addCategory start: categoryDto:{}", categoryDto);
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(
                categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
        log.info("addCategory end: categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.info("updateCategory start: categoryDto:{}", categoryDto);
        Category category = validator.validateAndReturnCategoryByCategoryId(categoryDto.getId());
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.info("updateCategory end: categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        log.info("getCategoryById start: categoryId:{}", categoryId);
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(
                validator.validateAndReturnCategoryByCategoryId(categoryId));
        log.info("getCategoryById end: categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long categoryId) {
        log.info("deleteCategoryById start: categoryId:{}", categoryId);
        validator.validateAndReturnCategoryByCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("deleteCategoryById end: categoryId:{}", categoryId);
    }
}