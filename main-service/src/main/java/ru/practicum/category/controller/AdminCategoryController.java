package ru.practicum.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategory) {
        log.info("POST /admin/categories request: {}", newCategory);
        CategoryDto categoryDto = categoryService.create(newCategory);
        log.info("POST /admin/categories response: {}", categoryDto);
        return categoryDto;
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto, @PathVariable @Positive long catId) {
        log.info("PATCH /admin/categories/{} request: {}", catId, categoryDto);
        CategoryDto updatedCategory = categoryService.update(catId, categoryDto);
        log.info("PATCH /admin/categories/{} response: {}", catId, updatedCategory);
        return updatedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long catId) {
        log.info("DELETE /admin/categories/{} request", catId);
        categoryService.delete(catId);
        log.info("DELETE /admin/categories/{} response: success", catId);
    }
}
