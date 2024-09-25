package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;
import ru.practicum.exception.DataNotFoundException;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    default Category checkAndGetCategory(long categoryId) {
        return this.findById(categoryId)
                .orElseThrow(() -> {
//                    log.debug("GET CATEGORY. Категория с айди {} не найдена.", categoryId);
                    return new DataNotFoundException("Категории с id=" + categoryId + " не существует.");
                });
    }
}
