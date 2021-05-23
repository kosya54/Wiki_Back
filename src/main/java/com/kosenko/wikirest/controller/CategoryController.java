package com.kosenko.wikirest.controller;

import com.kosenko.wikirest.entity.article.Category;
import com.kosenko.wikirest.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final ArticleService articleService;

    @Autowired
    public CategoryController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(articleService.addNewCategory(category), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<Category> read(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(articleService.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<Category>> read() {
        return new ResponseEntity<>(articleService.getAllCategories(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<Category> update(@PathVariable(name = "id") Long id, @Valid @RequestBody Category category) {
        return new ResponseEntity<>(articleService.updateCategory(id, category), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        articleService.deleteCategoryById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
