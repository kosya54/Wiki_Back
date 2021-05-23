package com.kosenko.wikirest.repository;

import com.kosenko.wikirest.entity.article.Article;
import com.kosenko.wikirest.entity.article.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findArticleById(Long id);

//    Article findArticlesByTitle(String title);

    List<Article> findAllByOrderByPostedDateDesc(Pageable pageable);

    List<Article> findAllByCategoriesOrderByPostedDate(Category category, Pageable pageable);

    List<Article> findAllByCategories(Category category);

    boolean existsArticleByTitle(String title);
}
