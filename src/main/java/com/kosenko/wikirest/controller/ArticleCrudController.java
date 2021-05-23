package com.kosenko.wikirest.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;
import com.kosenko.wikirest.entity.article.Article;
import com.kosenko.wikirest.entity.dto.NewArticleDto;
import com.kosenko.wikirest.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleCrudController {
    private final ArticleService articleService;

    @Autowired
    public ArticleCrudController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> create(@RequestBody List<NewArticleDto> newArticleDtoList) {
        articleService.addNewArticle(newArticleDtoList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @JsonView(Views.FullArticle.class)
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<Article> read(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(articleService.getArticleById(id), HttpStatus.OK);
    }

    @GetMapping
    @JsonView(Views.ArticleListView.class)
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<Article>> read(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return new ResponseEntity<>(articleService.getArticlesByLimit(page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/categories/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<Article>> read(
            @PathVariable(name = "id") Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return new ResponseEntity<>(articleService.getArticlesByCategoryId(categoryId, page, size), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        articleService.deleteArticleById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids) {
        articleService.deleteArticlesById(ids);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
