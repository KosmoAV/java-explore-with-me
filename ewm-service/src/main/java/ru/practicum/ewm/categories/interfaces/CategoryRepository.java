package ru.practicum.ewm.categories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
