package ru.practicum.ewm.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.interfaces.CategoryService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Call 'getCategories': from = {}, size = {}", from, size);

        return categoryService.getCategories(from, size);
    }

    @GetMapping(value = "/{catId}")
    public CategoryDto getCategory(@PathVariable @Positive Long catId) {

        log.info("Call 'getCategory': id = {}", catId);

        return categoryService.getCategory(catId);
    }
}
