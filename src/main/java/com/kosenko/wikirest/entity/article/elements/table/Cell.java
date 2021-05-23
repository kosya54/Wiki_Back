package com.kosenko.wikirest.entity.article.elements.table;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;

import javax.persistence.*;

@Entity
@Table(name = "cell_elements")
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullArticle.class)
    private Long id;

    @Column(columnDefinition = "TEXT")
    @JsonView(Views.FullArticle.class)
    private String content;

    @JsonView(Views.FullArticle.class)
    private boolean isMerged = false;

    public Cell() {

    }

    public Cell(String content) {
        this.content = content;
    }

    public Cell(String content, boolean isMerged) {
        this.content = content;
        this.isMerged = isMerged;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMerged() {
        return isMerged;
    }

    public void setMerged(boolean merged) {
        isMerged = merged;
    }

    @Override
    public String toString() {
        return content;
    }
}
