package com.kosenko.wikirest.entity.article.elements;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;
import com.kosenko.wikirest.entity.article.elements.table.TableElement;

@Entity
@Table(name = "paragraphs")
public class Paragraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullArticle.class)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    @JsonView(Views.FullArticle.class)
    private String content;

    @ElementCollection
    @CollectionTable(name = "images", joinColumns = @JoinColumn(name = "paragraph_id"))
    @JsonView(Views.FullArticle.class)
    private List<String> imageNames;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id")
    @JsonView(Views.FullArticle.class)
    private List<Numbering> numberings;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id")
    @JsonView(Views.FullArticle.class)
    private final List<TableElement> tables;

    public Paragraph() {
        imageNames = new ArrayList<>();
        numberings = new ArrayList<>();
        tables = new ArrayList<>();
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

    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> images) {
        this.imageNames = images;
    }

    public void addImageName(String name) {
        imageNames.add(name);
    }

    public List<Numbering> getNumberings() {
        return numberings;
    }

    public void setNumberings(List<Numbering> numberings) {
        this.numberings = numberings;
    }

    public void addNumbering(Numbering numbering) {
        numberings.add(numbering);
    }

    public List<TableElement> getTables() {
        return tables;
    }

    public void addTableElement(TableElement table) {
        tables.add(table);
    }
}
