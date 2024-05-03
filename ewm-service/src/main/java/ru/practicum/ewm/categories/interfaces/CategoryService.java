package ru.practicum.ewm.categories.interfaces;

import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    public CategoryDto addCategory(NewCategoryDto newCategoryDto);

    public void deleteCategory(Long catId);

    public List<CategoryDto> getCategories(Integer from, Integer size);

    public CategoryDto getCategory(Long catId);
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId);
}
