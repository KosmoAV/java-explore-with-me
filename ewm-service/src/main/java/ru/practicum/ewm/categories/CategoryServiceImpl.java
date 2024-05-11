package ru.practicum.ewm.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.interfaces.CategoryRepository;
import ru.practicum.ewm.categories.interfaces.CategoryService;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.events.interfaces.EventRepository;
import ru.practicum.ewm.exception.ConflictRequestException;
import ru.practicum.ewm.exception.DataNotFoundRequestException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {

        Category category = CategoryMapper.toCategory(newCategoryDto);

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long catId) {

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictRequestException("Cannot delete Category with id = " + catId +
                    " because exists events with this Category");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        return categoryRepository.findAll(page).get()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundRequestException("Category with id = " + catId
                        + " not found"));

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {

        if (!categoryRepository.existsById(catId)) {
            throw new DataNotFoundRequestException("Category with id = " + catId
                    + " not found");
        }

        categoryDto.setId(catId);

        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));

        return CategoryMapper.toCategoryDto(category);
    }
}
