package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategory);

    CategoryDto update(long catId, CategoryDto categoryDto);

    void delete(long catId);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(long catId);
}
