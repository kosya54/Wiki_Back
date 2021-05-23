package com.kosenko.wikirest.repository;

import com.kosenko.wikirest.entity.article.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    Category findCategoryById(Long id);

    Category findCategoryByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
