package com.kosenko.wikirest.entity.article.elements;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;

import javax.persistence.*;

@Entity
@Table(name = "numberings")
public class Numbering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullArticle.class)
    private Long id;

    @JsonView(Views.FullArticle.class)
    private int level;

    @Column(columnDefinition = "TEXT")
    @JsonView(Views.FullArticle.class)
    private String text;

    @JsonView(Views.FullArticle.class)
    private boolean isDecimal = true;

    public Numbering() {

    }

    public Numbering(int level, String text) {
        this.level = level;
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDecimal() {
        return isDecimal;
    }

    public void setDecimal(boolean decimal) {
        isDecimal = decimal;
    }

    @Override
    public String toString() {
        return "Level: " + level + " isDecimal: " + isDecimal + " Text: " + text;
    }
}
