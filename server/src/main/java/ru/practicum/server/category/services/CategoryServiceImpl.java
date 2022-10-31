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
        log.info("CategoryService.getAllCategories start: pageable: {}", pageable);
        List<CategoryDto> categoryDtoList = CategoryMapper.toCategoryDtoList(categoryRepository.findAll(pageable));
        log.info("CategoryService.getAllCategories end: categoryDtoList{}", categoryDtoList);
        return categoryDtoList;
    }

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("CategoryService.addCategory start categoryDto:{}", categoryDto);
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(
                categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
        log.info("CategoryService.addCategory end categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.info("CategoryService.updateCategory start categoryDto:{}", categoryDto);
        Category category = validator.validateAndReturnCategoryByCategoryId(categoryDto.getId());
        category.setName(categoryDto.getName());
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        log.info("CategoryService.updateCategory end categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        log.info("CategoryService.getCategoryById start categoryId:{}", categoryId);
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(
                validator.validateAndReturnCategoryByCategoryId(categoryId));
        log.info("CategoryService.getCategoryById end categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long categoryId) {
        log.info("CategoryService.deleteCategoryById start categoryId:{}", categoryId);
        validator.validateAndReturnCategoryByCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("CategoryService.deleteCategoryById end categoryId:{}", categoryId);
    }
}