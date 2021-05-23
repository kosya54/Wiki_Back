package com.kosenko.wikirest.service;

import com.kosenko.wikirest.entity.article.Article;
import com.kosenko.wikirest.entity.article.Category;
import com.kosenko.wikirest.entity.dto.NewArticleDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    void addNewArticle(List<NewArticleDto> newArticleDtoList);

    Article getArticleById(Long id);

    List<Article> getAllArticles();

    List<Article> getArticlesByLimit(int page, int size);

    List<Article> getArticlesByCategoryId(Long id, int page, int size);

    void deleteArticleById(Long id);

    void deleteArticlesById(List<Long> ids);

//Категории

    Category addNewCategory(Category category);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id, Category category);

    void deleteCategoryById(Long id);
}
