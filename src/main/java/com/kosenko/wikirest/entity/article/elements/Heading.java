package com.kosenko.wikirest.entity.article.elements;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "headings")
public class Heading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullArticle.class)
    private Long id;

    @Column(columnDefinition = "MEDIUMTEXT")
    @JsonView(Views.FullArticle.class)
    private String content;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "heading_id")
    @JsonView(Views.FullArticle.class)
    List<Paragraph> paragraphs;

    public Heading() {
        paragraphs = new ArrayList<>();
    }

    public Heading(String content, List<Paragraph> paragraphs) {
        this.content = content;
        this.paragraphs = paragraphs;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public void addParagraph(Paragraph paragraph) {
        paragraphs.add(paragraph);
    }
}
