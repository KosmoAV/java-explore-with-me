package ru.practicum.ewm.categories;

import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.model.Category;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {

        Category category = new Category();
        category.setName(newCategoryDto.getName());

        return category;
    }

    public static Category toCategory(CategoryDto categoryDto) {

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }
}
