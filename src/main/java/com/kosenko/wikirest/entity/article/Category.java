package com.kosenko.wikirest.entity.article;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.ArticleListView.class)
    private Long id;

    @NotBlank(message = "Название не должно быть пустым")
    @JsonView(Views.ArticleListView.class)
    private String name;

    public Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
