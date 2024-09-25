package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategory) {
        Category category = categoryMapper.toCategory(newCategory);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto update(long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.checkAndGetCategory(catId);
        category.setName(categoryDto.getName());
        return categoryMapper.toDto(category);
    }

    @Transactional
    @Override
    public void delete(long catId) {
        //ToDo Добавить проверку: "с категорией не должно быть связано ни одного события."
        categoryRepository.deleteById(catId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);

        return categoryRepository.findAll(page).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getById(long catId) {
        Category category = categoryRepository.checkAndGetCategory(catId);

        return categoryMapper.toDto(category);
    }
}
