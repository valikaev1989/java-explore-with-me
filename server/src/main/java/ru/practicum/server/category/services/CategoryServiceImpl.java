package ru.practicum.server.category.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import ru.practicum.server.category.model.categoryDtos.CategoryMapper;
import ru.practicum.server.category.model.categoryDtos.CategoryDto;
import ru.practicum.server.category.repositories.CategoryRepository;
import ru.practicum.server.utils.Validation;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Validation validation;

    @Override
    public List<CategoryDto> getAllCategories(Pageable pageable) {
        log.info("CategoryService.getAllCategories start");
        List<CategoryDto> categoryDtoList = CategoryMapper.toCategoryDtoList(categoryRepository.findAll(pageable));
        log.info("CategoryService.getAllCategories end: categoryDtoList{}", categoryDtoList);
        return categoryDtoList;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        log.info("CategoryService.addCategory start categoryDto:{}", categoryDto);
        CategoryDto categoryDtoResult = getResultForAddAndUpdate(categoryDto);
        log.info("CategoryService.addCategory end categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.info("CategoryService.updateCategory start categoryDto:{}", categoryDto);
        CategoryDto categoryDtoResult = getResultForAddAndUpdate(categoryDto);
        log.info("CategoryService.updateCategory end categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        log.info("CategoryService.getCategoryById start categoryId:{}", categoryId);
        CategoryDto categoryDtoResult = CategoryMapper.toCategoryDto(
                validation.validateAndReturnCategoryByCategoryId(categoryId));
        log.info("CategoryService.getCategoryById end categoryDtoResult:{}", categoryDtoResult);
        return categoryDtoResult;
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        log.info("CategoryService.deleteCategoryById start categoryId:{}", categoryId);
        validation.validateAndReturnCategoryByCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
        log.info("CategoryService.deleteCategoryById end categoryId:{}", categoryId);
    }

    private CategoryDto getResultForAddAndUpdate(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(
                categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }
}